package dev.sterner.book_of_the_dead.mixin;

import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.player.PlayerDataComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TrackTargetGoal.class)
public class TrackTargetGoalMixin {


	@Shadow
	@Nullable
	protected LivingEntity target;

	@Shadow
	@Final
	protected MobEntity mob;

	@Inject(method = "getFollowRange", at = @At("HEAD"), cancellable = true)
	private void book_of_the_dead$increaseDetectionRange(CallbackInfoReturnable<Double> cir) {
		if (this.target instanceof PlayerEntity player && this.mob instanceof HostileEntity && !this.mob.isUndead()) {
			PlayerDataComponent component = BotDComponents.PLAYER_COMPONENT.get(player);
			cir.setReturnValue(cir.getReturnValueD() * (double) component.getAggressionDebuffModifier());
		}
	}
}
