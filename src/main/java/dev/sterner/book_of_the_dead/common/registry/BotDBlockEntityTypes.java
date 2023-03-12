package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.common.block.entity.*;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

public class BotDBlockEntityTypes {
	private static final Map<BlockEntityType<?>, Identifier> BLOCK_ENTITY_TYPES = new LinkedHashMap<>();

	public static final BlockEntityType<HookBlockEntity> HOOK = register("hook",
			QuiltBlockEntityTypeBuilder.create(HookBlockEntity::new, BotDObjects.HOOK_BLOCK, BotDObjects.METAL_HOOK_BLOCK).build(null));

	public static final BlockEntityType<JarBlockEntity> JAR = register("jar",
			QuiltBlockEntityTypeBuilder.create(JarBlockEntity::new, BotDObjects.JAR).build(null));

	public static final BlockEntityType<NecroTableBlockEntity> NECRO = register("necro",
			QuiltBlockEntityTypeBuilder.create(NecroTableBlockEntity::new, BotDObjects.NECRO_TABLE).build(null));

	public static final BlockEntityType<ButcherTableBlockEntity> BUTCHER = register("butcher",
			QuiltBlockEntityTypeBuilder.create(ButcherTableBlockEntity::new, BotDObjects.BUTCHER_TABLE).build(null));

	public static final BlockEntityType<RitualBlockEntity> RITUAL = register("ritual",
			QuiltBlockEntityTypeBuilder.create(RitualBlockEntity::new, BotDObjects.RITUAL).build(null));

	public static final BlockEntityType<PedestalBlockEntity> PEDESTAL = register("pedestal",
			QuiltBlockEntityTypeBuilder.create(PedestalBlockEntity::new, BotDObjects.PEDESTAL).build());

	public static final BlockEntityType<RetortFlaskBlockEntity> RETORT = register("retort",
			QuiltBlockEntityTypeBuilder.create(RetortFlaskBlockEntity::new, BotDObjects.RETORT_FLASK_BLOCK).build());


	private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
		BLOCK_ENTITY_TYPES.put(type, Constants.id(name));
		return type;
	}



	public static void init() {
		BLOCK_ENTITY_TYPES.keySet().forEach(blockEntityType -> Registry.register(Registry.BLOCK_ENTITY_TYPE, BLOCK_ENTITY_TYPES.get(blockEntityType), blockEntityType));
	}
}
