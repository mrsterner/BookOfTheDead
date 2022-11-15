package dev.sterner.legemeton.mixin;

import dev.sterner.legemeton.api.interfaces.IBlockLeakParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(targets = "net/minecraft/client/particle/BlockLeakParticle$Dripping")
public abstract class DrippingParticleMixin extends Particle implements IBlockLeakParticle {
	@Unique
	private ParticleEffect LegemetonNextParticle;

	protected DrippingParticleMixin(ClientWorld clientWorld, double d, double e, double f) {
		super(clientWorld, d, e, f);
	}

	@Inject(method = "updateAge()V", at = @At("HEAD"), cancellable = true)
	private void legemeton$spawnCustomNextParticle(CallbackInfo ci) {
		if (LegemetonNextParticle != null && this.maxAge - 1 <= 0) {
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
