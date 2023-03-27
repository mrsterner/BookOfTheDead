package dev.sterner.book_of_the_dead.common.statuseffect;

import dev.sterner.book_of_the_dead.client.particle.ItemStackBeamParticleEffect;
import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.LivingEntityDataComponent;
import dev.sterner.book_of_the_dead.common.registry.BotDDamageTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDParticleTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SoulSiphonStatusEffect extends StatusEffect {
	public SoulSiphonStatusEffect(StatusEffectType type) {
		super(type, 0x91db69);
	}

	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		entity.damage(BotDDamageTypes.getDamageSource(entity.world, BotDDamageTypes.SACRIFICE), Integer.MAX_VALUE);
		super.onRemoved(entity, attributes, amplifier);
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		super.applyUpdateEffect(entity, amplifier);
		generateFX(entity.world, entity);

	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}

	public void generateFX(World world, LivingEntity living) {
		LivingEntityDataComponent component = BotDComponents.LIVING_COMPONENT.get(living);
		if (component.getRitualPos() != null) {
			Vec3d b = component.getRitualPos().subtract(new Vec3d(living.getX(), living.getY(), living.getZ()).add(0.5, 1.5, 0.5));
			Vec3d directionVector = new Vec3d(b.getX(), b.getY(), b.getZ());

			double x = living.getX() + (world.random.nextDouble() * 0.2D) + 0.4D;
			double y = living.getY() + (world.random.nextDouble() * 0.2D) + 1.2D;
			double z = living.getZ() + (world.random.nextDouble() * 0.2D) + 0.4D;
			if (world instanceof ServerWorld serverWorld) {
				serverWorld.spawnParticles(new ItemStackBeamParticleEffect(
						BotDParticleTypes.ITEM_BEAM_PARTICLE, Items.SLIME_BALL.getDefaultStack(),
						10), x, y, z, 0, directionVector.x, directionVector.y, directionVector.z, 0.10D);
			}
		}

	}
}
