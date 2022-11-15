package dev.sterner.legemeton;

import dev.sterner.legemeton.client.model.BagEntityModel;
import dev.sterner.legemeton.client.renderer.block.HookBlockEntityRenderer;
import dev.sterner.legemeton.client.renderer.entity.CorpseEntityRenderer;
import dev.sterner.legemeton.common.registry.LegemetonBlockEntityTypes;
import dev.sterner.legemeton.common.registry.LegemetonEntityTypes;
import dev.sterner.legemeton.common.registry.LegemetonObjects;
import dev.sterner.legemeton.common.registry.LegemetonPrticleTypes;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;

public class LegemetonClient implements ClientModInitializer {


	@Override
	public void onInitializeClient(ModContainer mod) {
		LegemetonPrticleTypes.init();


		BlockRenderLayerMap.put(RenderLayer.getCutout(), LegemetonObjects.ROPE, LegemetonObjects.HOOK_BLOCK);
		BlockEntityRendererRegistry.register(LegemetonBlockEntityTypes.HOOK, HookBlockEntityRenderer::new);

		EntityRendererRegistry.register(LegemetonEntityTypes.CORPSE_ENTITY, CorpseEntityRenderer::new);

		EntityModelLayerRegistry.registerModelLayer(BagEntityModel.LAYER_LOCATION, BagEntityModel::createBodyLayer);
	}
}
