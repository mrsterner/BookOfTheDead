package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.common.world.feature.SulfurFeature;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public interface BotDFeatureRegistry {

	Feature<DefaultFeatureConfig> SULFUR_FEATURE = register("sulfur_feature", new SulfurFeature());

	static <C extends FeatureConfig, F extends Feature<C>> F register(String id, F feature) {
		return Registry.register(Registries.FEATURE, id, feature);
	}

	static void init(){

	}
}
