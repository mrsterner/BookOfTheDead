package dev.sterner.book_of_the_dead.api;

import com.google.common.collect.Lists;
import com.mojang.brigadier.ParseResults;
import dev.sterner.book_of_the_dead.api.interfaces.IRitual;
import dev.sterner.book_of_the_dead.common.block.entity.PedestalBlockEntity;
import dev.sterner.book_of_the_dead.common.block.entity.RitualBlockEntity;
import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.LivingEntityDataComponent;
import dev.sterner.book_of_the_dead.common.item.ContractItem;
import dev.sterner.book_of_the_dead.common.recipe.RitualRecipe;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.registry.BotDSoundEvents;
import dev.sterner.book_of_the_dead.common.registry.BotDStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;


public class NecrotableRitual implements IRitual {
	private final Identifier id;
	public Vec3d ritualCenter = null;
	public RitualRecipe recipe;
	public int ticker = 0;
	public UUID userUuid = null;
	public int height = 0;
	private int index = 0;
	private boolean canEndRitual = false;
	private boolean lockTick = false;
	public int contract = 0;
	public int contract2 = 0;

	public NecrotableRitual(Identifier id) {
		this.id = id;
	}

	//Called methods from RitualBlockEntity

	@Override
	public void tick(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		ticker++;
		canEndRitual = this.consumeItems(world, blockPos, blockEntity) && this.consumeSacrifices(world, blockPos, blockEntity);
		if(canEndRitual || lockTick){
			if(!this.lockTick){
				this.runCommand(world, blockEntity, blockPos, "start");
			}
			this.lockTick = true;
			this.runCommand(world, blockEntity, blockPos, "tick");
			this.generateStatusEffects(world, blockPos, blockEntity);
		}
	}

	@Override
	public void onStopped(World world, BlockPos blockPos, RitualBlockEntity blockEntity){
		ticker = 0;
		index = 0;
		if(lockTick){
			if(userUuid == null){
				PlayerEntity player = world.getClosestPlayer(blockPos.getZ(), blockPos.getY(), blockPos.getZ(), 16D, true);
				if(player != null){
					userUuid = player.getUuid();
				}
			}

			this.runCommand(world, blockEntity, blockPos, "end");
			this.summonSummons(world, blockPos, blockEntity);
			this.summonItems(world, blockPos, blockEntity);
			this.lockTick = false;
		}
	}

	//RITUAL MANAGER

	/**
	 * Drops all resulting items at ritual origin
	 * @param world world
	 * @param blockPos ritual origin
	 * @param blockEntity ritualBlockEntity
	 */
	private void summonItems(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		double x = blockPos.getX() + 0.5;
		double y = blockPos.getY() + 0.5;
		double z = blockPos.getZ() + 0.5;

		if (blockEntity.currentNecrotableRitual != null && recipe != null) {
			if (world instanceof ServerWorld serverWorld) {
				serverWorld.playSound(null, x, y, z, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1F, 1F);
			}
			if (recipe.outputs != null) {
				for (ItemStack output : recipe.outputs) {
					ItemScatterer.spawn(world, x, y, z, output.copy());
				}
			}
		}
	}

	/**
	 * Spawns all entities which was specified in the ritual recipe at ritual origin
	 * @param world world
	 * @param blockPos ritual origin
	 * @param blockEntity ritualBlockEntity
	 */
	private void summonSummons(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		if(blockEntity.ritualRecipe.summons != null){
			for(EntityType<?> entityType : blockEntity.ritualRecipe.summons){
				var entity = entityType.create(world);
				if(entity instanceof LivingEntity livingEntity){
					Vec3i worldPos = new Vec3i(blockPos.getX(), blockPos.getY(), blockPos.getZ());
					livingEntity.refreshPositionAndAngles(new BlockPos(worldPos), world.random.nextFloat(), world.random.nextFloat());
					world.spawnEntity(livingEntity);
				}
			}
		}
	}

	/**
	 * Adds all statusEffects to the user of the ritual
	 * @param world world
	 * @param blockPos ritual origin
	 * @param blockEntity ritualBlockEntity
	 */
	private void generateStatusEffects(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		int size = 16;
		if(recipe.statusEffectInstance != null){
			List<LivingEntity> livingEntityList = world.getEntitiesByClass(LivingEntity.class, new Box(blockPos).expand(size), Entity::isAlive);
			for(LivingEntity living : livingEntityList){
				for(StatusEffectInstance instance : recipe.statusEffectInstance){
					if(living.canHaveStatusEffect(instance)){
						living.addStatusEffect(instance);
					}
				}
			}
		}
	}

	public Identifier getId() {
		return id;
	}

	/**
	 * Check if the pedestals have appropriate items matching recipe and consumes them in succession
	 * @param world world
	 * @param blockPos ritual origin
	 * @param blockEntity ritualBlockEntity
	 * @return true if all items where consumed
	 */
	private boolean consumeItems(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		if(blockEntity.ritualRecipe.inputs != null && blockEntity.ritualRecipe.inputs.isEmpty()) return true;
		double x = blockPos.getX() + 0.5;
		double y = blockPos.getY() + 0.5 + height;
		double z = blockPos.getZ() + 0.5;
		List<BlockPos> pedestalToActivate = new ArrayList<>();
		List<Pair<ItemStack, BlockPos>> stream = blockEntity.getPedestalInfo(world).stream().filter(itemStackBlockPosPair -> !itemStackBlockPosPair.getLeft().isEmpty()).toList();
		int dividedTime = recipe.getDuration();
		if(stream.size() > 0){
			dividedTime = (dividedTime / (stream.size() + 1));
		}

		for (Pair<ItemStack, BlockPos> itemStackBlockPosPair : stream) {
			if (recipe.inputs != null) {
				for(Ingredient ingredient : recipe.inputs){
					if (ingredient.test(itemStackBlockPosPair.getLeft())) {
						BlockPos checkPos = itemStackBlockPosPair.getRight().add(blockPos.getX(), blockPos.getY(), blockPos.getZ());
						if(world.getBlockEntity(checkPos) instanceof PedestalBlockEntity){
							if(itemStackBlockPosPair.getLeft().isOf(BotDObjects.CONTRACT)){
								ItemStack contract = itemStackBlockPosPair.getLeft();
								if (this.contract == 0) {
									this.contract = ContractItem.getIdFromContractNbt(contract);
								} else if (this.contract2 == 0) {
									this.contract2 = ContractItem.getIdFromContractNbt(contract);
								}
							}
							pedestalToActivate.add(checkPos);
						}
					}
				}
			}
		}
		if(ticker == 1 || (ticker + 1) % dividedTime == 0){
			if(index < pedestalToActivate.size() && world.getBlockEntity(pedestalToActivate.get(index)) instanceof PedestalBlockEntity pedestalBlockEntity){
				world.playSound(null, pedestalToActivate.get(index).getX(), pedestalToActivate.get(index).getY(), pedestalToActivate.get(index).getZ(), BotDSoundEvents.MISC_ITEM_BEAM, SoundCategory.BLOCKS, 0.75f, 0.75f * world.random.nextFloat() / 2);
				pedestalBlockEntity.setCrafting(true);
				pedestalBlockEntity.duration = dividedTime;
				pedestalBlockEntity.targetY = height;
			}
			index++;
		}

		if(world instanceof ServerWorld serverWorld) {
			this.generateFX(serverWorld, x, y, z);
		}
		return index == pedestalToActivate.size() + 1;
	}

	/**
	 * Check and kill sacrifices around the ritual origin depending on recipe
	 * @param world world
	 * @param blockPos origin
	 * @param blockEntity ritualBlockEntity
	 * @return true if sacrifice was successful
	 */
	private boolean consumeSacrifices(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		if (blockEntity.ritualRecipe.sacrifices != null && blockEntity.ritualRecipe.sacrifices.isEmpty()) {
			return true;
		}

		int size = 8;

		List<LivingEntity> livingEntityList = world.getEntitiesByClass(LivingEntity.class, new Box(blockPos).expand(size), Entity::isAlive);
		List<EntityType<?>> entityTypeList = Lists.newArrayList(livingEntityList.stream().map(Entity::getType).toList());
		List<EntityType<?>> ritualSacrifices = blockEntity.ritualRecipe.sacrifices;

		if (ritualSacrifices != null && new HashSet<>(entityTypeList).containsAll(ritualSacrifices)) {
			for (EntityType<?> entityType : ritualSacrifices) {
				LivingEntity foundEntity = getClosestEntity(livingEntityList, entityType, blockPos);
				if (foundEntity != null) {
					LivingEntityDataComponent livingEntityDataComponent = BotDComponents.LIVING_COMPONENT.get(foundEntity);
					livingEntityDataComponent.setRitualPos(ritualCenter);
					foundEntity.addStatusEffect(new StatusEffectInstance(BotDStatusEffects.SOUL_SIPHON, 20 * 3));
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Gets closes entity of a specific type
	 * @param entityList list of entities to test
	 * @param type entityType to look for
	 * @param pos position to measure distance from
	 * @return Entity closest to pos from entityList
	 */
	public <T extends LivingEntity> T getClosestEntity(List<? extends T> entityList, EntityType<?> type, BlockPos pos) {
		double d = -1.0;
		T livingEntity = null;
		for (T livingEntity2 : entityList) {
			double e = livingEntity2.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ());
			if (livingEntity2.getType() == type && (d == -1.0 || e < d)) {
				d = e;
				livingEntity = livingEntity2;
			}
		}
		return livingEntity;
	}

	/**
	 * Handles the output items sound and particle effect
	 * @param serverWorld serverWorld
	 * @param x coordinate for sound and particle
	 * @param y coordinate for sound and particle
	 * @param z coordinate for sound and particle
	 */
	private void generateFX(ServerWorld serverWorld, double x, double y, double z) {
		if(ticker % 5 == 0 && ticker < recipe.getDuration() - 40){
			serverWorld.playSound(null, x,y,z, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 10,0.5f);
		}
		if (recipe.outputs != null) {
			for(ItemStack output : recipe.outputs){
				for (int i = 0; i < recipe.outputs.size() * 2; i++) {
					serverWorld.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, output),
							x + ((serverWorld.random.nextDouble() / 2) - 0.25),
							y + ((serverWorld.random.nextDouble() / 2) - 0.25),
							z + ((serverWorld.random.nextDouble() / 2) - 0.25),
							0,
							1 * ((serverWorld.random.nextDouble() / 2) - 0.25),
							1 * ((serverWorld.random.nextDouble() / 2) - 0.25),
							1 * ((serverWorld.random.nextDouble() / 2) - 0.25),
							0);
				}
			}
		}
	}

	/**
	 * Runs a command depending on which key phrase is used, "start", "tick", "end"
	 * @param world world
	 * @param blockEntity ritualBlockEntity
	 * @param blockPos pos of the ritual origin
	 * @param phase keyword for which phase the command should run in
	 */
	private void runCommand(World world, RitualBlockEntity blockEntity, BlockPos blockPos, String phase){
		MinecraftServer minecraftServer = world.getServer();
		for (CommandType commandType : blockEntity.ritualRecipe.command) {
			if (commandType.type.equals(phase)) {
				runCommand(minecraftServer, blockPos, commandType.command);
			}
		}
	}

	//TODO test this
	private void runCommand(MinecraftServer minecraftServer, BlockPos blockPos, String command) {
		if (minecraftServer != null && !command.isEmpty()) {
			String posString = blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ();
			String parsedCommand = command.replaceAll("\\{pos}", posString);
			System.out.println("Parsed: " + parsedCommand);
			ServerCommandSource commandSource = minecraftServer.getCommandSource();
			CommandManager commandManager = minecraftServer.getCommandManager();
			ParseResults<ServerCommandSource> parseResults = commandManager.getDispatcher().parse(parsedCommand, commandSource);
			commandManager.execute(parseResults, parsedCommand);
		}
	}
}
