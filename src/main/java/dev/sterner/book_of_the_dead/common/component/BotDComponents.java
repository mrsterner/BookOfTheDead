package dev.sterner.book_of_the_dead.common.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;

public class BotDComponents implements EntityComponentInitializer {
	public static final ComponentKey<CorpseDataComponent> CORPSE_COMPONENT = ComponentRegistry.getOrCreate(Constants.id("corpse"), CorpseDataComponent.class);


	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.beginRegistration(MobEntity.class, CORPSE_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(CorpseDataComponent::new);
	}
}
