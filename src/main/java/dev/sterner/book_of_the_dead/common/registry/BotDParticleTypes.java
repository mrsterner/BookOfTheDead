package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.client.particle.*;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public interface BotDParticleTypes {
	Map<ParticleType<?>, Identifier> PARTICLE_TYPES = new LinkedHashMap<>();

	DefaultParticleType HANGING_BLOOD = Registry.register(Registries.PARTICLE_TYPE, Constants.id("hanging_blood"), FabricParticleTypes.simple());
	DefaultParticleType FALLING_BLOOD = Registry.register(Registries.PARTICLE_TYPE, Constants.id("falling_blood"), FabricParticleTypes.simple());
	DefaultParticleType LANDING_BLOOD = Registry.register(Registries.PARTICLE_TYPE, Constants.id("landing_blood"), FabricParticleTypes.simple());
	DefaultParticleType SPLASHING_BLOOD = Registry.register(Registries.PARTICLE_TYPE, Constants.id("splashing_blood"), FabricParticleTypes.simple());

	ParticleType<ItemStackBeamParticleEffect> ITEM_BEAM_PARTICLE = register("item_beam_particle", FabricParticleTypes.complex(ItemStackBeamParticleEffect.PARAMETERS_FACTORY));
	ParticleType<DefaultParticleType> SOAP_BUBBLE = register("soap_bubble", FabricParticleTypes.simple());

	ParticleType<SoulSpiralParticleEffect> SOUL_SPIRAL = register("soul_spiral", FabricParticleTypes.complex(SoulSpiralParticleEffect.PARAMETERS_FACTORY));
	ParticleType<OrbitParticleEffect> SOUL_ORBIT = register("soul_orbit", FabricParticleTypes.complex(OrbitParticleEffect.PARAMETERS_FACTORY));

	static <T extends ParticleEffect> ParticleType<T> register(String name, ParticleType<T> type) {
		PARTICLE_TYPES.put(type, Constants.id(name));
		return type;
	}

	static void init() {
		PARTICLE_TYPES.keySet().forEach(particleType -> Registry.register(Registries.PARTICLE_TYPE, PARTICLE_TYPES.get(particleType), particleType));
		ParticleFactoryRegistry.getInstance().register(ITEM_BEAM_PARTICLE, new ItemStackBeamParticle.ItemFactory());
		ParticleFactoryRegistry.getInstance().register(SPLASHING_BLOOD, BloodSplashParticle.DefaultFactory::new);
		ParticleFactoryRegistry.getInstance().register(SOAP_BUBBLE, SoapBubbleParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(LANDING_BLOOD, BloodDripParticle::createBloodLandParticle);
		ParticleFactoryRegistry.getInstance().register(FALLING_BLOOD, BloodDripParticle::createBloodFallParticle);
		ParticleFactoryRegistry.getInstance().register(HANGING_BLOOD, BloodDripParticle::createBloodHangParticle);
		ParticleFactoryRegistry.getInstance().register(SOUL_SPIRAL, SoulSpiralParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SOUL_ORBIT, OrbitParticle.Factory::new);

	}
}
