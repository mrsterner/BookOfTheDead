package dev.sterner.legemeton.mixin;

import dev.sterner.legemeton.api.interfaces.IBlockLeakParticle;
import net.minecraft.client.particle.BlockLeakParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockLeakParticle.ContinuousFalling.class)
public abstract class ContinuousFallingParticleMixin extends Particle implements IBlockLeakParticle {
	@Unique
	private ParticleEffect LegemetonNextParticle;

	protected ContinuousFallingParticleMixin(ClientWorld clientWorld, double d, double e, double f) {
		super(clientWorld, d, e, f);
	}

	@Inject(method = "updateVelocity()V", at = @At("HEAD"), cancellable = true)
	private void legemeton$spawnCustomNextParticle(CallbackInfo ci) {
		if (LegemetonNextParticle != null && this.onGround) {
			this.markDead();
			this.world.addParticle(this.LegemetonNextParticle, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
			ci.cancel();
		}
	}

	@Override
	public void setNextParticle(ParticleEffect effect) {
		LegemetonNextParticle = effect;
	}


}
