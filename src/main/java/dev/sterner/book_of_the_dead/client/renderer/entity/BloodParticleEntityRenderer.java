package dev.sterner.book_of_the_dead.client.renderer.entity;

import dev.sterner.book_of_the_dead.common.entity.BloodParticleEntity;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class BloodParticleEntityRenderer extends EntityRenderer<BloodParticleEntity> {
	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Constants.id("blood"), "main");
	public static final int NUM_TEXTURES = 9;
	public static final Identifier[] TEXTURE = new Identifier[NUM_TEXTURES];

	private final ModelPart main;

	public BloodParticleEntityRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
		ModelPart root = ctx.getPart(LAYER_LOCATION);
		this.main = root.getChild("main");
	}

	@Override
	public void render(BloodParticleEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		int variant = entity.getDataTracker().get(BloodParticleEntity.VARIANT);
		main.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE[variant])), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
	}

	@Override
	public Identifier getTexture(BloodParticleEntity entity) {
		return TEXTURE[entity.getDataTracker().get(BloodParticleEntity.VARIANT)];
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("main", ModelPartBuilder.create().uv(0, 0).cuboid(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 16, 16);
	}

	static {
		for (int i = 0; i < NUM_TEXTURES; i++) {
			TEXTURE[i] = Constants.id("textures/entity/blood/" + i + ".png");
		}
	}
}
