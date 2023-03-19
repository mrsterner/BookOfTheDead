package dev.sterner.book_of_the_dead.common.entity.brain.task;

import com.mojang.datafixers.util.Pair;
import dev.sterner.book_of_the_dead.common.entity.KakuzuEntity;
import dev.sterner.book_of_the_dead.common.registry.BotDMemoryModuleTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.BiFunction;

public class EnterPlayerOnDemandTask extends ExtendedBehaviour<KakuzuEntity> {
	protected BiFunction<KakuzuEntity, Vec3d, Float> speedModifier = (entity, targetPos) -> 1.0F;

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryModuleState>> getMemoryRequirements() {
		return ObjectArrayList.of(
				Pair.of(MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT),
				Pair.of(BotDMemoryModuleTypes.OWNER_PLAYER, MemoryModuleState.VALUE_PRESENT),
				Pair.of(BotDMemoryModuleTypes.SHOULD_MERGE, MemoryModuleState.VALUE_PRESENT)
		);
	}

	@Override
	protected boolean shouldRun(ServerWorld world, KakuzuEntity entity) {
		if(BrainUtils.hasMemory(entity, BotDMemoryModuleTypes.OWNER_PLAYER)){
			PlayerEntity player = BrainUtils.getMemory(entity, BotDMemoryModuleTypes.OWNER_PLAYER);
			return entity.squaredDistanceTo(player) < 64 && entity.isEnterOwnerFlag() && super.shouldRun(world, entity);
		}else{
			return false;
		}
	}

	@Override
	protected boolean shouldKeepRunning(ServerWorld world, KakuzuEntity entity, long gameTime) {
		return shouldRun(world, entity) && super.shouldKeepRunning(world, entity, gameTime);
	}

	@Override
	protected void keepRunning(ServerWorld world, KakuzuEntity entity, long gameTime) {
		if(BrainUtils.hasMemory(entity, BotDMemoryModuleTypes.OWNER_PLAYER)) {
			PlayerEntity player = BrainUtils.getMemory(entity, BotDMemoryModuleTypes.OWNER_PLAYER);
			if (player != null) {
				BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(player, this.speedModifier.apply(entity, player.getPos()), 0));
			}
			super.keepRunning(world, entity, gameTime);
		}
	}
}
