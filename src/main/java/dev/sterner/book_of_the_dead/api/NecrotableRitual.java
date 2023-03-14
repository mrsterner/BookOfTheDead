package dev.sterner.book_of_the_dead.api;

import com.google.common.collect.Lists;
import com.mojang.brigadier.ParseResults;
import dev.sterner.book_of_the_dead.api.interfaces.IRitual;
import dev.sterner.book_of_the_dead.common.block.entity.PedestalBlockEntity;
import dev.sterner.book_of_the_dead.common.block.entity.RitualBlockEntity;
import dev.sterner.book_of_the_dead.common.recipe.RitualRecipe;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.registry.BotDSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
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
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;


public class NecrotableRitual implements IRitual {
	private final Identifier id;
	public BlockPos ritualCenter = null;
	public RitualRecipe recipe;
	public World world = null;
	public int ticker = 0;
	public UUID user = null;
	public int height = 0;
	private int index = 0;
	private boolean canEndRitual = false;

	public NecrotableRitual(Identifier id) {
		this.id = id;
	}

	//Called methods from RitualBlockEntity

	@Override
	public void onStart(World world, BlockPos blockPos, RitualBlockEntity blockEntity){
		if(this.world == null){
			this.world = world;
		}

		if(world.getBlockState(blockPos).isOf(BotDObjects.NECRO_TABLE)){
			ritualCenter = blockPos.add(0.5,0.5,0.5);
			this.runCommand(blockEntity, blockPos, "start");
		}
	}

	@Override
	public void tick(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		ticker++;
		canEndRitual = this.consumeItems(world, blockPos, blockEntity) && this.consumeSacrifices(world, blockPos, blockEntity);
		if(canEndRitual){
			this.runCommand(blockEntity, blockPos, "tick");
			this.generateStatusEffects(world, blockPos, blockEntity);
		}
	}

	@Override
	public void onStopped(World world, BlockPos blockPos, RitualBlockEntity blockEntity){
		ticker = 0;
		index = 0;
		if(canEndRitual){
			this.runCommand(blockEntity, blockPos, "end");
			this.summonSummons(world, blockPos, blockEntity);
			this.summonItems(world, blockPos, blockEntity);
		}
	}

	//RITUAL MANAGER

	private void summonItems(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		double x = blockPos.getX() + 0.5;
		double y = blockPos.getY() + 0.5;
		double z = blockPos.getZ() + 0.5;

		if(world.getBlockEntity(blockPos) instanceof RitualBlockEntity ritualBlockEntity){
			if(ritualBlockEntity.currentNecrotableRitual != null && recipe != null){
				if(world instanceof ServerWorld serverWorld){
					serverWorld.playSound(null, x, y, z, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1F,1F);
				}
				if (recipe.outputs != null) {
					for(ItemStack output : recipe.outputs){
						ItemScatterer.spawn(world, x, y, z, output);
					}
				}
			}
		}
	}

	private void summonSummons(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		if(blockEntity.ritualRecipe.summons != null){
			for(EntityType<?> entityType : blockEntity.ritualRecipe.summons){
				var entity = entityType.create(world);
				if(entity instanceof LivingEntity livingEntity){
					Vec3d worldPos = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
					livingEntity.refreshPositionAndAngles(new BlockPos(worldPos), world.random.nextFloat(), world.random.nextFloat());
					world.spawnEntity(livingEntity);
				}
			}
		}
	}

	private void generateStatusEffects(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		if(user != null){
			PlayerEntity player = world.getPlayerByUuid(user);
			if(player != null && recipe.statusEffectInstance != null){
				for(StatusEffectInstance instance : recipe.statusEffectInstance){
					player.addStatusEffect(instance);
				}
			}
		}
	}

	public Identifier getId() {
		return id;
	}

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

		return index == pedestalToActivate.size();
	}

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
					foundEntity.damage(DamageSource.MAGIC, Integer.MAX_VALUE);
				}
			}
			return true;
		}
		return false;
	}

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

	private void generateFX(ServerWorld serverWorld, double x, double y, double z) {
		if(ticker % 5 == 0 && ticker < recipe.getDuration() - 40){
			serverWorld.playSound(null, x,y,z, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 10,0.5f);
		}
		if (recipe.outputs != null) {
			for(ItemStack output : recipe.outputs){
				for (int i = 0; i < recipe.outputs.size() * 2; i++) {
					serverWorld.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, output),
							x + ((world.random.nextDouble() / 2) - 0.25),
							y + ((world.random.nextDouble() / 2) - 0.25),
							z + ((world.random.nextDouble() / 2) - 0.25),
							0,
							1 * ((world.random.nextDouble() / 2) - 0.25),
							1 * ((world.random.nextDouble() / 2) - 0.25),
							1 * ((world.random.nextDouble() / 2) - 0.25),
							0);
				}
			}
		}
	}

	public void runCommand(RitualBlockEntity blockEntity, BlockPos blockPos, String phase){
		MinecraftServer minecraftServer = world.getServer();
		for (CommandType commandType : blockEntity.ritualRecipe.command) {
			if (commandType.type.equals(phase)) {
				runCommand(minecraftServer, blockPos, commandType.command);
			}
		}
	}

	//TODO test this
	public void runCommand(MinecraftServer minecraftServer, BlockPos blockPos, String command) {
		if (minecraftServer != null && !command.isEmpty()) {
			String posString = blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ();
			String parsedCommand = command.replaceAll("\\{pos}", posString);
			ServerCommandSource commandSource = minecraftServer.getCommandSource();
			CommandManager commandManager = minecraftServer.getCommandManager();
			ParseResults<ServerCommandSource> parseResults = commandManager.getDispatcher().parse(parsedCommand, commandSource);
			commandManager.execute(parseResults, parsedCommand);
		}
	}
}
