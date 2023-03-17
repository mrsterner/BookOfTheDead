package dev.sterner.book_of_the_dead.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.quiltmc.loader.api.minecraft.ClientOnly;

public class SoapBubbleParticle extends SpriteBillboardParticle {

	public SoapBubbleParticle(ClientWorld clientWorld, double x, double y, double z, double dx, double dy, double dz) {
		super(clientWorld, x, y, z, dx, dy, dz);
		setBoundingBoxSpacing(0.02f, 0.02f);
		scale *= random.nextFloat() * 0.3 + 0.3;
		this.velocityX *= 0.1;
		this.velocityY *= 0.1;
		this.velocityZ *= 0.1;
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public void tick() {
		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;
		if (maxAge-- <= 0) {
			markDead();
		} else {
			move(velocityX, velocityY, velocityZ);
			velocityX *= 0.7;
			velocityY *= 0.7;
			velocityZ *= 0.7;
		}
	}

	@ClientOnly
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double posX, double posY, double posZ, double velocityX, double velocityY, double velocityZ) {
			SoapBubbleParticle particle = new SoapBubbleParticle(clientWorld, posX, posY, posZ, velocityX, velocityY, velocityZ);
			particle.setSprite(spriteProvider);
			return particle;
		}
	}
}
