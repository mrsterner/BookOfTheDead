package dev.sterner.legemeton;

import dev.sterner.legemeton.client.model.BagEntityModel;
import dev.sterner.legemeton.client.model.JarEntityModel;
import dev.sterner.legemeton.client.renderer.block.HookBlockEntityRenderer;
import dev.sterner.legemeton.client.renderer.block.JarBlockEntityRenderer;
import dev.sterner.legemeton.client.renderer.entity.CorpseEntityRenderer;
import dev.sterner.legemeton.common.registry.*;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
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


		BlockRenderLayerMap.put(RenderLayer.getCutout(), LegemetonObjects.ROPE, LegemetonObjects.HOOK_BLOCK, LegemetonObjects.JAR);
		BlockEntityRendererRegistry.register(LegemetonBlockEntityTypes.HOOK, HookBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(LegemetonBlockEntityTypes.JAR, ctx -> new JarBlockEntityRenderer());
		BuiltinItemRendererRegistry.INSTANCE.register(LegemetonObjects.JAR, new JarBlockEntityRenderer());
		EntityRendererRegistry.register(LegemetonEntityTypes.CORPSE_ENTITY, CorpseEntityRenderer::new);

		EntityModelLayerRegistry.registerModelLayer(BagEntityModel.LAYER_LOCATION, BagEntityModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(JarEntityModel.LAYER_LOCATION, JarEntityModel::createBodyLayer);

		LegemetonSpriteIdentifiers.INSTANCE.addIdentifier(LegemetonSpriteIdentifiers.BLOOD);
	}
}
