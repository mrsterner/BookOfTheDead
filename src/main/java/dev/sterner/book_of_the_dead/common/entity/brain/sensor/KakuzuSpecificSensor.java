package dev.sterner.book_of_the_dead.common.entity.brain.sensor;

import dev.sterner.book_of_the_dead.common.entity.KakuzuEntity;
import dev.sterner.book_of_the_dead.common.registry.BotDMemoryModuleTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDSensorTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.List;

public class KakuzuSpecificSensor extends ExtendedSensor<KakuzuEntity> {
	@Override
	public List<MemoryModuleType<?>> memoriesUsed() {
		return ObjectArrayList.of(BotDMemoryModuleTypes.OWNER_PLAYER);
	}

	@Override
	public SensorType<? extends ExtendedSensor<?>> type() {
		return BotDSensorTypes.KAKUZU.get();
	}

	@Override
	protected void sense(ServerWorld world, KakuzuEntity entity) {
		Box box = entity.getBoundingBox().expand(this.getHorizontalExpansion(), this.getHeightExpansion(), this.getHorizontalExpansion());
		List<PlayerEntity> list = world.getEntitiesByClass(PlayerEntity.class, box, LivingEntity::isAlive);
		if (entity.getOwner() != null) {
			PlayerEntity player = world.getPlayerByUuid(entity.getOwner());
			if (player != null && list.contains(player)) {
				Brain<?> brain = entity.getBrain();
				brain.remember(BotDMemoryModuleTypes.OWNER_PLAYER, player);
			}
		}
	}

	protected int getHorizontalExpansion() {
		return 24;
	}

	protected int getHeightExpansion() {
		return 16;
	}
}
