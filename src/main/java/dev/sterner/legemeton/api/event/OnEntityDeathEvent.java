package dev.sterner.legemeton.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;

import static net.fabricmc.fabric.impl.base.event.EventFactoryImpl.createArrayBacked;

public class OnEntityDeathEvent {

	public static final Event<OnDeath> ON_ENTITY_DEATH = createArrayBacked(OnDeath.class, listeners -> (entity, blockPos, source) -> {
		for (OnDeath listener : listeners) {
			listener.onDeath(entity, blockPos, source);
		}
	});
	@FunctionalInterface
	public interface OnDeath {
		void onDeath(LivingEntity entity, BlockPos blockPos, DamageSource source);
	}
}
