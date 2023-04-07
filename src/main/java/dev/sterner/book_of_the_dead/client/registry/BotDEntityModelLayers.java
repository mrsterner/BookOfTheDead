package dev.sterner.book_of_the_dead.client.registry;

import dev.sterner.book_of_the_dead.client.model.*;
import dev.sterner.book_of_the_dead.client.renderer.block.BrainBlockEntityRenderer;
import dev.sterner.book_of_the_dead.client.renderer.block.ButcherTableBlockEntityRenderer;
import dev.sterner.book_of_the_dead.client.renderer.block.NecroTableBlockEntityRenderer;
import dev.sterner.book_of_the_dead.client.renderer.block.RetortFlaskBlockEntityRenderer;
import dev.sterner.book_of_the_dead.client.renderer.entity.BloodParticleEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;

public interface BotDEntityModelLayers {

	static void init() {
		EntityModelLayerRegistry.registerModelLayer(BagEntityModel.LAYER_LOCATION, BagEntityModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(JarEntityModel.LAYER_LOCATION, JarEntityModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(NecroTableBlockEntityRenderer.LAYER_LOCATION, NecroTableBlockEntityRenderer::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(ButcherTableBlockEntityRenderer.LAYER_LOCATION, ButcherTableBlockEntityRenderer::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(OldManEntityModel.LAYER_LOCATION, OldManEntityModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(BloodSlimeEntityModel.LAYER_LOCATION, BloodSlimeEntityModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(BloodSlimeEntityModel.OUTER_LAYER_LOCATION, BloodSlimeEntityModel::createOverylayBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(BrainEntityModel.LAYER_LOCATION, BrainEntityModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(RetortFlaskBlockEntityRenderer.LAYER_LOCATION, RetortFlaskBlockEntityRenderer::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(BotDSkullBlockEntityModel.LAYER_LOCATION, BotDSkullBlockEntityModel::getSkullTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(BrainBlockEntityRenderer.LAYER_LOCATION, BrainBlockEntityRenderer::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(KakuzuEntityModel.LAYER_LOCATION, KakuzuEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(KakuzuLivingEntityModel.LAYER_LOCATION, KakuzuLivingEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(CircleEntityModel.LAYER_LOCATION, CircleEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(BloodParticleEntityRenderer.LAYER_LOCATION, BloodParticleEntityRenderer::getTexturedModelData);
	}
}
