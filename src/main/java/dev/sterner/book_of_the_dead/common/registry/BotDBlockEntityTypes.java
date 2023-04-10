package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.common.block.entity.*;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

public interface BotDBlockEntityTypes {
	Map<BlockEntityType<?>, Identifier> BLOCK_ENTITY_TYPES = new LinkedHashMap<>();

	BlockEntityType<HookBlockEntity> HOOK = register("hook",
		QuiltBlockEntityTypeBuilder.create(HookBlockEntity::new,
			BotDObjects.HOOK_BLOCK,
			BotDObjects.METAL_HOOK_BLOCK
		).build());

	BlockEntityType<JarBlockEntity> JAR = register("jar",
		QuiltBlockEntityTypeBuilder.create(JarBlockEntity::new,
			BotDObjects.JAR
		).build());

	BlockEntityType<NecroTableBlockEntity> NECRO = register("necro",
		QuiltBlockEntityTypeBuilder.create(NecroTableBlockEntity::new,
			BotDObjects.NECRO_TABLE
		).build());

	BlockEntityType<ButcherTableBlockEntity> BUTCHER = register("butcher",
		QuiltBlockEntityTypeBuilder.create(ButcherTableBlockEntity::new,
			BotDObjects.BUTCHER_TABLE
		).build());

	BlockEntityType<PedestalBlockEntity> PEDESTAL = register("pedestal",
		QuiltBlockEntityTypeBuilder.create(PedestalBlockEntity::new,
			BotDObjects.PEDESTAL
		).build());

	BlockEntityType<RetortFlaskBlockEntity> RETORT = register("retort",
		QuiltBlockEntityTypeBuilder.create(RetortFlaskBlockEntity::new,
			BotDObjects.RETORT_FLASK_BLOCK
		).build());

	BlockEntityType<BotDSkullBlockEntity> HEAD = register("head",
		QuiltBlockEntityTypeBuilder.create(BotDSkullBlockEntity::new,
			BotDObjects.VILLAGER_HEAD,
			BotDObjects.VILLAGER_WALL_HEAD
		).build());

	BlockEntityType<BrainBlockEntity> BRAIN = register("brain",
		QuiltBlockEntityTypeBuilder.create(BrainBlockEntity::new,
			BotDObjects.BRAIN
		).build());

	BlockEntityType<BookBlockEntity> BOOK = register("book",
		QuiltBlockEntityTypeBuilder.create(BookBlockEntity::new,
			BotDObjects.BOOK_OF_THE_DEAD
		).build());

	BlockEntityType<TabletBlockEntity> TABLET = register("tablet",
		QuiltBlockEntityTypeBuilder.create(TabletBlockEntity::new,
			BotDObjects.EMERALD_TABLET
		).build());

	static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
		BLOCK_ENTITY_TYPES.put(type, Constants.id(name));
		return type;
	}

	static void init() {
		BLOCK_ENTITY_TYPES.keySet().forEach(blockEntityType -> Registry.register(Registries.BLOCK_ENTITY_TYPE, BLOCK_ENTITY_TYPES.get(blockEntityType), blockEntityType));
	}
}
