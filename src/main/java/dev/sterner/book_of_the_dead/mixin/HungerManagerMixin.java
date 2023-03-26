package dev.sterner.book_of_the_dead.mixin;

import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.PlayerDataComponent;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public class HungerManagerMixin {

	@Unique
	private static final ThreadLocal<PlayerEntity> book_of_the_dead$PLAYER_ENTITY_THREAD_LOCAL = new ThreadLocal<>();

	@Inject(method = "update", at = @At("HEAD"))
	private void book_of_the_dead$update(PlayerEntity player, CallbackInfo ci) {
		book_of_the_dead$PLAYER_ENTITY_THREAD_LOCAL.set(player);
	}

	@ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(FF)F"), index = 1)
	private float book_of_the_dead$debuffSaturation(float in) {
		PlayerDataComponent component = BotDComponents.PLAYER_COMPONENT.get(book_of_the_dead$PLAYER_ENTITY_THREAD_LOCAL.get());
		return in * component.getSaturationDebuffModifier();
	}
}
