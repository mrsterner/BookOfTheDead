package dev.sterner.legemeton.mixin;

import dev.sterner.legemeton.api.event.OnEntityDeathEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(method = "onDeath", at = @At("HEAD"))
	private void shuck$PreOnDeath(DamageSource source, CallbackInfo ci){
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		OnEntityDeathEvent.START.invoker().start(livingEntity, livingEntity.getBlockPos(), source);
	}

	@Inject(method = "onDeath", at = @At("TAIL"))
	private void shuck$postOnDeath(DamageSource source, CallbackInfo ci){
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		OnEntityDeathEvent.END.invoker().end(livingEntity, livingEntity.getBlockPos(), source);
	}
}
