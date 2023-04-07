package dev.sterner.book_of_the_dead.client.registry;

import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.minecraft.client.render.RenderLayer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;

public interface BotDBlockRenderLayers {

	static void init() {
		BlockRenderLayerMap.put(RenderLayer.getCutout(),
			BotDObjects.ROPE,
			BotDObjects.HOOK_BLOCK,
			BotDObjects.JAR,
			BotDObjects.NECRO_TABLE,
			BotDObjects.BUTCHER_TABLE,
			BotDObjects.REINFORCED_DOOR,
			BotDObjects.POPPY_CROP,
			BotDObjects.PEDESTAL,
			BotDObjects.RETORT_FLASK_BLOCK
		);
	}
}
