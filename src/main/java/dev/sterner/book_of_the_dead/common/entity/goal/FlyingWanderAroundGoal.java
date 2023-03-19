package dev.sterner.book_of_the_dead.common.entity.goal;

import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class FlyingWanderAroundGoal extends Goal {
	private PathAwareEntity mobEntity;

	public FlyingWanderAroundGoal(PathAwareEntity mobEntity) {
		this.setControls(EnumSet.of(Goal.Control.MOVE));
		this.mobEntity = mobEntity;
	}

	@Override
	public boolean canStart() {
		return mobEntity.getNavigation().isIdle() && mobEntity.getRandom().nextInt(10) == 0;
	}

	@Override
	public boolean shouldContinue() {
		return mobEntity.getNavigation().isFollowingPath();
	}

	@Override
	public void start() {
		Vec3d vec3d = this.getRandomLocation();
		if (vec3d != null) {
			mobEntity.getNavigation().startMovingAlong(mobEntity.getNavigation().findPathTo(BlockPos.fromPosition(vec3d), 1), 1.0);
		}
	}

	@Nullable
	private Vec3d getRandomLocation() {
		Vec3d vec3d2;
		vec3d2 = mobEntity.getRotationVec(0.0F);
		Vec3d vec3d3 = AboveGroundTargeting.find(mobEntity, 8, 7, vec3d2.x, vec3d2.z, (float) (Math.PI / 2), 3, 1);
		return vec3d3 != null ? vec3d3 : NoPenaltySolidTargeting.find(mobEntity, 8, 4, -2, vec3d2.x, vec3d2.z, (float) (Math.PI / 2));
	}
}
