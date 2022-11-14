package dev.sterner.legemeton.client.renderer;

import dev.sterner.legemeton.common.entity.CorpseEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class CorpseEntityRenderer extends LivingEntityRenderer<CorpseEntity, EntityModel<CorpseEntity>> {
	public static final Identifier EMPTY = new Identifier("minecraft", "textures/block/redstone_dust_overlay.png");
	private final EntityRenderDispatcher dispatcher;

	public CorpseEntityRenderer(EntityRendererFactory.Context ctx) {
		super(ctx, new Model(ctx.getPart(EntityModelLayers.PLAYER)), 0.5F);
		this.dispatcher = ctx.getRenderDispatcher();
	}

	@Override
	public void render(CorpseEntity corpseEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		super.render(corpseEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
		LivingEntity renderedEntity = corpseEntity.storedCorpseEntity;
		matrixStack.push();
		NbtCompound nbtCompound = corpseEntity.getCorpseEntity();
		if(renderedEntity != null){
			renderedEntity.hurtTime = 0;

			matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(renderedEntity.bodyYaw));
			if (renderedEntity.age > 0) {
				float f = ((float)renderedEntity.age + tickDelta - 1.0F) / 20.0F * 1.6F;
				f = MathHelper.sqrt(f);
				if (f > 1.0F) {
					f = 1.0F;
				}
				matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(f * 90));
			}else{
				matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90));
			}

			matrixStack.translate(0.25F,-0.75F,0.0F);
			if(renderedEntity.isBaby()){
				float g = 0.5F;
				matrixStack.scale(g, g, g);
			}
			dispatcher.setRenderShadows(false);
			dispatcher.render(renderedEntity, 0, 0, 0, 0, 0, matrixStack, vertexConsumerProvider, light);
		}else{
			EntityType.getEntityFromNbt(nbtCompound, corpseEntity.world).ifPresent(type -> {
				if(type instanceof LivingEntity livingEntity){
					corpseEntity.storedCorpseEntity = livingEntity;
				}

			});
		}
		matrixStack.pop();
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
