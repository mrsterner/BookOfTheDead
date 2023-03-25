package dev.sterner.book_of_the_dead;

import dev.sterner.book_of_the_dead.common.event.BotDEvents;
import dev.sterner.book_of_the_dead.common.event.BotDItemGroupEvents;
import dev.sterner.book_of_the_dead.common.event.BotDUseEvents;
import dev.sterner.book_of_the_dead.common.registry.*;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
public class BotD implements ModInitializer {

	private static final boolean DEBUG_MODE = true;

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

		BotDUseEvents.init();
		BotDEvents.init();
		BotDItemGroupEvents.init();
	}
}
