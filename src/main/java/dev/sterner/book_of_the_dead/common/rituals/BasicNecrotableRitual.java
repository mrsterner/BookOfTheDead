package dev.sterner.book_of_the_dead.common.rituals;

import com.google.common.collect.Lists;
import com.mojang.brigadier.ParseResults;
import dev.sterner.book_of_the_dead.api.CommandType;
import dev.sterner.book_of_the_dead.api.PedestalInfo;
import dev.sterner.book_of_the_dead.api.interfaces.IRitual;
import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import dev.sterner.book_of_the_dead.common.block.entity.PedestalBlockEntity;
import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.LivingEntityDataComponent;
import dev.sterner.book_of_the_dead.common.registry.BotDSoundEvents;
import dev.sterner.book_of_the_dead.common.registry.BotDStatusEffects;
import dev.sterner.book_of_the_dead.common.util.BotDUtils;
import dev.sterner.book_of_the_dead.common.util.ParticleUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
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
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class BasicNecrotableRitual implements IRitual {
	private final Identifier id;
	public int pedestalTicker = 0;
	public int sacrificeTicker = -1;
	public boolean canCollectPedestals = true;
	public boolean canCollectSacrifices = true;
	public int ticker = 0;
	public UUID userUuid = null;
	private boolean lockTick = false;
	public DefaultedList<Integer> contract = DefaultedList.ofSize(8, 0);

	public BasicNecrotableRitual(Identifier id) {
		this.id = id;
	}

	//Called methods from RitualBlockEntity

	@Override
	public void tick(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
		ticker++;
		boolean canEndSacrifices = this.consumeSacrifices(world, blockPos, blockEntity);
		boolean canEndRitual = this.consumeItems(world, blockPos, blockEntity);
		if ((canEndRitual && canEndSacrifices) || lockTick) {
			if (!this.lockTick) {
				this.runCommand(world, blockEntity, blockPos, "start");
				this.generateStatusEffects(world, blockPos, blockEntity);
			}
			this.lockTick = true;
			this.runCommand(world, blockEntity, blockPos, "tick");
		}
	}

	@Override
	public void onStopped(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
		ticker = 0;
		if (lockTick) {
			if (userUuid == null) {
				PlayerEntity player = world.getClosestPlayer(blockPos.getZ(), blockPos.getY(), blockPos.getZ(), 16D, true);
				if (player != null) {
					userUuid = player.getUuid();
				}
			}

			this.runCommand(world, blockEntity, blockPos, "end");
			this.summonSummons(world, blockPos, blockEntity);
			this.summonItems(world, blockPos, blockEntity);
			this.lockTick = false;
			this.canCollectPedestals = true;
			this.canCollectSacrifices = true;
		}
	}

	//RITUAL MANAGER

	/**
	 * Drops all resulting items at ritual origin
	 *
	 * @param world       world
	 * @param blockPos    ritual origin
	 * @param blockEntity ritualBlockEntity
	 */
	private void summonItems(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
		double x = blockPos.getX() + 0.5;
		double y = blockPos.getY() + 0.5;
		double z = blockPos.getZ() + 0.5;

		if (blockEntity.currentBasicNecrotableRitual != null && blockEntity.ritualRecipe != null) {
			if (world instanceof ServerWorld serverWorld) {
				serverWorld.playSound(null, x, y, z, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1F, 1F);
			}
			if (blockEntity.ritualRecipe.outputs() != null) {
				for (ItemStack output : blockEntity.ritualRecipe.outputs()) {
					ItemScatterer.spawn(world, x, y, z, output.copy());
				}
			}
		}
	}

	/**
	 * Spawns all entities which was specified in the ritual recipe at ritual origin
	 *
	 * @param world       world
	 * @param blockPos    ritual origin
	 * @param blockEntity ritualBlockEntity
	 */
	private void summonSummons(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
		if (blockEntity.ritualRecipe.summons() != null) {
			for (EntityType<?> entityType : blockEntity.ritualRecipe.summons()) {
				var entity = entityType.create(world);
				if (entity instanceof LivingEntity livingEntity) {
					Vec3i worldPos = new Vec3i(blockPos.getX(), blockPos.getY(), blockPos.getZ());
					livingEntity.refreshPositionAndAngles(new BlockPos(worldPos), world.random.nextFloat(), world.random.nextFloat());
					world.spawnEntity(livingEntity);
				}
			}
		}
	}

	/**
	 * Adds all statusEffects to the user of the ritual
	 *
	 * @param world       world
	 * @param blockPos    ritual origin
	 * @param blockEntity ritualBlockEntity
	 */
	private void generateStatusEffects(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
		int size = 16;
		List<LivingEntity> livingEntityList = new ArrayList<>();
		boolean foundContract = false;
		if (blockEntity.ritualRecipe.statusEffectInstance() != null) {
			for (Integer id : this.contract) {
				if (id != 0) {
					Entity entity = world.getEntityById(id);
					if (entity instanceof LivingEntity livingEntity) {
						livingEntityList.add(livingEntity);
						foundContract = true;
					}
				}
			}
			if (!foundContract) {
				livingEntityList = world.getEntitiesByClass(LivingEntity.class, new Box(blockPos).expand(size), Entity::isAlive);
			}
			for (LivingEntity living : livingEntityList) {
				for (StatusEffectInstance instance : blockEntity.ritualRecipe.statusEffectInstance()) {
					if (living.canHaveStatusEffect(instance)) {
						living.addStatusEffect(new StatusEffectInstance(instance));
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
	 *
	 * @param world       world
	 * @param blockPos    ritual origin
	 * @param blockEntity ritualBlockEntity
	 * @return true if all items where consumed
	 */
	private boolean consumeItems(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
		if (blockEntity.ritualRecipe == null) {
			return false;
		} else if (blockEntity.ritualRecipe.inputs() != null && blockEntity.ritualRecipe.inputs().isEmpty()) {
			return true;
		}


		if (blockEntity.pedestalToActivate.isEmpty() && canCollectPedestals) {
			List<PedestalInfo> infoStream = blockEntity.getPedestalInfo(world).stream().filter(itemStackBlockPosPair -> !itemStackBlockPosPair.stack().isEmpty()).toList();
			for (PedestalInfo info : infoStream) {
				for (Ingredient ingredient : blockEntity.ritualRecipe.inputs()) {
					if (ingredient.test(info.stack())) {
						BlockPos checkPos = info.pos();
						if (world.getBlockEntity(checkPos) instanceof PedestalBlockEntity) {
							blockEntity.pedestalToActivate.add(info);
						}
					}
				}
			}
		} else if (!blockEntity.pedestalToActivate.isEmpty()) {
			pedestalTicker++;
			BlockPos particlePos = blockEntity.pedestalToActivate.get(0).pos();

			if (world instanceof ServerWorld serverWorld) {
				if(blockEntity.ritualRecipe.outputs() != null){
					ParticleUtils.generateItemParticle(serverWorld, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, blockEntity.ritualRecipe.outputs());
				}
				ParticleUtils.spawnItemParticleBeam(new Vec3d(particlePos.getX(), particlePos.getY(), particlePos.getZ()), new Vec3d(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ()), serverWorld, blockEntity.pedestalToActivate.get(0).stack());
			}

			if (pedestalTicker == 1) {
				world.playSound(null, blockEntity.pedestalToActivate.get(0).pos().getX(), blockEntity.pedestalToActivate.get(0).pos().getY(), blockEntity.pedestalToActivate.get(0).pos().getZ(), BotDSoundEvents.MISC_ITEM_BEAM, SoundCategory.BLOCKS, 0.5f, 0.75f * world.random.nextFloat() / 2);
			}

			if (pedestalTicker > 20 * 4) {
				if (world.getBlockEntity(blockEntity.pedestalToActivate.get(0).pos()) instanceof PedestalBlockEntity pedestalBlockEntity) {
					pedestalBlockEntity.setStack(ItemStack.EMPTY);
					pedestalBlockEntity.markDirty();
				}
				blockEntity.pedestalToActivate.remove(0);
				blockEntity.markDirty();
				canCollectPedestals = false;
				pedestalTicker = 0;
				return blockEntity.pedestalToActivate.isEmpty();
			}
		}

		return false;
	}

	/**
	 * Check and kill sacrifices around the ritual origin depending on recipe
	 *
	 * @param world       world
	 * @param blockPos    origin
	 * @param blockEntity ritualBlockEntity
	 * @return true if sacrifice was successful
	 */
	private boolean consumeSacrifices(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
		if (blockEntity.ritualRecipe.sacrifices() != null && blockEntity.ritualRecipe.sacrifices().isEmpty()) {
			return true;
		}

		int size = 8;

		if(blockEntity.sacrificeCache.isEmpty() && canCollectSacrifices){
			List<LivingEntity> livingEntityList = world.getEntitiesByClass(LivingEntity.class, new Box(blockPos).expand(size), Entity::isAlive);
			List<EntityType<?>> entityTypeList = Lists.newArrayList(livingEntityList.stream().map(Entity::getType).toList());
			List<EntityType<?>> ritualSacrifices = blockEntity.ritualRecipe.sacrifices();

			// Count occurrences of each entity type in both lists
			Map<EntityType<?>, Long> entityTypeCountMap = entityTypeList.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
			Map<EntityType<?>, Long> ritualSacrificesCountMap = ritualSacrifices.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

			// Create a new list to store the missing entity types
			List<EntityType<?>> missingEntityTypes = new ArrayList<>();
			for (EntityType<?> entityType : ritualSacrifices) {
				if (!entityTypeList.contains(entityType)) {
					long entityTypeCount = entityTypeCountMap.getOrDefault(entityType, 0L);
					long ritualSacrificesCount = ritualSacrificesCountMap.getOrDefault(entityType, 0L);
					int copiesToAdd = (int) Math.max(ritualSacrificesCount - entityTypeCount, 0);
					missingEntityTypes.addAll(Collections.nCopies(copiesToAdd, entityType));
				}
			}
			// Add the missing entity types to entityTypeList
			entityTypeList.addAll(missingEntityTypes);

			// Find and add the closest entity for each entity type to sacrificeCache
			for (EntityType<?> entityType : ritualSacrifices) {
				LivingEntity foundEntity = BotDUtils.getClosestEntity(livingEntityList, entityType, blockPos);
				if (foundEntity != null) {
					blockEntity.sacrificeCache.add(foundEntity);
					livingEntityList.remove(foundEntity);
				}
			}
			return false;

		} else if (!blockEntity.sacrificeCache.isEmpty()) {
			sacrificeTicker++;
			if(sacrificeTicker >= 20 * 3){
				if(blockEntity.sacrificeCache.get(0) instanceof MobEntity mob){
					LivingEntityDataComponent livingEntityDataComponent = BotDComponents.LIVING_COMPONENT.get(mob);
					livingEntityDataComponent.setRitualPos(new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
					mob.addStatusEffect(new StatusEffectInstance(BotDStatusEffects.SOUL_SIPHON, 20 * 3));
				}
				canCollectSacrifices = false;
				blockEntity.sacrificeCache.remove(0);
				blockEntity.markDirty();
				sacrificeTicker = 0;
				return blockEntity.sacrificeCache.isEmpty();
			}
		}
		return blockEntity.sacrificeCache.isEmpty();
	}

	/**
	 * Runs a command depending on which key phrase is used, "start", "tick", "end". Runs the {@link BasicNecrotableRitual#runCommand(MinecraftServer, BlockPos, String)}
	 *
	 * @param world       world
	 * @param blockEntity ritualBlockEntity
	 * @param blockPos    pos of the ritual origin
	 * @param phase       keyword for which phase the command should run in
	 */
	private void runCommand(World world, NecroTableBlockEntity blockEntity, BlockPos blockPos, String phase) {
		MinecraftServer minecraftServer = world.getServer();
		for (CommandType commandType : blockEntity.ritualRecipe.command()) {
			if (commandType.type().equals(phase)) {
				runCommand(minecraftServer, blockPos, commandType.command());
			}
		}
	}

	/**
	 * Runs the command with the command manager
	 *
	 * @param minecraftServer server
	 * @param blockPos        ritual center
	 * @param command         command to execute
	 */
	private void runCommand(MinecraftServer minecraftServer, BlockPos blockPos, String command) {
		if (minecraftServer != null && !command.isEmpty()) {
			command = "execute positioned {pos} run " + command;
			String posString = blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ();
			String parsedCommand = command.replaceAll("\\{pos}", posString);
			ServerCommandSource commandSource = minecraftServer.getCommandSource();
			CommandManager commandManager = minecraftServer.getCommandManager();
			ParseResults<ServerCommandSource> parseResults = commandManager.getDispatcher().parse(parsedCommand, commandSource);
			commandManager.execute(parseResults, parsedCommand);
		}
	}
}
