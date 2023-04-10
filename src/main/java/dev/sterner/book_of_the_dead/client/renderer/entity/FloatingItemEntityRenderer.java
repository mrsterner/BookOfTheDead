package dev.sterner.book_of_the_dead.client.renderer.entity;

import dev.sterner.book_of_the_dead.client.renderer.LightFXRenderer;
import dev.sterner.book_of_the_dead.common.entity.FloatingItemEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;

import java.awt.*;

public class FloatingItemEntityRenderer extends EntityRenderer<FloatingItemEntity> {
	public final ItemRenderer itemRenderer;

	public FloatingItemEntityRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
		this.itemRenderer = ctx.getItemRenderer();
		this.shadowRadius = 0;
		this.shadowOpacity = 0;
	}

	@Override
	public void render(FloatingItemEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		matrices.push();
		ItemStack itemStack = entity.getItem();
		BakedModel model = this.itemRenderer.getHeldItemModel(itemStack, entity.world, null, entity.getItem().getCount());
		float yOffset = entity.getYOffset(tickDelta);
		float scale = model.getTransformation().getTransformation(ModelTransformationMode.GROUND).scale.y();
		float rotation = entity.getRotation(tickDelta);
		matrices.translate(0.0D, (yOffset + 0.25F * scale), 0.0D);
		if (entity.getDataTracker().get(FloatingItemEntity.IS_SPECIAL_RENDER)) {
			matrices.push();
			matrices.translate(0, 0.15, 0);
			matrices.scale(0.0075f, 0.0075f, 0.0075f);
			((VertexConsumerProvider.Immediate) vertexConsumers).draw();
			LightFXRenderer.render(matrices, vertexConsumers, new Color(255, 59, 120));
			matrices.pop();
		}
		matrices.multiply(Axis.Y_POSITIVE.rotation(rotation));
		this.itemRenderer.renderItem(itemStack, ModelTransformationMode.GROUND, false, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, model);
		matrices.pop();

		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
	}

	@Override
	public Identifier getTexture(FloatingItemEntity entity) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
	}
}
