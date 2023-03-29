package dev.sterner.book_of_the_dead.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
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
