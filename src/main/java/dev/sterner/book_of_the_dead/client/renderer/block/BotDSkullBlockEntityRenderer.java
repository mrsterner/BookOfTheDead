package dev.sterner.book_of_the_dead.client.renderer.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.api.block.AbstractBotDSkullBlock;
import dev.sterner.book_of_the_dead.client.model.BotDSkullBlockEntityModel;
import dev.sterner.book_of_the_dead.common.block.BotDSkullBlock;
import dev.sterner.book_of_the_dead.common.block.BotDWallSkullBlock;
import dev.sterner.book_of_the_dead.common.block.entity.BotDSkullBlockEntity;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BotDSkullBlockEntityRenderer implements BlockEntityRenderer<BotDSkullBlockEntity>, BuiltinItemRendererRegistry.DynamicItemRenderer {
	private final Map<BotDSkullBlock.BotDType, BotDSkullBlockEntityModel> MODELS;

	private static final Map<BotDSkullBlock.BotDType, Identifier> TEXTURES = Util.make(Maps.newHashMap(), map -> {
		map.put(BotDSkullBlock.Type.VILLAGER, new Identifier("textures/entity/villager/villager.png"));
	});

	public static Map<BotDSkullBlock.BotDType, BotDSkullBlockEntityModel> getModels(EntityModelLoader modelLoader) {
		ImmutableMap.Builder<BotDSkullBlock.BotDType, BotDSkullBlockEntityModel> builder = ImmutableMap.builder();
		builder.put(BotDSkullBlock.Type.VILLAGER, new BotDSkullBlockEntityModel(modelLoader.getModelPart(BotDSkullBlockEntityModel.LAYER_LOCATION)));
		return builder.build();
	}

	public BotDSkullBlockEntityRenderer() {
		MODELS = Map.of(BotDSkullBlock.Type.VILLAGER, new BotDSkullBlockEntityModel(BotDSkullBlockEntityModel.getSkullTexturedModelData().createModel()));
	}

	@Override
	public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		BotDSkullBlockEntityModel skullBlockEntityModel = this.MODELS.get(BotDSkullBlock.Type.VILLAGER);
		RenderLayer renderLayer = getRenderLayer(BotDSkullBlock.Type.VILLAGER);
		renderSkull(null, 0, matrices, vertexConsumers, light, skullBlockEntityModel, renderLayer);
	}

	@Override
	public void render(BotDSkullBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		BlockState blockState = entity.getCachedState();
		boolean bl = blockState.getBlock() instanceof BotDWallSkullBlock;
		Direction direction = bl ? blockState.get(BotDWallSkullBlock.FACING) : null;
		float h = 22.5F * (float) (bl ? (2 + direction.getHorizontal()) * 4 : blockState.get(BotDSkullBlock.ROTATION));
		BotDSkullBlock.BotDType skullType = ((AbstractBotDSkullBlock) blockState.getBlock()).getSkullType();
		BotDSkullBlockEntityModel skullBlockEntityModel = this.MODELS.get(skullType);
		RenderLayer renderLayer = getRenderLayer(skullType);
		renderSkull(direction, h, matrices, vertexConsumers, light, skullBlockEntityModel, renderLayer);
	}

	public static void renderSkull(@Nullable Direction direction, float yaw, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, BotDSkullBlockEntityModel model, RenderLayer renderLayer) {
		matrices.push();
		if (direction == null) {
			matrices.translate(0.5, 0.0, 0.5);
		} else {
			float f = 0.25F;
			matrices.translate((0.5F - (float) direction.getOffsetX() * f), f, (double) (0.5F - (float) direction.getOffsetZ() * f));
		}

		matrices.scale(-1.0F, -1.0F, 1.0F);
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
		model.setHeadRotation(yaw, 0.0F);
		model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		matrices.pop();
	}

	public static RenderLayer getRenderLayer(BotDSkullBlock.BotDType type) {
		Identifier identifier = TEXTURES.get(type);
		return RenderLayer.getEntityCutoutNoCullZOffset(identifier);
	}


}
