package dev.sterner.legemeton.client.renderer;

import dev.sterner.legemeton.common.entity.CorpseEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

public class CorpseEntityRenderer extends LivingEntityRenderer<CorpseEntity, EntityModel<CorpseEntity>> {
	public static final Identifier EMPTY = new Identifier("minecraft", "textures/block/redstone_dust_overlay.png");

	public CorpseEntityRenderer(EntityRendererFactory.Context ctx) {
		super(ctx, new Model(ctx.getPart(EntityModelLayers.PLAYER)), 0.5F);
		addFeature(new FeatureRenderer<>(this) {



			@Override
			public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CorpseEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
				if(entity.getLivingEntity() != null){
					matrices.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(90.0F));
					ctx.getRenderDispatcher().render(entity.livingEntity, entity.getX(), entity.getY(), entity.getZ(), entity.getYaw(), tickDelta, matrices, vertexConsumers, light);
				}

				//VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getTranslucent());
				//getContextModel().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
			}
		});
	}

	@Override
	public Identifier getTexture(CorpseEntity entity) {
		return EMPTY;
	}

	private static class Model extends BipedEntityModel<CorpseEntity> {
		Model(ModelPart root) {
			super(root);
		}
	}
}
