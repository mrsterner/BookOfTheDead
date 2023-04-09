package dev.sterner.book_of_the_dead.client.renderer.feature;

import dev.sterner.book_of_the_dead.common.entity.BloodParticleEntity;
import dev.sterner.book_of_the_dead.common.registry.BotDEntityTypes;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.StuckObjectsFeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.random.RandomGenerator;
import org.joml.Quaternionf;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.ArrayList;
import java.util.List;

@ClientOnly
public class StuckBloodFeatureRenderer<T extends LivingEntity, M extends PlayerEntityModel<T>> extends StuckObjectsFeatureRenderer<T, M> {
	private final EntityRenderDispatcher dispatcher;
	private List<Entity> bloods = new ArrayList<>();

	public StuckBloodFeatureRenderer(EntityRendererFactory.Context context, LivingEntityRenderer<T, M> entityRenderer) {
		super(entityRenderer);
		this.dispatcher = context.getRenderDispatcher();
	}

	@Override
	protected int getObjectCount(T entity) {
		return 24;
	}

	@Override
	protected void renderObject(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, float directionX, float directionY, float directionZ, float tickDelta) {

	}

	protected void renderObject(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, float directionX, float directionY, float directionZ, float tickDelta, int n) {

		//matrices.translate(0,-1.5,0);
		//matrices.scale(1.1f,1.1f,1.1f);
		if (!bloods.isEmpty()) {

			this.dispatcher.render(bloods.get(n), 0.0, 0.0, 0.0, 0, tickDelta, matrices, vertexConsumers, light);
		}
	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
		int m = this.getObjectCount(livingEntity);
		RandomGenerator randomGenerator = RandomGenerator.createLegacy(livingEntity.getId());
		if (m > 0) {
			for (int n = 0; n < m; ++n) {

				matrixStack.push();
				float yaw = (float) (bloods.get(n).getYaw() * (Math.PI / 180)); // convert to radians
				float pitch = (float) (bloods.get(n).getPitch() * (Math.PI / 180)); // convert to radians

				// create quaternions
				Quaternionf yawQuat = new Quaternionf().rotateY(yaw);
				Quaternionf pitchQuat = new Quaternionf().rotateX(pitch);

				Quaternionf rotationQuat = pitchQuat.mul(yawQuat);

				matrixStack.multiply(rotationQuat);
				matrixStack.translate(0, -livingEntity.getHeight(), 0);

				ModelPart modelPart = this.getContextModel().getRandomPart(randomGenerator);
				ModelPart.Cuboid cuboid = modelPart.getRandomCuboid(randomGenerator);
				modelPart.rotate(matrixStack);
				float o = randomGenerator.nextFloat();
				float p = randomGenerator.nextFloat();
				float q = randomGenerator.nextFloat();
				float r = MathHelper.lerp(o, cuboid.minX - 1, cuboid.maxX + 1) / 16.0F;
				float s = MathHelper.lerp(p, cuboid.minY, cuboid.maxY) / 16.0F;
				float t = MathHelper.lerp(q, cuboid.minZ - 1, cuboid.maxZ + 1) / 16.0F;

				float fd = MathHelper.sqrt(o * o + q * q);

				matrixStack.translate(r, s, t);
				o = -1.0F * (o * 2.0F - 1.0F);
				p = -1.0F * (p * 2.0F - 1.0F);
				q = -1.0F * (q * 2.0F - 1.0F);
				if (bloods.size() <= n || bloods.get(n) == null) {
					BloodParticleEntity bloodParticle = new BloodParticleEntity(BotDEntityTypes.BLOOD_PARTICLE_ENTITY, livingEntity.world);
					bloodParticle.getDataTracker().set(BloodParticleEntity.VARIANT, livingEntity.world.random.nextInt(8));
					bloodParticle.setYaw((float) (Math.atan2((double) o, (double) q) * 180.0F / (float) Math.PI));
					bloodParticle.setPitch((float) (Math.atan2((double) p, (double) fd) * 180.0F / (float) Math.PI));
					bloodParticle.prevYaw = bloodParticle.getYaw();
					bloodParticle.prevPitch = bloodParticle.getPitch();
					if (bloods.size() <= n) {
						bloods.add(bloodParticle); // Add a new element to the end of the list
					} else if (bloods.get(n) == null) {
						bloods.set(n, bloodParticle); // Update an existing null element at the specified index
					}
				}
				this.renderObject(matrixStack, vertexConsumerProvider, i, livingEntity, o, p, q, h, n);
				matrixStack.pop();
			}
		}
	}
}
