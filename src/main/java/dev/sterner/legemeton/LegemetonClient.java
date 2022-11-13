package dev.sterner.legemeton;

import dev.sterner.legemeton.client.renderer.CorpseEntityRenderer;
import dev.sterner.legemeton.common.registry.LegemetonEntityTypes;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class LegemetonClient implements ClientModInitializer {


	@Override
	public void onInitializeClient(ModContainer mod) {
		EntityRendererRegistry.register(LegemetonEntityTypes.CORPSE_ENTITY, CorpseEntityRenderer::new);
	}
}
