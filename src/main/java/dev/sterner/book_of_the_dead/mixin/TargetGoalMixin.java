package dev.sterner.book_of_the_dead.mixin;

import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.PlayerDataComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TargetGoal.class)
public abstract class TargetGoalMixin extends TrackTargetGoal {

	@Shadow protected LivingEntity targetEntity;

	public TargetGoalMixin(MobEntity mob, boolean checkVisibility) {
		super(mob, checkVisibility);
	}

	@Inject(method = "start", at = @At("HEAD"), cancellable = true)
	private void book_of_the_dead$decreaseDetectionRange(CallbackInfo ci){
		if(targetEntity instanceof PlayerEntity player){
			PlayerDataComponent component = BotDComponents.PLAYER_COMPONENT.get(player);
			double range = getFollowRange() * component.getUndeadAggressionBuffModifier();
			boolean bl = this.mob.squaredDistanceTo(player) > range * range;
			if (bl){
				this.stop();
				ci.cancel();
			}
		}

	}
}
