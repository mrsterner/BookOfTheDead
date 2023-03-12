package dev.sterner.book_of_the_dead.data;

import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Blocks;

public class BotDBlockLootTableProvider extends FabricBlockLootTableProvider {
	protected BotDBlockLootTableProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	protected void generateBlockLootTables() {
		addDrop(BotDObjects.HOOK_BLOCK, () -> BotDObjects.HOOK);
		addDrop(BotDObjects.METAL_HOOK_BLOCK, () -> BotDObjects.METAL_HOOK);
		addDrop(BotDObjects.ROPE);
		addDrop(BotDObjects.PEDESTAL, Blocks.DEEPSLATE_TILE_WALL);
		addDrop(BotDObjects.RITUAL, Blocks.DEEPSLATE_TILE_SLAB);
	}
}
