package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.jetbrains.annotations.Nullable;

public interface BotDEntityAttributeRegistry {

	EntityAttribute ATTACK_RANGE = make("attack_range", 0.0, -1024.0, 1024.0);

	static EntityAttribute make(final String name, final double base, final double min, final double max) {
		return new ClampedEntityAttribute("attribute.name.generic." + Constants.MODID + '.' + name, base, min, max).setTracked(true);
	}

	static double getAttackRange(final LivingEntity entity, final double baseAttackRange) {
		@Nullable final var attackRange = entity.getAttributeInstance(ATTACK_RANGE);
		return (attackRange != null) ? (baseAttackRange + attackRange.getValue()) : baseAttackRange;
	}

	static double getSquaredAttackRange(final LivingEntity entity, final double sqBaseAttackRange) {
		final var attackRange = getAttackRange(entity, Math.sqrt(sqBaseAttackRange));
		return attackRange * attackRange;
	}

	static boolean isWithinAttackRange(final PlayerEntity player, final Entity entity) {
		return player.squaredDistanceTo(entity) <= getSquaredAttackRange(player, 64.0);
	}

	static void init() {
		Registry.register(Registries.ENTITY_ATTRIBUTE, Constants.id( "attack_range"), ATTACK_RANGE);
	}
}
