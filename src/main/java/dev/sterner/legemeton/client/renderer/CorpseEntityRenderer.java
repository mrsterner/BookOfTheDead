package dev.sterner.legemeton.client.renderer;

import dev.sterner.legemeton.common.entity.CorpseEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class CorpseEntityRenderer extends LivingEntityRenderer<CorpseEntity, EntityModel<CorpseEntity>> {
	public static final Identifier EMPTY = new Identifier("minecraft", "textures/block/redstone_dust_overlay.png");
	Entity renderedEntity = null;
	private final EntityRenderDispatcher dispatcher;

	public CorpseEntityRenderer(EntityRendererFactory.Context ctx) {
		super(ctx, new Model(ctx.getPart(EntityModelLayers.PLAYER)), 0.5F);
		this.dispatcher = ctx.getRenderDispatcher();
	}

	@Override
	public void render(CorpseEntity livingEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		super.render(livingEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
		NbtCompound nbtCompound = livingEntity.getCorpseEntity();
		EntityType.get(nbtCompound.getString("id")).ifPresent(type -> {
			if(renderedEntity == null){
				renderedEntity = type.create(livingEntity.world);
			}
			matrixStack.push();

			if (livingEntity.age > 0) {
				float f = ((float)livingEntity.age + tickDelta - 1.0F) / 20.0F * 1.6F;
				f = MathHelper.sqrt(f);
				if (f > 1.0F) {
					f = 1.0F;
				}

				matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(f * this.getLyingAngle(livingEntity)));
			}
			matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(livingEntity.getDataTracker().get(CorpseEntity.TRACKER_BODY_ROTATION).getYaw()));
			matrixStack.translate(0.25F,-0.5F,0.0F);

			dispatcher.render(renderedEntity, 0, 0, 0, 0, 0, matrixStack, vertexConsumerProvider, light);
			matrixStack.pop();
		});

	}

	@Override
	protected boolean hasLabel(CorpseEntity livingEntity) {
		return false;
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
