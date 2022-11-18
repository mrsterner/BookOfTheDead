package dev.sterner.legemeton;

import dev.sterner.legemeton.client.model.BagEntityModel;
import dev.sterner.legemeton.client.model.JarEntityModel;
import dev.sterner.legemeton.client.renderer.block.HookBlockEntityRenderer;
import dev.sterner.legemeton.client.renderer.block.JarBlockEntityRenderer;
import dev.sterner.legemeton.client.renderer.block.NecroTableBlockEntityRenderer;
import dev.sterner.legemeton.client.renderer.entity.CorpseEntityRenderer;
import dev.sterner.legemeton.client.renderer.item.AllBlackSwordItemRenderer;
import dev.sterner.legemeton.common.item.AllBlackSwordItem;
import dev.sterner.legemeton.common.registry.*;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

public class LegemetonClient implements ClientModInitializer {


	@Override
	public void onInitializeClient(ModContainer mod) {
		LegemetonPrticleTypes.init();


		BlockRenderLayerMap.put(RenderLayer.getCutout(), LegemetonObjects.ROPE, LegemetonObjects.HOOK_BLOCK, LegemetonObjects.JAR, LegemetonObjects.NECRO_TABLE);
		BlockEntityRendererRegistry.register(LegemetonBlockEntityTypes.HOOK, HookBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(LegemetonBlockEntityTypes.JAR, ctx -> new JarBlockEntityRenderer());
		BlockEntityRendererRegistry.register(LegemetonBlockEntityTypes.NECRO, NecroTableBlockEntityRenderer::new);
		BuiltinItemRendererRegistry.INSTANCE.register(LegemetonObjects.JAR, new JarBlockEntityRenderer());
		EntityRendererRegistry.register(LegemetonEntityTypes.CORPSE_ENTITY, CorpseEntityRenderer::new);

		EntityModelLayerRegistry.registerModelLayer(BagEntityModel.LAYER_LOCATION, BagEntityModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(JarEntityModel.LAYER_LOCATION, JarEntityModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(NecroTableBlockEntityRenderer.LAYER_LOCATION, NecroTableBlockEntityRenderer::createBodyLayer);

		LegemetonSpriteIdentifiers.INSTANCE.addIdentifier(LegemetonSpriteIdentifiers.BLOOD);



		for (Item item : LegemetonObjects.ITEMS.keySet()) {
			if(item instanceof AllBlackSwordItem){
				Identifier allBlackId = Registry.ITEM.getId(item);
				AllBlackSwordItemRenderer allBlackItemRenderer = new AllBlackSwordItemRenderer(allBlackId);
				ResourceLoader.get(ResourceType.CLIENT_RESOURCES).registerReloader(allBlackItemRenderer);
				BuiltinItemRendererRegistry.INSTANCE.register(item, allBlackItemRenderer);
				ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
					out.accept(new ModelIdentifier(allBlackId + "_gui", "inventory"));
					out.accept(new ModelIdentifier(allBlackId + "_handheld", "inventory"));
				});
			}

		}
	}
}
