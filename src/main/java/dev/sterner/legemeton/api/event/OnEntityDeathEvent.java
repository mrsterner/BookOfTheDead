package dev.sterner.legemeton.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;

import static net.fabricmc.fabric.impl.base.event.EventFactoryImpl.createArrayBacked;

public class OnEntityDeathEvent {

	public static final Event<Start> START = createArrayBacked(Start.class, listeners -> (entity, blockPos, source) -> {
		for (Start listener : listeners) {
			listener.start(entity, blockPos, source);
		}
	});

	public static final Event<End> END = createArrayBacked(End.class, listeners -> (entity, blockPos, source) -> {
		for (End listener : listeners) {
			listener.end(entity, blockPos, source);
		}
	});

	@FunctionalInterface
	public interface Start {
		void start(LivingEntity entity, BlockPos blockPos, DamageSource source);
	}

	@FunctionalInterface
	public interface End {
		void end(LivingEntity entity, BlockPos blockPos, DamageSource source);
	}
}
