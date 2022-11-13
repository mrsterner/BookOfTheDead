package dev.sterner.legemeton.client.renderer.feature;

import dev.sterner.legemeton.api.interfaces.Hauler;
import dev.sterner.legemeton.common.entity.CorpseEntity;
import dev.sterner.legemeton.common.util.Constants;
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
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
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
				//NbtCompound nbtCompound = hauler.getCorpsedEntity();
				NbtCompound nbtCompound2 = hauler.getCorpseEntity();
				EntityType.get(nbtCompound2.getString("id")).ifPresent(type -> {
					newEntity = type.create(entity.world);
					if(dispatcher != null){
						System.out.println("RenderNew: " + newEntity);
						dispatcher.render(newEntity, 0, 0, 0, 0, 0, matrices, vertexConsumers, light);

					}
				});

				/*
				EntityType.get(nbtCompound.getString("id")).ifPresent(type -> {
						shoulderEntity = type.create(entity.world);
						NbtCompound nbt = new NbtCompound();
						//System.out.println("Entity" + shoulderEntity);
						shoulderEntity.writeNbt(nbt);
						if(shoulderEntity instanceof CorpseEntity corpse){
							System.out.println("U: "+ nbt.contains(Constants.Nbt.TARGET, NbtElement.COMPOUND_TYPE));
							if (nbt.contains(Constants.Nbt.TARGET, NbtElement.COMPOUND_TYPE)) {
								corpse.setCorpseEntity(nbt.getCompound(Constants.Nbt.TARGET));
							}
						}
						System.out.println("NBTTTTTTT" + nbt);
						EntityType.get(nbt.getString("id")).ifPresent(type2 -> {
							System.out.println("Type2: " + type2);
						});

						if(dispatcher != null){
							System.out.println("Render: " + shoulderEntity);
							dispatcher.render(shoulderEntity, 0, 0, 0, 0, 0, matrices, vertexConsumers, light);

						}

					//System.out.println("NBT: "+ nbt);


				});

				 */

			}

		});
		matrices.pop();
	}
}
