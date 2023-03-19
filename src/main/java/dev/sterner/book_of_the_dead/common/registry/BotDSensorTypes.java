package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.common.entity.brain.sensor.KakuzuSpecificSensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.tslat.smartbrainlib.SBLConstants;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.function.Supplier;

public interface BotDSensorTypes {
	Supplier<SensorType<KakuzuSpecificSensor>> KAKUZU = register("kakuzu", KakuzuSpecificSensor::new);

	private static <T extends ExtendedSensor<?>> Supplier<SensorType<T>> register(String id, Supplier<T> sensor) {
		return SBLConstants.SBL_LOADER.registerSensorType(id, sensor);
	}

	static void init() {

	}
}
