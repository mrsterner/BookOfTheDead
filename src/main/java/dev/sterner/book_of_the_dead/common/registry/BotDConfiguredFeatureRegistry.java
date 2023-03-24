package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

import java.util.LinkedHashMap;
import java.util.Map;

public interface BotDConfiguredFeatureRegistry {
	Map<Identifier, ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = new LinkedHashMap<>();
	Map<ConfiguredFeature<?, ?>, RegistryKey<ConfiguredFeature<?, ?>>> CONFIGURED_FEATURE_KEYS = new LinkedHashMap<>();

	ConfiguredFeature<?, ?> CONFIGURED_SULFUR_FEATURE = register("sulfur_feature", new ConfiguredFeature<>(BotDFeatureRegistry.SULFUR_FEATURE, DefaultFeatureConfig.INSTANCE));

	static <C extends FeatureConfig, E extends Feature<C>, F extends ConfiguredFeature<C, E>> F register(String id, F feature) {
		CONFIGURED_FEATURES.put(Constants.id(id), feature);
		return feature;
	}
	static void init(Registry<ConfiguredFeature<?, ?>> configured) {
		CONFIGURED_FEATURES.forEach((id, feature) -> {Registry.register(configured, id, feature);
			CONFIGURED_FEATURE_KEYS.put(feature, configured.getKey(feature).orElseThrow());
		});
	}
}
