package dev.sterner.legemeton.common.registry;

import dev.sterner.legemeton.common.entity.CorpseEntity;
import dev.sterner.legemeton.common.entity.OldManEntity;
import dev.sterner.legemeton.common.util.Constants;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.entity.api.QuiltEntityTypeBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

public class LegemetonEntityTypes {
	private static final Map<EntityType<?>, Identifier> ENTITY_TYPES = new LinkedHashMap<>();


	public static final EntityType<CorpseEntity> CORPSE_ENTITY =
			register(
					"corpse",
					QuiltEntityTypeBuilder.<CorpseEntity>create()
							.spawnGroup(SpawnGroup.MISC)
							.entityFactory(CorpseEntity::new)
							.build());

	public static final EntityType<OldManEntity> OLD_MAN_ENTITY =
			register(
					"old_man",
					QuiltEntityTypeBuilder.<OldManEntity>create()
							.spawnGroup(SpawnGroup.AMBIENT)
							.spawnableFarFromPlayer()
							.entityFactory(OldManEntity::new)
							.setDimensions(EntityDimensions.fixed(0.6F, 1.95F))
							.build());


	private static <T extends Entity> EntityType<T> register(String name, EntityType<T> type) {
		ENTITY_TYPES.put(type, Constants.id(name));
		return type;
	}

	public static void init() {
		FabricDefaultAttributeRegistry.register(CORPSE_ENTITY, CorpseEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(OLD_MAN_ENTITY, OldManEntity.createMobAttributes());

		ENTITY_TYPES.keySet().forEach(entityType -> Registry.register(Registry.ENTITY_TYPE, ENTITY_TYPES.get(entityType), entityType));
	}
}
