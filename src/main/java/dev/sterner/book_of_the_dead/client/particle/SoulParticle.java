package dev.sterner.book_of_the_dead.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.Random;

public class SoulParticle extends SpriteBillboardParticle {
	private final SpriteProvider spriteProvider;

	private SoulParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SoulParticleEffect soulTrailParticleEffect, SpriteProvider spriteProvider) {
		super(world, x, y, z, velocityX, velocityY, velocityZ);
		this.spriteProvider = spriteProvider;
		this.colorRed = soulTrailParticleEffect.getRed();
		this.colorGreen = soulTrailParticleEffect.getGreen();
		this.colorBlue = soulTrailParticleEffect.getBlue();
		this.maxAge = 50 + this.random.nextInt(10);
		this.scale *= 0.05f + new Random().nextFloat() * 2.25f;
		this.velocityX *= 0.1F;
		this.velocityY *= 0.1F;
		this.velocityZ *= 0.1F;
		this.velocityX += velocityX;
		this.velocityY += velocityY;
		this.velocityZ += velocityZ;
		this.collidesWithWorld = false;
		this.gravityStrength = 0;
		this.setSpriteForAge(spriteProvider);
	}

	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.age++ >= this.maxAge) {
			colorAlpha -= 0.05f;
		}
		if (colorAlpha < 0f || this.scale <= 0f) {
			this.markDead();
		}
		this.scale = Math.max(0, this.scale - 0.005f);
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

	@ClientOnly
	public static class Factory implements ParticleFactory<SoulParticleEffect> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(SoulParticleEffect soulTrailParticleEffect, ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new SoulParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ, soulTrailParticleEffect, this.spriteProvider);
		}
	}
}
