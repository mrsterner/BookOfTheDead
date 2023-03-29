package dev.sterner.book_of_the_dead.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.quiltmc.loader.api.minecraft.ClientOnly;

public class SoulSpiralParticle extends SpriteBillboardParticle {
	private final SpriteProvider spriteProvider;
	protected double xTarget;
	protected double yTarget;
	protected double zTarget;
	protected Vec3d offset;

	protected SoulSpiralParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider, SoulSpiralParticleEffect effect) {
		super(world, x, y, z);
		this.gravityStrength = 0.0F;
		this.maxAge = 600 + random.nextInt(600);
		this.spriteProvider = spriteProvider;
		this.xTarget = effect.getTargetX();
		this.yTarget = effect.getTargetY();
		this.zTarget = effect.getTargetZ();
		this.setSpriteForAge(spriteProvider);
		this.gravityStrength = 0.0F;
		double maxDeviation = 0.2; // Maximum deviation from straight line path
		this.offset = new Vec3d(
				(Math.random() * maxDeviation * 2) - maxDeviation,
				(Math.random() * maxDeviation * 2) - maxDeviation,
				(Math.random() * maxDeviation * 2) - maxDeviation
		);
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		Vec3d vec3d = camera.getPos();
		float f = (float)(MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float g = (float)(MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float h = (float)(MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
		Quaternionf quaternionf;
		if (this.angle == 0.0F) {
			quaternionf = camera.getRotation();
		} else {
			quaternionf = new Quaternionf(camera.getRotation());
			quaternionf.rotateZ(MathHelper.lerp(tickDelta, this.prevAngle, this.angle));
		}

		Vector3f[] vector3fs = new Vector3f[]{
				new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)
		};
		float i = this.getSize(tickDelta);

		for(int j = 0; j < 4; ++j) {
			Vector3f vector3f = vector3fs[j];
			vector3f.rotate(quaternionf);
			vector3f.mul(i);
			vector3f.add(f, g, h);
		}

		float k = this.getMinU();
		float l = this.getMaxU();
		float m = this.getMinV();
		float n = this.getMaxV();
		int o = 15728880;

		vertexConsumer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).uv(l, n).color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha).light(o).next();
		vertexConsumer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).uv(l, m).color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha).light(o).next();
		vertexConsumer.vertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).uv(k, m).color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha).light(o).next();
		vertexConsumer.vertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).uv(k, n).color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha).light(o).next();
	}

	@Override
	public void tick() {

		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		if (this.age++ >= this.maxAge) {
			this.colorAlpha -= 0.05f;
		}
		if (this.colorAlpha < 0f || this.scale <= 0f) {
			this.markDead();
		}

		Vec3d targetVector = new Vec3d(this.xTarget - this.x, this.yTarget - this.y, this.zTarget - this.z);
		double length = targetVector.length();
		double factor = 0.1 / length;

		// Add some persistence to the random offset
		if (this.age % 10 == 0) { // Change the 10 to adjust the persistence
			double angle = Math.toRadians(45); // Maximum angle of deviation from straight line
			double randX = Math.random() * angle - angle / 2;
			double randY = Math.random() * angle - angle / 2;
			double randZ = Math.random() * angle - angle / 2;
			this.offset = new Vec3d(randX, randY, randZ);
		}
		targetVector = targetVector.add(this.offset).multiply(factor);

		velocityX = 0.9 * velocityX + 0.1 * targetVector.x;
		velocityY = 0.9 * velocityY + 0.1 * targetVector.y;
		velocityZ = 0.9 * velocityZ + 0.1 * targetVector.z;

		if (!new Vec3d(x, y, z).equals(new Vec3d(xTarget, yTarget, zTarget))) {
			this.move(velocityX, velocityY, velocityZ);
		}
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@ClientOnly
	public static class Factory implements ParticleFactory<SoulSpiralParticleEffect> {
		private final SpriteProvider spriteProvider;


		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Nullable
		@Override
		public Particle createParticle(SoulSpiralParticleEffect effect, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new SoulSpiralParticle(world, x, y, z, this.spriteProvider, effect);
		}
	}
}
