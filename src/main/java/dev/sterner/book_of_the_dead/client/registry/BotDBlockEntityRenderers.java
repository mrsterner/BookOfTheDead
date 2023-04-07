package dev.sterner.book_of_the_dead.client.registry;

import dev.sterner.book_of_the_dead.client.renderer.block.*;
import dev.sterner.book_of_the_dead.common.registry.BotDBlockEntityTypes;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public interface BotDBlockEntityRenderers {

	static void init() {
		BlockEntityRendererFactories.register(BotDBlockEntityTypes.HOOK, HookBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(BotDBlockEntityTypes.JAR, ctx -> new JarBlockEntityRenderer());
		BlockEntityRendererFactories.register(BotDBlockEntityTypes.NECRO, NecroTableBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(BotDBlockEntityTypes.BUTCHER, ButcherTableBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(BotDBlockEntityTypes.PEDESTAL, PedestalBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(BotDBlockEntityTypes.RETORT, RetortFlaskBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(BotDBlockEntityTypes.HEAD, ctx -> new BotDSkullBlockEntityRenderer());
		BlockEntityRendererFactories.register(BotDBlockEntityTypes.BRAIN, BrainBlockEntityRenderer::new);
	}
}
