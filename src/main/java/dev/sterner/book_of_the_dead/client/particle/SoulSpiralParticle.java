package dev.sterner.book_of_the_dead.client.particle;

import dev.sterner.book_of_the_dead.api.particle.BotDParticle;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.Random;

public class SoulSpiralParticle extends BotDParticle {
	private final SpriteProvider spriteProvider;
	protected double xTarget;
	protected double yTarget;
	protected double zTarget;
	protected Vec3d offset;

	protected SoulSpiralParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider, SoulSpiralParticleEffect effect) {
		super(world, x, y, z);
		this.gravityStrength = 0.0F;
		this.maxAge = 40 + random.nextInt(10);
		this.spriteProvider = spriteProvider;
		this.colorRed = effect.getRed();
		this.colorGreen = effect.getGreen();
		this.colorBlue = effect.getBlue();
		this.colorAlpha = 0.75f;
		this.xTarget = effect.getTargetX();
		this.yTarget = effect.getTargetY();
		this.zTarget = effect.getTargetZ();
		this.velocityX *= 0.25F;
		this.velocityY *= 0.25F;
		this.velocityZ *= 0.25F;
		this.scale *= 0.05f + new Random().nextFloat() * 2.25f;
		this.setSpriteForAge(spriteProvider);
		this.gravityStrength = 0.0F;
		this.collidesWithWorld = false;
		double maxDeviation = 0.2; // Maximum deviation from straight line path
		this.offset = new Vec3d(
				(Math.random() * maxDeviation * 2) - maxDeviation,
				(Math.random() * maxDeviation * 2) - maxDeviation,
				(Math.random() * maxDeviation * 2) - maxDeviation
		);
	}

	@Override
	public void tick() {

		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		if (this.age++ >= this.maxAge) {
			this.colorAlpha -= 0.075f;
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

		velocityX = 0.5 * velocityX + 0.5 * targetVector.x;
		velocityY = 0.5 * velocityY + 0.5 * targetVector.y;
		velocityZ = 0.5 * velocityZ + 0.5 * targetVector.z;

		if (!new Vec3d(x, y, z).equals(new Vec3d(xTarget, yTarget, zTarget))) {
			this.move(velocityX, velocityY, velocityZ);
		}
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
