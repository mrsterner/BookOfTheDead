package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.registry.Holder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.BiomePlacementModifier;
import net.minecraft.world.gen.decorator.HeightRangePlacementModifier;
import net.minecraft.world.gen.decorator.InSquarePlacementModifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.quiltmc.qsl.registry.api.event.DynamicRegistryManagerSetupContext;
import org.quiltmc.qsl.worldgen.biome.api.BiomeModifications;
import org.quiltmc.qsl.worldgen.biome.api.BiomeSelectors;
import org.quiltmc.qsl.worldgen.biome.api.ModificationPhase;

import java.util.List;

import static dev.sterner.book_of_the_dead.common.registry.BotDConfiguredFeatureRegistry.CONFIGURED_FEATURE_KEYS;
import static dev.sterner.book_of_the_dead.common.registry.BotDConfiguredFeatureRegistry.CONFIGURED_SULFUR_FEATURE;


public interface BotDPlacedFeatureRegistry {

	RegistryKey<PlacedFeature> SULFUR_FEATURE = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Constants.id("sulfur_feature"));

	static void init(Registry<ConfiguredFeature<?, ?>> configured, DynamicRegistryManagerSetupContext.RegistryMap registryMap) {
		Holder<ConfiguredFeature<?, ?>> sulfur = configured.getHolder(CONFIGURED_FEATURE_KEYS.get(CONFIGURED_SULFUR_FEATURE)).orElseThrow();
		registryMap.register(
			RegistryKeys.PLACED_FEATURE,
			Constants.id("placed_sulfur_feature"),
			new PlacedFeature(sulfur,
				List.copyOf(List.of(
					InSquarePlacementModifier.getInstance(),
					HeightRangePlacementModifier.trapezoid(YOffset.fixed(-56), YOffset.fixed(24)),
					BiomePlacementModifier.getInstance()
				)))
		);


		BiomeModifications.create(Constants.id("worldgen")).add(ModificationPhase.ADDITIONS, (b) -> true, context -> context.getGenerationSettings().addFeature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, BotDPlacedFeatureRegistry.SULFUR_FEATURE));
	}

	static void init() {
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.LOCAL_MODIFICATIONS, SULFUR_FEATURE);
	}
}
