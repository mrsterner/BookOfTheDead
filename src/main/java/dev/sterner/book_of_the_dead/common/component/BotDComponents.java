package dev.sterner.book_of_the_dead.common.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class BotDComponents implements EntityComponentInitializer {
	public static final ComponentKey<CorpseDataComponent> CORPSE_COMPONENT = ComponentRegistry.getOrCreate(Constants.id("corpse"), CorpseDataComponent.class);
	public static final ComponentKey<PlayerDataComponent> PLAYER_COMPONENT = ComponentRegistry.getOrCreate(Constants.id("player"), PlayerDataComponent.class);
	public static final ComponentKey<LivingEntityDataComponent> LIVING_COMPONENT = ComponentRegistry.getOrCreate(Constants.id("living"), LivingEntityDataComponent.class);
	public static final ComponentKey<PlayerKnowledgeComponent> KNOWLEDGE_COMPONENT = ComponentRegistry.getOrCreate(Constants.id("knowledge"), PlayerKnowledgeComponent.class);
	public static final ComponentKey<PlayerSanityComponent> SANITY_COMPONENT = ComponentRegistry.getOrCreate(Constants.id("eye"), PlayerSanityComponent.class);
	public static final ComponentKey<PlayerAdviceComponent> ADVICE_COMPONENT = ComponentRegistry.getOrCreate(Constants.id("advice"), PlayerAdviceComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.beginRegistration(LivingEntity.class, CORPSE_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(CorpseDataComponent::new);
		registry.beginRegistration(PlayerEntity.class, PLAYER_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(PlayerDataComponent::new);
		registry.beginRegistration(LivingEntity.class, LIVING_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(LivingEntityDataComponent::new);
		registry.beginRegistration(PlayerEntity.class, KNOWLEDGE_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(PlayerKnowledgeComponent::new);
		registry.beginRegistration(PlayerEntity.class, SANITY_COMPONENT).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(PlayerSanityComponent::new);
		registry.beginRegistration(PlayerEntity.class, ADVICE_COMPONENT).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(PlayerAdviceComponent::new);
	}
}
