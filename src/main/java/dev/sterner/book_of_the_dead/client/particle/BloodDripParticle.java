package dev.sterner.book_of_the_dead.client.particle;

import dev.sterner.book_of_the_dead.common.registry.BotDParticleTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.BlockLeakParticle;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public class BloodDripParticle extends SpriteBillboardParticle {
	private final Fluid fluid;
	protected boolean obsidianTear;

	public BloodDripParticle(ClientWorld world, double x, double y, double z, Fluid fluid) {
		super(world, x, y, z);
		this.setBoundingBoxSpacing(0.01F, 0.01F);
		this.gravityStrength = 0.06F;
		this.fluid = fluid;
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public int getBrightness(float tint) {
		return this.obsidianTear ? 240 : super.getBrightness(tint);
	}

	@Override
	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;
		this.updateAge();
		if (!this.dead) {
			this.velocityY -= (double)this.gravityStrength;
			this.move(this.velocityX, this.velocityY, this.velocityZ);
			this.updateVelocity();
			if (!this.dead) {
				this.velocityX *= 0.98F;
				this.velocityY *= 0.98F;
				this.velocityZ *= 0.98F;
				if (this.fluid != Fluids.EMPTY) {
					BlockPos blockPos = BlockPos.create(this.x, this.y, this.z);
					FluidState fluidState = this.world.getFluidState(blockPos);
					if (fluidState.getFluid() == this.fluid && this.y < (double)((float)blockPos.getY() + fluidState.getHeight(this.world, blockPos))) {
						this.markDead();
					}
				}
			}
		}
	}

	protected void updateAge() {
		if (this.maxAge-- <= 0) {
			this.markDead();
		}
	}

	protected void updateVelocity() {
	}

	public static SpriteBillboardParticle createBloodHangParticle(
			DefaultParticleType defaultParticleType, ClientWorld world, double d, double e, double f, double g, double h, double i
	) {
		BloodDripParticle.Dripping dripping = new BloodDripParticle.Dripping(world, d, e, f, Fluids.EMPTY, BotDParticleTypes.FALLING_BLOOD);
		dripping.gravityStrength *= 0.01F;
		dripping.maxAge = 100;
		dripping.setColor(0.62F, 0.0F, 0.1F);
		return dripping;
	}

	public static SpriteBillboardParticle createBloodFallParticle(
			DefaultParticleType defaultParticleType, ClientWorld world, double d, double e, double f, double g, double h, double i
	) {
		BloodDripParticle blockLeakParticle = new BloodDripParticle.FallingBlood(world, d, e, f, Fluids.EMPTY, BotDParticleTypes.LANDING_BLOOD);
		blockLeakParticle.gravityStrength = 0.01F;
		blockLeakParticle.setColor(0.67F, 0.04F, 0.05F);
		return blockLeakParticle;
	}

	public static SpriteBillboardParticle createBloodLandParticle(
			DefaultParticleType defaultParticleType, ClientWorld world, double d, double e, double f, double g, double h, double i
	) {
		BloodDripParticle blockLeakParticle = new BloodDripParticle.Landing(world, d, e, f, Fluids.EMPTY);
		blockLeakParticle.maxAge = (int)(128.0 / (Math.random() * 0.8 + 0.2));
		blockLeakParticle.setColor(0.67F, 0.04F, 0.05F);
		return blockLeakParticle;
	}

	@ClientOnly
	static class Dripping extends BloodDripParticle {
		private final ParticleEffect nextParticle;

		Dripping(ClientWorld world, double x, double y, double z, Fluid fluid, ParticleEffect nextParticle) {
			super(world, x, y, z, fluid);
			this.nextParticle = nextParticle;
			this.gravityStrength *= 0.02F;
			this.maxAge = 40;
		}

		@Override
		protected void updateAge() {
			if (this.maxAge-- <= 0) {
				this.markDead();
				this.world.addParticle(this.nextParticle, this.x, this.y, this.z, this.velocityX, this.velocityY, this.velocityZ);
			}
		}

		@Override
		protected void updateVelocity() {
			this.velocityX *= 0.02;
			this.velocityY *= 0.02;
			this.velocityZ *= 0.02;
		}
	}

	@ClientOnly
	static class Falling extends BloodDripParticle {
		Falling(ClientWorld clientWorld, double d, double e, double f, Fluid fluid) {
			this(clientWorld, d, e, f, fluid, (int)(64.0 / (Math.random() * 0.8 + 0.2)));
		}

		Falling(ClientWorld world, double x, double y, double z, Fluid fluid, int maxAge) {
			super(world, x, y, z, fluid);
			this.maxAge = maxAge;
		}

		@Override
		protected void updateVelocity() {
			if (this.onGround) {
				this.markDead();
			}
		}
	}

	@ClientOnly
	public static class ContinuousFalling extends BloodDripParticle.Falling {
		protected final ParticleEffect nextParticle;

		ContinuousFalling(ClientWorld world, double x, double y, double z, Fluid fluid, ParticleEffect nextParticle) {
			super(world, x, y, z, fluid);
			this.nextParticle = nextParticle;
		}

		@Override
		protected void updateVelocity() {
			if (this.onGround) {
				this.markDead();
				this.world.addParticle(this.nextParticle, this.x, this.y, this.z, 0.0, 0.0, 0.0);
			}
		}
	}

	@ClientOnly
	static class FallingBlood extends BloodDripParticle.ContinuousFalling {
		FallingBlood(ClientWorld clientWorld, double d, double e, double f, Fluid fluid, ParticleEffect particleEffect) {
			super(clientWorld, d, e, f, fluid, particleEffect);
		}

		@Override
		protected void updateVelocity() {
			if (this.onGround) {
				this.markDead();
				this.world.addParticle(this.nextParticle, this.x, this.y, this.z, 0.0, 0.0, 0.0);
				float f = MathHelper.nextBetween(this.random, 0.3F, 1.0F);
				this.world.playSound(this.x, this.y, this.z, SoundEvents.BLOCK_BEEHIVE_DRIP, SoundCategory.BLOCKS, f, 1.0F, false);
			}
		}
	}

	@ClientOnly
	static class Landing extends BloodDripParticle {
		Landing(ClientWorld clientWorld, double d, double e, double f, Fluid fluid) {
			super(clientWorld, d, e, f, fluid);
			this.maxAge = (int)(16.0 / (Math.random() * 0.8 + 0.2));
		}
	}
}
