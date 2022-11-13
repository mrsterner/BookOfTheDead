package dev.sterner.legemeton.client.renderer.feature;

import dev.sterner.legemeton.api.interfaces.Hauler;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3f;
import net.minecraft.village.VillagerProfession;

public class ShoulderCropseFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	private Entity shoulderEntity = null;
	private Entity newEntity = null;
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
				EntityType.get(nbtCompound2.getString("id")).ifPresent(type -> {
					newEntity = type.create(entity.world);
					if(newEntity instanceof VillagerEntity villagerEntity && hauler.getVillagerData().getProfession() != VillagerProfession.NONE){
						villagerEntity.setVillagerData(hauler.getVillagerData());
						newEntity = villagerEntity;
					}
					if(newEntity instanceof AnimalEntity){
						matrices.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(90));
						matrices.translate(0,-1,0);
					}
					if(dispatcher != null){
						if(hauler.getIsBaby()){
							float g = 0.5F;
							matrices.scale(g, g, g);
						}else{
							float f = 0.85F;
							matrices.scale(f,f,f);
						}
						matrices.translate(0,-0.2,0.4);
						dispatcher.render(newEntity, 0, 0, 0, 0, 0, matrices, vertexConsumers, light);

					}
				});
			}
		});
		matrices.pop();
	}
}
