package dev.sterner.book_of_the_dead.mixin;

import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.PlayerAbilityData;
import dev.sterner.book_of_the_dead.common.component.PlayerDataComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.sensor.VillagerHostilesSensor;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerHostilesSensor.class)
public class VillagerHostilesSensorMixin {

	@Inject(at = @At("HEAD"), method = "matches", cancellable = true)
	private void book_of_the_dead$matches(LivingEntity entity, LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
		if (target instanceof PlayerEntity player && ((VillagerEntity) entity).getReputation(player) < -100) {
			PlayerDataComponent component = BotDComponents.PLAYER_COMPONENT.get(player);
			if (component.getReputationDebuffModifier() <= PlayerAbilityData.REPUTATION[2]) {
				cir.setReturnValue(true);
			}
		}
	}

	@Inject(method = "isCloseEnoughForDanger", at = @At("HEAD"), cancellable = true)
	private void book_of_the_dead$getNearestHostile(LivingEntity villager, LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
		if (target instanceof PlayerEntity player && ((VillagerEntity) villager).getReputation(player) < -100) {
			PlayerDataComponent component = BotDComponents.PLAYER_COMPONENT.get(player);
			if (component.getReputationDebuffModifier() <= PlayerAbilityData.REPUTATION[2]) {
				cir.setReturnValue(target.squaredDistanceTo(villager) <= 12 * 12);
			}
		}
	}
}
