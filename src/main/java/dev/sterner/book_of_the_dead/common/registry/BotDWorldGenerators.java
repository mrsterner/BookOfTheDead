package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.common.util.Constants;
import dev.sterner.book_of_the_dead.common.world.structure.OldHouse;
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.StructureType;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.LinkedHashMap;
import java.util.Map;

public interface BotDWorldGenerators {
	Map<StructureType<?>, Identifier> STRUCTURE_TYPES = new LinkedHashMap<>();

	StructureType<OldHouse> OLD_HOUSE = register("old_house", () -> OldHouse.CODEC);


	static <T extends StructureFeature> StructureType<T> register(String name, StructureType<T> type) {
		STRUCTURE_TYPES.put(type, Constants.id(name));
		return type;
	}

	static void init() {
		STRUCTURE_TYPES.keySet().forEach(structureType -> Registry.register(Registries.STRUCTURE_TYPE, STRUCTURE_TYPES.get(structureType), structureType));

		final String[] types = new String[]{"plains"};
		for (String type : types) {
			DynamicRegistrySetupCallback.EVENT.register(registryManager ->
					registryManager.registerEntryAdded(RegistryKeys.STRUCTURE_POOL, ((rawId, id, pool) -> {
						if (id.equals(new Identifier("minecraft", "village/" + type + "/houses"))) {
							pool.elements.add(StructurePoolElement.ofSingle(Constants.MODID + ":village/" + type + "/houses/" + type + "_old_house").apply(StructurePool.Projection.RIGID));
						}
					}))
			);
		}
	}
}
