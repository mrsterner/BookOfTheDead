package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.common.entity.BloodSlimeEntity;
import dev.sterner.book_of_the_dead.common.entity.OldManEntity;
import dev.sterner.book_of_the_dead.common.entity.PlayerCorpseEntity;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.entity.api.QuiltEntityTypeBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

public interface BotDEntityTypes {
    Map<EntityType<?>, Identifier> ENTITY_TYPES = new LinkedHashMap<>();

	EntityType<OldManEntity> OLD_MAN_ENTITY =
			register(
					"old_man",
					QuiltEntityTypeBuilder.<OldManEntity>create()
							.spawnGroup(SpawnGroup.AMBIENT)
							.spawnableFarFromPlayer()
							.entityFactory(OldManEntity::new)
							.setDimensions(EntityDimensions.fixed(0.6F, 1.95F))
							.build());

	EntityType<BloodSlimeEntity> BLOOD_SLIME_ENTITY =
			register(
					"blood_slime",
					QuiltEntityTypeBuilder.<BloodSlimeEntity>create()
							.spawnGroup(SpawnGroup.MONSTER)
							.spawnableFarFromPlayer()
							.entityFactory(BloodSlimeEntity::new)
							.setDimensions(EntityDimensions.fixed(0.8F, 0.8F))
							.build());

	EntityType<PlayerCorpseEntity> PLAYER_CORPSE_ENTITY =
			register(
					"corpse_player",
					QuiltEntityTypeBuilder.<PlayerCorpseEntity>create()
							.spawnGroup(SpawnGroup.MISC)
							.entityFactory(PlayerCorpseEntity::new)
							.setDimensions(EntityDimensions.fixed(0.6F, 1.85F))
							.build());


	static <T extends Entity> EntityType<T> register(String name, EntityType<T> type) {
		ENTITY_TYPES.put(type, Constants.id(name));
		return type;
	}

	static void init() {
		DefaultAttributeRegistry.DEFAULT_ATTRIBUTE_REGISTRY.put(OLD_MAN_ENTITY, OldManEntity.createAttributes().build());
		DefaultAttributeRegistry.DEFAULT_ATTRIBUTE_REGISTRY.put(BLOOD_SLIME_ENTITY, BloodSlimeEntity.createMobAttributes().build());
		DefaultAttributeRegistry.DEFAULT_ATTRIBUTE_REGISTRY.put(PLAYER_CORPSE_ENTITY, PlayerCorpseEntity.createAttributes().build());

		ENTITY_TYPES.keySet().forEach(entityType -> Registry.register(Registries.ENTITY_TYPE, ENTITY_TYPES.get(entityType), entityType));
	}
}
