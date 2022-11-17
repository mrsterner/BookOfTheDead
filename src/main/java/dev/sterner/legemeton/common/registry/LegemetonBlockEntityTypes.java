package dev.sterner.legemeton.common.registry;

import dev.sterner.legemeton.common.block.entity.HookBlockEntity;
import dev.sterner.legemeton.common.block.entity.JarBlockEntity;
import dev.sterner.legemeton.common.block.entity.NecroTableBlockEntity;
import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

public class LegemetonBlockEntityTypes {
	private static final Map<BlockEntityType<?>, Identifier> BLOCK_ENTITY_TYPES = new LinkedHashMap<>();

	public static final BlockEntityType<HookBlockEntity> HOOK = register("hook",
			QuiltBlockEntityTypeBuilder.create(HookBlockEntity::new, LegemetonObjects.HOOK_BLOCK).build(null));

	public static final BlockEntityType<JarBlockEntity> JAR = register("jar",
			QuiltBlockEntityTypeBuilder.create(JarBlockEntity::new, LegemetonObjects.JAR).build(null));

	public static final BlockEntityType<NecroTableBlockEntity> NECRO = register("necro",
			QuiltBlockEntityTypeBuilder.create(NecroTableBlockEntity::new, LegemetonObjects.NECRO_TABLE).build(null));




	private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
		BLOCK_ENTITY_TYPES.put(type, Constants.id(name));
		return type;
	}



	public static void init() {
		BLOCK_ENTITY_TYPES.keySet().forEach(blockEntityType -> Registry.register(Registry.BLOCK_ENTITY_TYPE, BLOCK_ENTITY_TYPES.get(blockEntityType), blockEntityType));
	}
}
