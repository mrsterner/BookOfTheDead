package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.common.entity.*;
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

	EntityType<KakuzuEntity> KAKUZU_ENTITY =
		register(
			"kakuzu",
			QuiltEntityTypeBuilder.<KakuzuEntity>create()
				.spawnGroup(SpawnGroup.MISC)
				.entityFactory(KakuzuEntity::new)
				.setDimensions(EntityDimensions.fixed(0.25F, 0.5F))
				.build());

	EntityType<FloatingItemEntity> FLOATING_ITEM_ENTITY =
		register(
			"floating_item",
			QuiltEntityTypeBuilder.<FloatingItemEntity>create()
				.spawnGroup(SpawnGroup.MISC)
				.entityFactory(FloatingItemEntity::new)
				.setDimensions(EntityDimensions.fixed(0.5F, 0.75F))
				.build());

	EntityType<BloodParticleEntity> BLOOD_PARTICLE_ENTITY =
		register(
			"blood_particle",
			QuiltEntityTypeBuilder.<BloodParticleEntity>create()
				.spawnGroup(SpawnGroup.MISC)
				.entityFactory(BloodParticleEntity::new)
				.setDimensions(EntityDimensions.fixed(0.1F, 0.1F))
				.build());


	static <T extends Entity> EntityType<T> register(String name, EntityType<T> type) {
		ENTITY_TYPES.put(type, Constants.id(name));
		return type;
	}

	static void init() {
		DefaultAttributeRegistry.DEFAULT_ATTRIBUTE_REGISTRY.put(OLD_MAN_ENTITY, OldManEntity.createAttributes().build());
		DefaultAttributeRegistry.DEFAULT_ATTRIBUTE_REGISTRY.put(BLOOD_SLIME_ENTITY, BloodSlimeEntity.createMobAttributes().build());
		DefaultAttributeRegistry.DEFAULT_ATTRIBUTE_REGISTRY.put(PLAYER_CORPSE_ENTITY, PlayerCorpseEntity.createAttributes().build());
		DefaultAttributeRegistry.DEFAULT_ATTRIBUTE_REGISTRY.put(KAKUZU_ENTITY, KakuzuEntity.createAttributes().build());

		ENTITY_TYPES.keySet().forEach(entityType -> Registry.register(Registries.ENTITY_TYPE, ENTITY_TYPES.get(entityType), entityType));
	}
}
