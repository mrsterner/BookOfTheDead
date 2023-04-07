package dev.sterner.book_of_the_dead.client.registry;

import dev.sterner.book_of_the_dead.client.renderer.block.BotDSkullBlockEntityRenderer;
import dev.sterner.book_of_the_dead.client.renderer.block.JarBlockEntityRenderer;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;

public interface BotDItemRenderers {

	static void init() {
		BuiltinItemRendererRegistry.INSTANCE.register(BotDObjects.JAR, new JarBlockEntityRenderer());
		BuiltinItemRendererRegistry.INSTANCE.register(BotDObjects.VILLAGER_HEAD, new BotDSkullBlockEntityRenderer());
	}
}
