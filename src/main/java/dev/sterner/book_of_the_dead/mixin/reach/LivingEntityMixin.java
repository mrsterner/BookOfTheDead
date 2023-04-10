package dev.sterner.book_of_the_dead.mixin.reach;

import dev.sterner.book_of_the_dead.common.registry.BotDEntityAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {
	LivingEntityMixin(final EntityType<?> type, final World world) {
		super(type, world);
	}

	@Inject(method = "createAttributes", require = 1, allow = 1, at = @At("RETURN"))
	private static void book_of_the_dead$addAttributes(final CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
		info.getReturnValue().add(BotDEntityAttributeRegistry.ATTACK_RANGE);
	}
}
