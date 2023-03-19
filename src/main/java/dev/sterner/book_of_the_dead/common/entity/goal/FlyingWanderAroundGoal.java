package dev.sterner.book_of_the_dead.common.entity.goal;

import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class FlyingWanderAroundGoal extends WanderAroundFarGoal {

	public FlyingWanderAroundGoal(PathAwareEntity pathAwareEntity) {
		super(pathAwareEntity, 1.0D);
		this.setControls(EnumSet.of(Control.MOVE));
	}

	@Override
	public boolean canStart() {
		return this.mob.getNavigation() == null && super.canStart();
	}

	@Nullable
	@Override
	protected Vec3d getWanderTarget() {
		return getFlightTarget();
	}

	@Nullable
	protected Vec3d getFlightTarget() {
		Vec3d vec3d = this.mob.getRotationVec(0.0f);
		Vec3d vec3d2 = AboveGroundTargeting.find(this.mob, 12, 5, vec3d.x, vec3d.z, 1.5707963705062866F, 12, 8);
		if (vec3d2 != null) {
			return vec3d2.add(0, 3, 0);
		}
		return NoPenaltySolidTargeting.find(this.mob, 12, 4, -2, vec3d.x, vec3d.z, 1.5707963705062866);
	}
}
