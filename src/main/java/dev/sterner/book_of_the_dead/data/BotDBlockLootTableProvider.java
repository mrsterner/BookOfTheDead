package dev.sterner.book_of_the_dead.data;

import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootTable;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class BotDBlockLootTableProvider extends FabricBlockLootTableProvider {
	protected BotDBlockLootTableProvider(FabricDataOutput dataOutput) {
		super(dataOutput);
	}

	@Override
	public void generate() {
		addDrop(BotDObjects.HOOK_BLOCK, () -> BotDObjects.HOOK);
		addDrop(BotDObjects.METAL_HOOK_BLOCK, () -> BotDObjects.METAL_HOOK);
		addDrop(BotDObjects.ROPE);
		addDrop(BotDObjects.PEDESTAL, Blocks.DEEPSLATE_TILE_WALL);
		addDrop(BotDObjects.RITUAL, Blocks.DEEPSLATE_TILE_SLAB);
	}

	@Override
	public void accept(BiConsumer<Identifier, LootTable.Builder> identifierBuilderBiConsumer) {

	}
}
