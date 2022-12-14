package dev.sterner.book_of_the_dead;

import dev.sterner.book_of_the_dead.client.model.*;
import dev.sterner.book_of_the_dead.client.renderer.block.*;
import dev.sterner.book_of_the_dead.client.renderer.entity.BloodSlimeEntityRenderer;
import dev.sterner.book_of_the_dead.client.renderer.entity.CorpseEntityRenderer;
import dev.sterner.book_of_the_dead.client.renderer.entity.OldManEntityRenderer;
import dev.sterner.book_of_the_dead.client.renderer.item.AllBlackSwordItemRenderer;
import dev.sterner.book_of_the_dead.common.item.AllBlackSwordItem;
import dev.sterner.book_of_the_dead.common.registry.*;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

public class BotDClient implements ClientModInitializer {


	@Override
	public void onInitializeClient(ModContainer mod) {
		BotDParticleTypes.init();



		BlockRenderLayerMap.put(RenderLayer.getCutout(),
				BotDObjects.ROPE,
				BotDObjects.HOOK_BLOCK,
				BotDObjects.JAR,
				BotDObjects.NECRO_TABLE,
				BotDObjects.BUTCHER_TABLE,
				BotDObjects.REINFORCED_DOOR,
				BotDObjects.POPPY_CROP,
				BotDObjects.PEDESTAL,
				BotDObjects.RITUAL,
				BotDObjects.RETORT_FLASK_BLOCK
		);

		BlockEntityRendererFactories.register(BotDBlockEntityTypes.HOOK, HookBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(BotDBlockEntityTypes.JAR, ctx -> new JarBlockEntityRenderer());
		BlockEntityRendererFactories.register(BotDBlockEntityTypes.NECRO, NecroTableBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(BotDBlockEntityTypes.BUTCHER, ButcherTableBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(BotDBlockEntityTypes.PEDESTAL, PedestalBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(BotDBlockEntityTypes.RITUAL, RitualBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(BotDBlockEntityTypes.RETORT, RetortFlaskBlockEntityRenderer::new);

		BuiltinItemRendererRegistry.INSTANCE.register(BotDObjects.JAR, new JarBlockEntityRenderer());
		EntityRendererRegistry.register(BotDEntityTypes.CORPSE_ENTITY, CorpseEntityRenderer::new);
		EntityRendererRegistry.register(BotDEntityTypes.OLD_MAN_ENTITY, OldManEntityRenderer::new);
		EntityRendererRegistry.register(BotDEntityTypes.BLOOD_SLIME_ENTITY, BloodSlimeEntityRenderer::new);

		EntityModelLayerRegistry.registerModelLayer(BagEntityModel.LAYER_LOCATION, BagEntityModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(JarEntityModel.LAYER_LOCATION, JarEntityModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(NecroTableBlockEntityRenderer.LAYER_LOCATION, NecroTableBlockEntityRenderer::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(ButcherTableBlockEntityRenderer.LAYER_LOCATION, ButcherTableBlockEntityRenderer::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(LargeCircleEntityModel.LAYER_LOCATION, LargeCircleEntityModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(OldManEntityModel.LAYER_LOCATION, OldManEntityModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(BloodSlimeEntityModel.LAYER_LOCATION, BloodSlimeEntityModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(BloodSlimeEntityModel.OUTER_LAYER_LOCATION, BloodSlimeEntityModel::createOverylayBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(BrainEntityModel.LAYER_LOCATION, BrainEntityModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(RetortFlaskBlockEntityRenderer.LAYER_LOCATION, RetortFlaskBlockEntityRenderer::createBodyLayer);

		BotDSpriteIdentifiers.INSTANCE.addIdentifier(BotDSpriteIdentifiers.BLOOD);

		ClientTickEvents.END.register(ClientTickHandler::clientTickEnd);

		for (Item item : BotDObjects.ITEMS.keySet()) {
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
	public static final class ClientTickHandler {
		private ClientTickHandler() {
		}

		public static int ticksInGame = 0;
		public static float partialTicks = 0;
		public static float delta = 0;
		public static float total = 0;

		public static void calcDelta() {
			float oldTotal = total;
			total = ticksInGame + partialTicks;
			delta = total - oldTotal;
		}

		public static void renderTick(float renderTickTime) {
			partialTicks = renderTickTime;
		}

		public static void clientTickEnd(MinecraftClient mc) {
			if (!mc.isPaused()) {
				ticksInGame++;
				partialTicks = 0;
			}
			calcDelta();
		}
	}
}
