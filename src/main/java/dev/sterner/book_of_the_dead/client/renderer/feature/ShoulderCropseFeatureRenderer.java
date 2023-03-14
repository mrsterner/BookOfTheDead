package dev.sterner.book_of_the_dead.client.renderer.feature;

import dev.sterner.book_of_the_dead.api.interfaces.IHauler;
import dev.sterner.book_of_the_dead.client.model.BagEntityModel;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;

public class ShoulderCropseFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	private final EntityRenderDispatcher dispatcher;
	private final BagEntityModel model;
	public static final Identifier BAG = Constants.id("textures/entity/bag.png");

	public ShoulderCropseFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context, EntityModelLoader loader, EntityRenderDispatcher dispatcher) {
		super(context);
		this.dispatcher = dispatcher;
		this.model = new BagEntityModel(loader.getModelPart(BagEntityModel.LAYER_LOCATION));
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		matrices.push();
		IHauler.of(entity).ifPresent(hauler -> {
			if(!hauler.getCorpseEntity().isEmpty()){
				NbtCompound nbtCompound2 = hauler.getCorpseEntity();
				EntityType.getEntityFromNbt(nbtCompound2, entity.world).ifPresent(type -> {
					if(type instanceof LivingEntity livingEntity && dispatcher != null){
						livingEntity.hurtTime = 0;
						livingEntity.deathTime = 0;
						dispatcher.setRenderShadows(false);
						matrices.push();
						matrices.multiply(Axis.X_POSITIVE.rotationDegrees(entity.isInSneakingPose() ? 20 : 0));
						if(livingEntity instanceof AnimalEntity){
							renderQuadraped(matrices, vertexConsumers, light, livingEntity);
						}else{
							renderHumanoid(matrices, vertexConsumers, light, livingEntity);
						}
						matrices.pop();
					}
				});
			}
		});
		matrices.pop();
	}
	public void renderQuadraped(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity livingEntity){
		matrices.push();
		matrices.multiply(Axis.X_NEGATIVE.rotationDegrees(90));
		if(livingEntity.isBaby()){
			matrices.translate(0,-1,0);
		}else{
			float f = 0.75f;
			matrices.scale(f,f,f);
			matrices.translate(0,-1.5,0);
		}
		dispatcher.render(livingEntity, 0, 0, 0, 0, 0, matrices, vertexConsumers, light);
		matrices.pop();

		matrices.push();
		matrices.translate(0,-0.5,0.6);
		float g = 1.1f;
		matrices.scale(g,g,g);
		this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(BAG)),light, OverlayTexture.DEFAULT_UV, 1,1,1,1);
		matrices.pop();
	}

	public void renderHumanoid(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity livingEntity){
		matrices.push();
		if(livingEntity.isBaby()){
			matrices.translate(0,-0.25,0.5);
		}else{
			float f = 0.75f;
			matrices.scale(f,f,f);
			matrices.translate(0,-1.0,0.5);
		}
		dispatcher.render(livingEntity, 0, 0, 0, 0, 0, matrices, vertexConsumers, light);
		matrices.pop();

		matrices.push();
		matrices.translate(0,-0.5,0.6);
		float g = 1.1f;
		matrices.scale(g,g,g);
		this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(BAG)),light, OverlayTexture.DEFAULT_UV, 1,1,1,1);
		matrices.pop();
	}
}
