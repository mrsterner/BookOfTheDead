package dev.sterner.book_of_the_dead;

import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.CorpseDataComponent;
import dev.sterner.book_of_the_dead.common.component.PlayerDataComponent;
import dev.sterner.book_of_the_dead.common.event.BotDEvents;
import dev.sterner.book_of_the_dead.common.event.BotDItemGroupEvents;
import dev.sterner.book_of_the_dead.common.event.BotDUseEvents;
import dev.sterner.book_of_the_dead.common.registry.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.base.api.util.TriState;
import org.quiltmc.qsl.entity.effect.api.StatusEffectEvents;
import org.quiltmc.qsl.entity.effect.api.StatusEffectRemovalReason;
import org.quiltmc.qsl.entity.event.api.EntityReviveEvents;
import org.quiltmc.qsl.entity.event.api.ServerPlayerEntityCopyCallback;
import org.quiltmc.qsl.registry.api.event.RegistryEvents;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

import java.util.Optional;
import java.util.Set;

public class BotD implements ModInitializer {

	static final boolean DEBUG_MODE = true;

	public static boolean isDebugMode() {
		return DEBUG_MODE && QuiltLoader.isDevelopmentEnvironment();
	}

	@Override
	public void onInitialize(ModContainer mod) {
		BotDObjects.init();
		BotDEntityTypes.init();
		BotDBlockEntityTypes.init();
		BotDEnchantments.init();
		BotDStatusEffects.init();
		BotDRecipeTypes.init();
		BotDWorldGenerators.init();
		BotDTrades.init();
		BotDRituals.init();
		BotDSoundEvents.init();
		BotDMemoryModuleTypes.init();
		BotDSensorTypes.init();
		BotDDamageTypes.init();
		BotDFeatureRegistry.init();
		BotDPlacedFeatureRegistry.init();

		RegistryEvents.DYNAMIC_REGISTRY_SETUP.register((ctx) -> {
			ctx.withRegistries(registries -> {
				Registry<ConfiguredFeature<?, ?>> configured = registries.get(RegistryKeys.CONFIGURED_FEATURE);
				BotDConfiguredFeatureRegistry.init(configured);
				BotDPlacedFeatureRegistry.init(configured, registries);
			}, Set.of(RegistryKeys.PLACED_FEATURE, RegistryKeys.CONFIGURED_FEATURE));
		});

		BotDUseEvents.init();
		BotDEvents.init();
		BotDItemGroupEvents.init();
	}
}
