package dev.sterner.book_of_the_dead.common.rituals;

import com.google.common.collect.Lists;
import com.mojang.brigadier.ParseResults;
import dev.sterner.book_of_the_dead.api.CommandType;
import dev.sterner.book_of_the_dead.api.PedestalInfo;
import dev.sterner.book_of_the_dead.api.interfaces.IRitual;
import dev.sterner.book_of_the_dead.api.ritual.RitualManager;
import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import dev.sterner.book_of_the_dead.common.block.entity.PedestalBlockEntity;
import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.LivingEntityDataComponent;
import dev.sterner.book_of_the_dead.common.item.ContractItem;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
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
	public final RitualManager ritualManager = new RitualManager();

	public BasicNecrotableRitual(Identifier id) {
		this.id = id;
	}

	public Identifier getId() {
		return id;
	}

	@Override
	public void tick(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
		if (ritualManager.userUuid == null) {
			PlayerEntity player = world.getClosestPlayer(blockPos.getZ(), blockPos.getY(), blockPos.getZ(), 16D, true);
			if (player != null) {
				ritualManager.userUuid = player.getUuid();
			}
		}

		boolean sacrificesConsumed = ritualManager.consumeSacrifices(world, blockPos, blockEntity);
		boolean itemsConsumed = ritualManager.consumeItems(world, blockPos, blockEntity);
		if ((sacrificesConsumed && itemsConsumed) || ritualManager.lockTick) {
			if (!ritualManager.lockTick) {
				ritualManager.runCommand(world, blockEntity, blockPos, "start");
				ritualManager.generateStatusEffects(world, blockPos, blockEntity);
			}
			ritualManager.lockTick = true;
			ritualManager.runCommand(world, blockEntity, blockPos, "tick");
		}
	}

	@Override
	public void onStopped(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
		if (ritualManager.lockTick) {
			ritualManager.runCommand(world, blockEntity, blockPos, "end");
			ritualManager.summonSummons(world, blockPos, blockEntity);
			ritualManager.summonItems(world, blockPos, blockEntity);
			this.reset();
		}
	}

	@Override
	public void reset() {
		ritualManager.lockTick = false;
		ritualManager.canCollectPedestals = true;
		ritualManager.canCollectSacrifices = true;
	}
}

