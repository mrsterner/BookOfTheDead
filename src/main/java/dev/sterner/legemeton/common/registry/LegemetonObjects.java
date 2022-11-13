package dev.sterner.legemeton.common.registry;

import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class LegemetonObjects {
	public static final Map<Block, Identifier> BLOCKS = new LinkedHashMap<>();
	public static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

	public static final Item BUTCHER_KNIFE = register("butcher_knife", new AxeItem(ToolMaterials.NETHERITE, 6, -2, settings()));
	public static final Item LEGEMETON = register("legemeton", new Item(settings()));
	public static final Item CONTRACT = register("contract", new Item(settings()));
	public static final Item SIGNED_CONTRACT = register("signed_contract", new Item(settings()));
	public static final Item PACKET = register("packet", new Item(settings()));
	public static final Item CAGE = register("cage", new Item(settings()));
	public static final Item HOOK = register("hook", new Item(settings()));

	public static final Item FLESH = register("flesh", new Item(settings().food(FoodComponents.COOKED_CHICKEN)));

	public static final Item SLICED_FLESH = register("sliced_flesh", new Item(settings().food(FoodComponents.PORKCHOP)));
	public static final Item COOKED_SLICED_FLESH = register("cooked_sliced_flesh", new Item(settings().food(FoodComponents.COOKED_PORKCHOP)));
	public static final Item FAT = register("fat", new Item(settings()));
	public static final Item SKIN = register("skin", new Item(settings()));
	public static final Item BOTTLE_OF_BLOOD = register("bottle_of_blood", new Item(settings()));


	private static Item.Settings settings() {
		return new Item.Settings().group(Constants.LEGEMETON_GROUP);
	}

	private static <T extends Item> T register(String name, T item) {
		ITEMS.put(item, Constants.id(name));
		return item;
	}

	private static <T extends Block> T register(String name, T block, Item.Settings settings, boolean createItem) {
		BLOCKS.put(block, Constants.id(name));
		if (createItem) {
			ITEMS.put(new BlockItem(block, settings), BLOCKS.get(block));
		}
		return block;
	}

	public static void init() {
		BLOCKS.keySet().forEach(block -> Registry.register(Registry.BLOCK, BLOCKS.get(block), block));
		ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
	}
}
