package dev.sterner.legemeton.client.renderer.feature;

import dev.sterner.legemeton.api.interfaces.Hauler;
import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.client.network.AbstractClientPlayerEntity;
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
import net.minecraft.util.math.Vec3f;

public class ShoulderCropseFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	private final EntityRenderDispatcher dispatcher;

	public ShoulderCropseFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context, EntityModelLoader loader, EntityRenderDispatcher dispatcher) {
		super(context);
		this.dispatcher = dispatcher;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		matrices.push();
		Hauler.of(entity).ifPresent(hauler -> {
			if(!hauler.getCorpseEntity().isEmpty()){
				NbtCompound nbtCompound2 = hauler.getCorpseEntity();
				EntityType.getEntityFromNbt(nbtCompound2.getCompound(Constants.Nbt.CORPSE_ENTITY), entity.world).ifPresent(type -> {
					if(type instanceof LivingEntity livingEntity){
						livingEntity.hurtTime = 0;
						if(type instanceof AnimalEntity){
							matrices.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(90));
							matrices.translate(0,-1,0);
						}
						if(livingEntity.isBaby()){
							matrices.translate(0,0,-0.2);
						}else{
							float f = 0.85F;
							matrices.scale(f,f,f);
						}
						if(dispatcher != null){
							matrices.translate(0,-0.3,0.4);
							dispatcher.render(livingEntity, 0, 0, 0, 0, 0, matrices, vertexConsumers, light);

						}
					}
				});
			}
		});
		matrices.pop();
	}
}
