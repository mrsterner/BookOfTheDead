package dev.sterner.book_of_the_dead.data;

import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class BotDBlockLootTableProvider extends FabricBlockLootTableProvider {
	protected BotDBlockLootTableProvider(FabricDataOutput dataOutput) {
		super(dataOutput);
	}

	@Override
	public void generate() {
		this.addDrop(BotDObjects.HOOK_BLOCK, BotDObjects.HOOK);
		this.addDrop(BotDObjects.METAL_HOOK_BLOCK, BotDObjects.METAL_HOOK);
		this.addDrop(BotDObjects.ROPE);
		this.addDrop(BotDObjects.PEDESTAL, Blocks.DEEPSLATE_TILE_WALL);
		this.addDrop(BotDObjects.RITUAL, Blocks.DEEPSLATE_TILE_SLAB);
		this.add(BotDObjects.SULFUR, dropWithoutSilkTouch(BotDObjects.SULFUR.asItem(), 1, 2));
	}

	@Override
	public void accept(BiConsumer<Identifier, LootTable.Builder> identifierBuilderBiConsumer) {
		this.generate(identifierBuilderBiConsumer);
	}

	public LootTable.Builder dropWithoutSilkTouch(Item item, float min, float max) {
		return drop(ItemEntry.builder(item).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(min, max))).apply(ApplyBonusLootFunction.uniformBonusCount(Enchantments.FORTUNE)));
	}

	public static LootTable.Builder drop(LootPoolEntry.Builder<?> alternative) {
		return LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(alternative));
	}
}
