package dev.sterner.book_of_the_dead;

import dev.sterner.book_of_the_dead.client.model.*;
import dev.sterner.book_of_the_dead.client.network.BloodSplashParticlePacket;
import dev.sterner.book_of_the_dead.client.renderer.block.*;
import dev.sterner.book_of_the_dead.client.renderer.entity.BloodSlimeEntityRenderer;
import dev.sterner.book_of_the_dead.client.renderer.entity.KakuzuEntityRenderer;
import dev.sterner.book_of_the_dead.client.renderer.entity.OldManEntityRenderer;
import dev.sterner.book_of_the_dead.client.renderer.entity.PlayerCorpseEntityRenderer;
import dev.sterner.book_of_the_dead.client.renderer.item.AllBlackSwordItemRenderer;
import dev.sterner.book_of_the_dead.common.registry.BotDBlockEntityTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDEntityTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.registry.BotDParticleTypes;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

import java.util.List;

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
				BotDObjects.RETORT_FLASK_BLOCK
		);

		ClientPlayNetworking.registerGlobalReceiver(BloodSplashParticlePacket.ID, BloodSplashParticlePacket::handle);

		BlockEntityRendererFactories.register(BotDBlockEntityTypes.HOOK, HookBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(BotDBlockEntityTypes.JAR, ctx -> new JarBlockEntityRenderer());
		BlockEntityRendererFactories.register(BotDBlockEntityTypes.NECRO, NecroTableBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(BotDBlockEntityTypes.BUTCHER, ButcherTableBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(BotDBlockEntityTypes.PEDESTAL, PedestalBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(BotDBlockEntityTypes.RETORT, RetortFlaskBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(BotDBlockEntityTypes.HEAD, ctx -> new BotDSkullBlockEntityRenderer());
		BlockEntityRendererFactories.register(BotDBlockEntityTypes.BRAIN, BrainBlockEntityRenderer::new);

		BuiltinItemRendererRegistry.INSTANCE.register(BotDObjects.JAR, new JarBlockEntityRenderer());
		BuiltinItemRendererRegistry.INSTANCE.register(BotDObjects.VILLAGER_HEAD, new BotDSkullBlockEntityRenderer());

		EntityRendererRegistry.register(BotDEntityTypes.OLD_MAN_ENTITY, OldManEntityRenderer::new);
		EntityRendererRegistry.register(BotDEntityTypes.BLOOD_SLIME_ENTITY, BloodSlimeEntityRenderer::new);
		EntityRendererRegistry.register(BotDEntityTypes.PLAYER_CORPSE_ENTITY, PlayerCorpseEntityRenderer::new);
		EntityRendererRegistry.register(BotDEntityTypes.KAKUZU_ENTITY, KakuzuEntityRenderer::new);

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

		ModelPredicateProviderRegistry.register(BotDObjects.CONTRACT, new Identifier("signed"), (itemStack, clientWorld, livingEntity, i) -> livingEntity == null ? 0 : itemStack.hasNbt() && itemStack.getOrCreateNbt().contains(Constants.Nbt.CONTRACT) ? 1.0F : 0.0F);
		ModelPredicateProviderRegistry.register(BotDObjects.SYRINGE, new Identifier("filled"), (itemStack, clientWorld, livingEntity, i) -> livingEntity == null ? 0 : itemStack.hasNbt() && itemStack.getOrCreateNbt().contains(Constants.Nbt.STATUS_EFFECT_INSTANCE) ? 1.0F : 0.0F);
		ModelPredicateProviderRegistry.register(BotDObjects.SYRINGE, new Identifier("blood"), (itemStack, clientWorld, livingEntity, i) -> livingEntity == null ? 0 : itemStack.hasNbt() && itemStack.getOrCreateNbt().contains(Constants.Nbt.BLOOD) ? 1.0F : 0.0F);
		ModelPredicateProviderRegistry.register(BotDObjects.MEAT_CLEAVER, new Identifier("bloody"), (itemStack, clientWorld, livingEntity, i) -> livingEntity == null ? 0 : itemStack.hasNbt() && itemStack.getOrCreateNbt().contains(Constants.Nbt.BLOOD) ? 1.0F : 0.0F);

		ClientTickEvents.END.register(ClientTickHandler::clientTickEnd);

		for (Item item : List.of(BotDObjects.ALL_BLACK)) {
			Identifier allBlackId = Registries.ITEM.getId(item);
			AllBlackSwordItemRenderer allBlackItemRenderer = new AllBlackSwordItemRenderer(allBlackId);
			ResourceLoader.get(ResourceType.CLIENT_RESOURCES).registerReloader(allBlackItemRenderer);
			BuiltinItemRendererRegistry.INSTANCE.register(item, allBlackItemRenderer);
			ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
				out.accept(new ModelIdentifier(allBlackId.withPath(allBlackId.getPath() + "_gui"), "inventory"));
				out.accept(new ModelIdentifier(allBlackId.withPath(allBlackId.getPath() + "_handheld"), "inventory"));
			});
		}

		ColorProviderRegistry.ITEM.register((itemStack, index) -> {
			if (index == 0) {
				if (itemStack.hasNbt() && itemStack.getOrCreateNbt().contains(Constants.Nbt.STATUS_EFFECT_INSTANCE)) {
					NbtCompound nbt = itemStack.getSubNbt(Constants.Nbt.STATUS_EFFECT_INSTANCE);
					if (nbt != null) {
						StatusEffect effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(nbt.getString(Constants.Nbt.STATUS_EFFECT)));
						if (effect != null) {
							return effect.getColor();
						}
					}
				}
			}
			return -1;
		}, BotDObjects.SYRINGE);
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
