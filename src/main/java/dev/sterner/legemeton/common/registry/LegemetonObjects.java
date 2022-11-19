package dev.sterner.legemeton.common.registry;

import dev.sterner.legemeton.common.block.*;
import dev.sterner.legemeton.common.item.AllBlackSwordItem;
import dev.sterner.legemeton.common.item.LegemetonItem;
import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.item.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

import java.util.LinkedHashMap;
import java.util.Map;

public class LegemetonObjects {
	public static final Map<Block, Identifier> BLOCKS = new LinkedHashMap<>();
	public static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

	public static final Item BUTCHER_KNIFE = register("butcher_knife", new AxeItem(ToolMaterials.NETHERITE, 6, -2, settings()));
	public static final Item BLOODY_BUTCHER_KNIFE = register("bloody_butcher_knife", new AxeItem(ToolMaterials.NETHERITE, 6, -2, new Item.Settings()));
	public static final Item LEGEMETON = register("legemeton", new LegemetonItem(Constants.id("legemeton"), settings()));
	public static final Item ALL_BLACK = register("all_black", new AllBlackSwordItem(ToolMaterials.NETHERITE, 8, -2, settings(), true));

	public static final Item CELLAR_KEY = register("cellar_key", new Item(settings()));
	public static final Item CONTRACT = register("contract", new Item(settings()));
	public static final Item SIGNED_CONTRACT = register("signed_contract", new Item(settings()));
	public static final Item PACKET = register("packet", new Item(settings()));
	public static final Item CAGE = register("cage", new Item(settings()));
	public static final Item HOOK = register("hook", new Item(settings()));
	public static final Item METAL_HOOK = register("metal_hook", new Item(settings()));
	public static final Block ROPE = register("rope", new RopeBlock(QuiltBlockSettings.of(Material.WOOL).strength(0.2F)), settings(), true);

	public static final Item FLESH = register("flesh", new Item(settings().food(FoodComponents.COOKED_CHICKEN)));
	public static final Item SLICED_FLESH = register("sliced_flesh", new Item(settings().food(FoodComponents.PORKCHOP)));
	public static final Item COOKED_SLICED_FLESH = register("cooked_sliced_flesh", new Item(settings().food(FoodComponents.COOKED_PORKCHOP)));
	public static final Item FAT = register("fat", new Item(settings()));
	public static final Item SKIN = register("skin", new Item(settings()));
	public static final Item BOTTLE_OF_BLOOD = register("bottle_of_blood", new Item(settings()));

	public static final Item QUICKSILVER = register("quicksilver", new Item(settings()));
	public static final Item SOUL_STONE = register("soul_stone", new Item(settings()));
	public static final Item CINNABAR = register("cinnabar", new Item(settings()));
	public static final Item EMERALD_TABLET = register("emerald_tablet", new Item(settings()));

	public static final Block HOOK_BLOCK = register("hook_block", new HookBlock(QuiltBlockSettings.of(Material.WOOL).strength(0.2F)), settings(), false);
	public static final Block JAR = register("jar", new JarBlock(QuiltBlockSettings.of(Material.GLASS).strength(0.3F).sounds(BlockSoundGroup.GLASS)), settings(),true);
	public static final Block NECRO_TABLE = register("necro", new NecroTableBlock(QuiltBlockSettings.copy(Blocks.DEEPSLATE)), settings(),false);
	public static final Block REINFORCED_DOOR = register("reinforced_door", new ReinforcedDoorBlock(QuiltBlockSettings.copyOf(Blocks.OAK_DOOR)),settings(), true);

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
