package dev.sterner.book_of_the_dead.client.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tessellator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import dev.sterner.book_of_the_dead.api.particle.BotDParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.quiltmc.loader.api.minecraft.ClientOnly;

public class OrbitParticle extends BotDParticle {
	private final SpriteProvider spriteProvider;
	private final Vec3d center; // point B
	private double radius;
	private double angle;

	public OrbitParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider, OrbitParticleEffect effect) {
		super(world, x, y, z);
		this.colorRed = effect.getRed();
		this.colorGreen = effect.getGreen();
		this.colorBlue = effect.getBlue();
		this.center = new Vec3d(effect.getTargetX(), effect.getTargetY(), effect.getTargetZ());
		this.radius = effect.getRadius();
		this.angle = 0;
		this.spriteProvider = spriteProvider;
		this.setSpriteForAge(spriteProvider);
		this.gravityStrength = 0.0F;
		this.collidesWithWorld = false;
		this.maxAge = 400 + random.nextInt(30);
	}

	@Override
	public void tick() {

		if (this.age++ >= this.maxAge) {
			this.markDead();
		}

		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		double nx = this.center.x + this.radius * Math.cos(this.angle);
		double ny = this.y;
		double nz = this.center.z + this.radius * Math.sin(this.angle);
		Vec3d targetVector = new Vec3d(nx - this.x, ny - this.y, nz - this.z);

		velocityX = (0.75) * velocityX + (0.25) * targetVector.x;
		velocityY = (0.75) * velocityY + (0.25) * targetVector.y;
		velocityZ = (0.75) * velocityZ + (0.25) * targetVector.z;

		this.world.addParticle(new SoulSpiralParticleEffect(this.colorRed, this.colorGreen, this.colorBlue, (float) x,  (float) y,  (float) z), this.x + random.nextGaussian() / 15, this.y + random.nextGaussian() / 15, this.z + random.nextGaussian() / 15, 0, 0, 0);

		this.move(velocityX, velocityY, velocityZ);
		this.angle += 0.075; // adjust angle speed
		this.radius -= 0.01;
		if(this.radius < 0.1){
			markDead();
		}
	}

	@ClientOnly
	public static class Factory implements ParticleFactory<OrbitParticleEffect> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Nullable
		@Override
		public Particle createParticle(OrbitParticleEffect effect, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new OrbitParticle(world, x, y, z, this.spriteProvider, effect);
		}
	}
}
