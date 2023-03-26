package dev.sterner.book_of_the_dead.common.entity.brain.task;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Vec3d;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class StrollFlyTask<E extends PathAwareEntity> extends ExtendedBehaviour<E> {
	protected BiFunction<E, Vec3d, Float> speedModifier = (entity, targetPos) -> 1.0F;
	protected Predicate<E> avoidWaterPredicate = (entity) -> true;
	protected BiPredicate<E, Vec3d> positionPredicate = (entity, pos) -> true;

	public StrollFlyTask() {
	}

	protected List<Pair<MemoryModuleType<?>, MemoryModuleState>> getMemoryRequirements() {
		return ObjectArrayList.of(Pair.of(MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT));
	}

	public StrollFlyTask<E> dontAvoidWater() {
		return this.avoidWaterWhen((entity) -> false);
	}

	public StrollFlyTask<E> avoidWaterWhen(Predicate<E> predicate) {
		this.avoidWaterPredicate = predicate;
		return this;
	}

	protected void start(E entity) {
		Vec3d targetPos = getTargetFlyPos(entity, 10, 7);
		if (!this.positionPredicate.test(entity, targetPos)) {
			targetPos = null;
		}

		if (targetPos == null) {
			BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
		} else {
			BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(targetPos, this.speedModifier.apply(entity, targetPos), 0));
		}
	}


	@Nullable
	private static Vec3d getTargetFlyPos(PathAwareEntity pathAwareEntity, int i, int j) {
		Vec3d vec3d = pathAwareEntity.getRotationVec(0.0F);
		return NoPenaltySolidTargeting.find(pathAwareEntity, i, j, -2, vec3d.x, vec3d.z, (float) (Math.PI / 2));
	}
}
