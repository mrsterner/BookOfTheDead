package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.api.interfaces.IBlockLeakParticle;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.BlockLeakParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;

public class BotDParticleTypes {
	public static final DefaultParticleType DRIPPING_BLOOD = Registry.register(Registry.PARTICLE_TYPE, Constants.id("dripping_blood"), FabricParticleTypes.simple());
	public static final DefaultParticleType FALLING_BLOOD = Registry.register(Registry.PARTICLE_TYPE, Constants.id("falling_blood"), FabricParticleTypes.simple());
	public static final DefaultParticleType LANDING_BLOOD = Registry.register(Registry.PARTICLE_TYPE,  Constants.id("landing_blood"), FabricParticleTypes.simple());


	public static void init() {
		ParticleFactoryRegistry.getInstance().register(LANDING_BLOOD, s -> new BlockLeakParticle.LandingHoneyFactory(s) {
			@Override
			public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
				BlockLeakParticle r = (BlockLeakParticle) super.createParticle(parameters, world, x, y, z, velocityX, velocityY, velocityZ);
				if (r != null) {
					r.setColor(0.67F, 0.04F, 0.05F);
				}
				return r;
			}
		});
		ParticleFactoryRegistry.getInstance().register(FALLING_BLOOD, s -> new BlockLeakParticle.FallingWaterFactory(s) {
			@Override
			public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
				BlockLeakParticle r = (BlockLeakParticle) super.createParticle(parameters, world, x, y, z, velocityX, velocityY, velocityZ);
				if (r != null) {
					r.setColor(0.67F, 0.04F, 0.05F);
					((IBlockLeakParticle) r).setNextParticle(LANDING_BLOOD);
				}

				return r;
			}
		});
		ParticleFactoryRegistry.getInstance().register(DRIPPING_BLOOD, s -> new BlockLeakParticle.DrippingWaterFactory(s) {
			@Override
			public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
				BlockLeakParticle r = (BlockLeakParticle) super.createParticle(parameters, world, x, y, z, velocityX, velocityY, velocityZ);
				if (r != null) {
					r.setColor(0.62F, 0.0F, 0.1F);
					((IBlockLeakParticle) r).setNextParticle(FALLING_BLOOD);
				}

				return r;
			}
		});
	}
}
