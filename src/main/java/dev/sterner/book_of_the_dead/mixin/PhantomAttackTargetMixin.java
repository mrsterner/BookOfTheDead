package dev.sterner.book_of_the_dead.mixin;

import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.PlayerDataComponent;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@Mixin(PhantomEntity.FindTargetGoal.class)
public class PhantomAttackTargetMixin {

	@Inject(method = "canStart", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/PhantomEntity;setTarget(Lnet/minecraft/entity/LivingEntity;)V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private void book_of_the_dead$stopPhantomAttack(CallbackInfoReturnable<Boolean> cir, List<PlayerEntity> list, Iterator var2, PlayerEntity playerEntity) {
		PlayerDataComponent component = BotDComponents.PLAYER_COMPONENT.get(playerEntity);
		if (component.getPhantomImmunity()) {
			cir.setReturnValue(false);
		}
	}
}
