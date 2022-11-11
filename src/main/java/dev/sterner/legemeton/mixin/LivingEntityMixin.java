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
	private void shuck$onDeath(DamageSource source, CallbackInfo ci){
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		OnEntityDeathEvent.ON_ENTITY_DEATH.invoker().onDeath(livingEntity, livingEntity.getBlockPos(), source);
	}
}
