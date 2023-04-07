package dev.sterner.book_of_the_dead;

import dev.sterner.book_of_the_dead.client.event.BotDClientEvents;
import dev.sterner.book_of_the_dead.client.registry.*;
import dev.sterner.book_of_the_dead.common.registry.BotDKeyBindings;
import dev.sterner.book_of_the_dead.common.registry.BotDParticleTypes;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class BotDClient implements ClientModInitializer {

	@Override
	public void onInitializeClient(ModContainer mod) {
		BotDParticleTypes.init();
		BotDKeyBindings.init();
		BotDBlockEntityRenderers.init();
		BotDEntityModelLayers.init();
		BotDEntityRenderers.init();
		BotDClientPackets.init();
		BotDModelRegistry.init();
		BotDColorProviders.init();
		BotDClientEvents.init();
		BotDBlockRenderLayers.init();
		BotDItemRenderers.init();
	}
}
