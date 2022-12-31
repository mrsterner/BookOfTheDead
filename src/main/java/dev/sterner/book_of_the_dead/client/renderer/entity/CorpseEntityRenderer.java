package dev.sterner.book_of_the_dead.client.renderer.entity;

import dev.sterner.book_of_the_dead.common.entity.CorpseEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class CorpseEntityRenderer extends LivingEntityRenderer<CorpseEntity, EntityModel<CorpseEntity>> {
	public static final Identifier EMPTY = new Identifier("minecraft", "textures/block/redstone_dust_overlay.png");
	private final EntityRenderDispatcher dispatcher;
	int k = 0;

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

			matrixStack.translate(-0.25F,0.25F,0.0F);
			matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(renderedEntity.bodyYaw));
			if (corpseEntity.age > 0) {
				float f = ((float)corpseEntity.age + tickDelta - 1.0F) / 20.0F * 1.6F;
				f = MathHelper.sqrt(f);
				if (f > 1.0F) {
					f = 1.0F;
				}
				matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(f * 90));
			}else{
				matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90));
			}
			if(renderedEntity.isBaby()){
				float g = 0.5F;
				matrixStack.scale(g, g, g);
				matrixStack.translate(-0.2,0,0);
			}
			if(corpseEntity.age > 20){
				float g = ((float)corpseEntity.age + tickDelta - 20 - 1.0F);
				g = MathHelper.sqrt(g);
				if (g > 1.0F) {
					g = 1.0F;
				}

				if(corpseEntity.age < 26){
					for(int i = 0; i < 10; ++i) {
						double d = corpseEntity.world.random.nextGaussian() * 0.02;
						double e = corpseEntity.world.random.nextGaussian() * 0.02;
						double f = corpseEntity.world.random.nextGaussian() * 0.02;
						corpseEntity.world.addParticle(ParticleTypes.POOF, corpseEntity.getParticleX(1.0), corpseEntity.getRandomBodyY(), corpseEntity.getParticleZ(1.0), d, e, f);
					}
				}
				matrixStack.translate(0,-0.65 * renderedEntity.getHeight() * g,0);
			}
			dispatcher.setRenderShadows(false);
			if(renderedEntity instanceof PlayerEntity player){
				dispatcher.render(player,0,0,0,0,0, matrixStack, vertexConsumerProvider, light);//Skins?
			}else{
				dispatcher.render(renderedEntity, 0, 0, 0, 0, 0, matrixStack, vertexConsumerProvider, light);
			}

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
