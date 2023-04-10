package dev.sterner.book_of_the_dead.mixin.reach;

import dev.sterner.book_of_the_dead.common.registry.BotDEntityAttributeRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.resource.SynchronousResourceReloader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GameRenderer.class)
abstract class GameRendererMixin implements SynchronousResourceReloader {
	@Shadow
	@Final
	private MinecraftClient client;


	@ModifyConstant(method = "updateTargetedEntity(F)V", constant = @Constant(doubleValue = 3.0))
	private double book_of_the_dead$getActualAttackRange0(final double attackRange) {
		if (this.client.player != null) {
			return BotDEntityAttributeRegistry.getAttackRange(this.client.player, attackRange);
		}
		return attackRange;
	}

	@ModifyConstant(method = "updateTargetedEntity(F)V", constant = @Constant(doubleValue = 9.0))
	private double book_of_the_dead$getActualAttackRange1(final double attackRange) {
		if (this.client.player != null) {
			return BotDEntityAttributeRegistry.getSquaredAttackRange(this.client.player, attackRange);
		}
		return attackRange;
	}
}
