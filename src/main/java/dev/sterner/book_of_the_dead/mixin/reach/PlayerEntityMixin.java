package dev.sterner.book_of_the_dead.mixin.reach;

import dev.sterner.book_of_the_dead.common.registry.BotDEntityAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	PlayerEntityMixin(final EntityType<? extends LivingEntity> type, final World world) {
		super(type, world);
	}
	@ModifyConstant(method = "attack(Lnet/minecraft/entity/Entity;)V", constant = @Constant(doubleValue = 9.0))
	private double book_of_the_dead$getActualAttackRange(final double attackRange) {
		return BotDEntityAttributeRegistry.getSquaredAttackRange(this, attackRange);
	}
}
