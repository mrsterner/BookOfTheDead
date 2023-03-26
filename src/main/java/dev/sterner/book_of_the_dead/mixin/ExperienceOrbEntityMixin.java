package dev.sterner.book_of_the_dead.mixin;

import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.PlayerDataComponent;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrbEntity.class)
public class ExperienceOrbEntityMixin {

	@Unique
	private static final ThreadLocal<PlayerEntity> book_of_the_dead$PLAYER_ENTITY_THREAD_LOCAL = new ThreadLocal<>();

	@Inject(method = "onPlayerCollision", at = @At("HEAD"))
	private void book_of_the_dead$onPlayerCollision(PlayerEntity player, CallbackInfo ci) {
		book_of_the_dead$PLAYER_ENTITY_THREAD_LOCAL.set(player);
	}

	@ModifyArg(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addExperience(I)V"))
	private int book_of_the_dead$debuffExperience(int experience) {
		PlayerDataComponent component = BotDComponents.PLAYER_COMPONENT.get(book_of_the_dead$PLAYER_ENTITY_THREAD_LOCAL.get());
		return (int) (component.getExperienceDebuffModifier() * experience);
	}
}
