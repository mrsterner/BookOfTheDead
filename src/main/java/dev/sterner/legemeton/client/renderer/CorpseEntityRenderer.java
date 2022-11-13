package dev.sterner.legemeton.client.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import dev.sterner.legemeton.common.entity.CorpseEntity;
import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;

public class CorpseEntityRenderer extends LivingEntityRenderer<CorpseEntity, EntityModel<CorpseEntity>> {
	public static final Identifier EMPTY = new Identifier("minecraft", "textures/block/redstone_dust_overlay.png");
	private Entity renderedEntity = null;
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
			if(nbtCompound.contains("Yaw")){
				renderedEntity.bodyYaw = nbtCompound.getFloat("Yaw");
			}
			System.out.println("IsBaby: "+ nbtCompound.contains("IsBaby"));
			System.out.println("Yaw: " + nbtCompound.contains("Yaw"));
			System.out.println("VillagerData: " + nbtCompound.contains("VillagerData", NbtElement.COMPOUND_TYPE));
			if(nbtCompound.contains("IsBaby") && renderedEntity instanceof MobEntity mobEntity){

				mobEntity.setBaby(nbtCompound.getBoolean("IsBaby"));
				renderedEntity = mobEntity;
			}
			if (nbtCompound.contains("VillagerData", NbtElement.COMPOUND_TYPE) && renderedEntity instanceof VillagerEntity villagerEntity) {
				DataResult<VillagerData> dataResult = VillagerData.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, nbtCompound.get("VillagerData")));
				villagerEntity.setVillagerData(dataResult.get().orThrow());
			}

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
			dispatcher.render(renderedEntity, 0, 0, 0, 0, 0, matrixStack, vertexConsumerProvider, light);
		}else{


			EntityType.get(nbtCompound.getString("id")).ifPresent(type -> {
				corpseEntity.storedCorpseEntity = (LivingEntity) type.create(corpseEntity.world);
			});
		}
		matrixStack.pop();
		/*
		if(dispatcher.getRenderer(livingEntity1) instanceof MobEntityRenderer mobEntityRenderer){
			EntityModel entityModel = mobEntityRenderer.getModel();
			RenderLayer renderLayer = RenderLayer.getEntityTranslucent(mobEntityRenderer.getTexture(livingEntity1));
			if (renderLayer != null) {
				VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
				int overlay = getOverlay(livingEntity, this.getAnimationCounter(livingEntity, tickDelta));
				entityModel.render(matrixStack, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
			}
		}


		 */

		/*
		NbtCompound nbtCompound = livingEntity.storedCorpseEntity();
		EntityType.get(nbtCompound.getString("id")).ifPresent(type -> {
			renderedEntity = type.create(livingEntity.world);


			if(renderedEntity instanceof VillagerEntity villagerEntity && livingEntity.getVillagerData().getProfession() != VillagerProfession.NONE){
				villagerEntity.setVillagerData(livingEntity.getVillagerData());
				renderedEntity = villagerEntity;
			}



			matrixStack.push();
			matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(livingEntity.getDataTracker().get(Constants.DataTrackers.TRACKER_BODY_ROTATION).getYaw()));
			if (livingEntity.age > 0 && livingEntity.getIsDying()) {
				float f = ((float)livingEntity.age + tickDelta - 1.0F) / 20.0F * 1.6F;
				f = MathHelper.sqrt(f);
				if (f > 1.0F) {
					f = 1.0F;
				}
				matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(f * this.getLyingAngle(livingEntity)));
			}else{
				matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(this.getLyingAngle(livingEntity)));
			}

			matrixStack.translate(0.25F,-0.75F,0.0F);
			if(livingEntity.getIsBaby()){
				float g = 0.5F;
				matrixStack.scale(g, g, g);
			}

			EntityRenderer<?> entityRenderer =  dispatcher.renderers.get(type);
			if(entityRenderer instanceof LivingEntityRenderer<?,?> livingEntityRenderer){
				EntityModel<? extends LivingEntity> entityModel = livingEntityRenderer.getModel();
				livingEntityRenderer.getTexture();
				RenderLayer renderLayer = RenderLayer.getEntityTranslucent(new Identifier(livingEntity.getIdentifierId()));
				if (renderLayer != null) {
					VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
					int overlay = getOverlay(livingEntity, this.getAnimationCounter(livingEntity, tickDelta));
					entityModel.render(matrixStack, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
				}
			}


			//dispatcher.render(renderedEntity, 0, 0, 0, 0, 0, matrixStack, vertexConsumerProvider, light);
			matrixStack.pop();
		});

		 */

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
