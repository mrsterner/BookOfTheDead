package dev.sterner.book_of_the_dead.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class BloodSplashParticle extends SpriteBillboardParticle {
	private final float sampleU;
	private final float sampleV;

	protected BloodSplashParticle(ClientWorld world, double x, double y, double z, double dx, double dy, double dz) {
		super(world, x, y, z);
		this.gravityStrength = 0.75F;
		this.velocityMultiplier = 0.999F;
		this.velocityX *= 0.8F;
		this.velocityY *= 0.8F;
		this.velocityZ *= 0.8F;
		this.velocityY = (this.random.nextFloat() * 0.2F + 0.05F);
		this.scale /= 2;
		this.scale *= this.random.nextFloat() * 2.0F + 0.2F;
		this.maxAge = (int) (64.0 / (Math.random() * 0.8 + 0.2));

		this.sampleU = this.random.nextFloat() * 3.0F;
		this.sampleV = this.random.nextFloat() * 3.0F;
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	protected float getMinU() {
		return this.sprite.getFrameU((this.sampleU + 1.0F) / 4.0F * 16.0F);
	}

	@Override
	protected float getMaxU() {
		return this.sprite.getFrameU(this.sampleU / 4.0F * 16.0F);
	}

	@Override
	protected float getMinV() {
		return this.sprite.getFrameV(this.sampleV / 4.0F * 16.0F);
	}

	@Override
	protected float getMaxV() {
		return this.sprite.getFrameV((this.sampleV + 1.0F) / 4.0F * 16.0F);
	}

	@Environment(EnvType.CLIENT)
	public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public DefaultFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double x, double y, double z, double dx, double dy, double dz) {
			BloodSplashParticle bloodSplashParticle = new BloodSplashParticle(clientWorld, x, y, z, dx, dy + 1.0, dz);
			bloodSplashParticle.setSprite(this.spriteProvider);
			return bloodSplashParticle;
		}
	}
}
