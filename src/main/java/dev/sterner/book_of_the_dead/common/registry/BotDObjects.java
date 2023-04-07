package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.BotD;
import dev.sterner.book_of_the_dead.common.block.*;
import dev.sterner.book_of_the_dead.common.item.*;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

import java.util.LinkedHashMap;
import java.util.Map;

public interface BotDObjects {
	Map<Block, Identifier> BLOCKS = new LinkedHashMap<>();
	Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

	Material SULFUR_MATERIAL = new Material.Builder(MapColor.PALE_YELLOW).build();

	Item DEBUG_WAND = register("debug_wand", new DebugWandItem(settings()));

	Item PAPER_AND_QUILL = register("paper_and_quill", new Item(settings().maxCount(1)));
	Item CARPENTER_TOOLS = register("carpenter_tools", new Item(settings().maxCount(1).maxDamage(32)));

	Item MEAT_CLEAVER = register("meat_cleaver", new AxeItem(ToolMaterials.NETHERITE, 6, -2, settings()));
	Item BOOK_OF_THE_DEAD = register("book_of_the_dead", new BookOfTheDeadItem(Constants.id("book_of_the_dead"), settings()));
	Item ALL_BLACK = register("all_black", new AllBlackSwordItem(ToolMaterials.NETHERITE, 8, -2, settings()));

	Item SYRINGE = register("syringe", new SyringeItem(settings()));





	Item SULFURIC_ACID = register("sulfuric_acid", new Item(settings()));
	Item PHILOSOPHERS_STONE = register("philosophers_stone", new Item(settings()));

	Item MORPHINE = register("morphine", new StatusEffectItem(settings(), BotDStatusEffects.MORPHINE));
	Item EUTHANASIA = register("euthanasia", new StatusEffectItem(settings(), BotDStatusEffects.EUTHANASIA));
	Item ADRENALINE = register("adrenaline", new StatusEffectItem(settings(), BotDStatusEffects.ADRENALINE));

	Item SKELETON_HELMET = register("skeleton_helmet", new SkeletonArmorItem(ArmorItem.ArmorSlot.HELMET, settings()));
	Item SKELETON_CHESTPLATE = register("skeleton_chestplate", new SkeletonArmorItem(ArmorItem.ArmorSlot.CHESTPLATE, settings()));
	Item SKELETON_LEGGINGS = register("skeleton_leggings", new SkeletonArmorItem(ArmorItem.ArmorSlot.LEGGINGS, settings()));
	Item SKELETON_BOOTS = register("skeleton_boots", new SkeletonArmorItem(ArmorItem.ArmorSlot.BOOTS, settings()));

	Item WITHER_HELMET = register("wither_helmet", new WitherArmorItem(ArmorItem.ArmorSlot.HELMET, settings()));
	Item WITHER_CHESTPLATE = register("wither_chestplate", new WitherArmorItem(ArmorItem.ArmorSlot.CHESTPLATE, settings()));
	Item WITHER_LEGGINGS = register("wither_leggings", new WitherArmorItem(ArmorItem.ArmorSlot.LEGGINGS, settings()));
	Item WITHER_BOOTS = register("wither_boots", new WitherArmorItem(ArmorItem.ArmorSlot.BOOTS, settings()));

	Item SOAP = register("soap", new SoapItem(settings()));

	Item CELLAR_KEY = register("cellar_key", new CellarKeyItem(settings()));
	Item CONTRACT = register("contract", new ContractItem(settings()));
	Item PACKET = register("packet", new Item(settings()));
	Item CAGE = register("cage", new CageItem(settings()));
	Item HOOK = register("hook", new Item(settings()));
	Item METAL_HOOK = register("metal_hook", new Item(settings()));
	Item OLD_LETTER = register("old_letter", new OldLetterItem(settings()));
	Block ROPE = register("rope", new RopeBlock(QuiltBlockSettings.of(Material.WOOL).strength(0.2F)), settings(), true);

	Item FLESH = register("flesh", new Item(settings().food(FoodComponents.PORKCHOP)));
	Item COOKED_FLESH = register("cooked_flesh", new Item(settings().food(FoodComponents.COOKED_PORKCHOP)));
	Item FAT = register("fat", new Item(settings()));
	Item SKIN = register("skin", new Item(settings()));
	Item EYE = register("eye", new Item(settings()));
	Item BOTTLE_OF_BLOOD = register("bottle_of_blood", new Item(settings()));

	Block VILLAGER_WALL_HEAD = register("villager_wall_head", new BotDWallSkullBlock(BotDSkullBlock.Type.VILLAGER, QuiltBlockSettings.copyOf(Blocks.ZOMBIE_HEAD)), settings(), false);
	Block VILLAGER_HEAD = registerWallStanding("villager_head", new BotDSkullBlock(BotDSkullBlock.Type.VILLAGER, QuiltBlockSettings.copyOf(Blocks.ZOMBIE_HEAD)), VILLAGER_WALL_HEAD, settings(), true);

	Block CANDLE_WALL = register("candle_wall", new CandleWallBlock(QuiltBlockSettings.of(Material.DECORATION)), settings(), false);
	Block CANDLE = registerWallStanding("candle", new CandleBlock(QuiltBlockSettings.of(Material.DECORATION)), CANDLE_WALL, settings(), true);

	Block BRAIN = register("brain", new BrainBlock(QuiltBlockSettings.of(Material.SOLID_ORGANIC)), settings(), true);
	Item HEART = register("heart", new Item(settings()));

	Block RETORT_FLASK_BLOCK = register("retort_flask_block", new RetortFlaskBlock(QuiltBlockSettings.of(Material.GLASS)), settings(), false);
	Item RETORT_FLASK = register("retort_flask", new BlockItem(RETORT_FLASK_BLOCK, settings()));
	Item EMERALD_TABLET = register("emerald_tablet", new Item(settings()));

	Item POPPY_POD = register("poppy_pod", new Item(settings()));
	Block POPPY_CROP = register("poppy_crop", new PoppyCropBlock(QuiltBlockSettings.copy(Blocks.WHEAT)), settings(), false);
	Item POPPY_SEEDS = register("poppy_seeds", new AliasedBlockItem(POPPY_CROP, settings()));

	Block SULFUR_PILE = register("sulfur_pile", new SulfurLayerBlock(QuiltBlockSettings.of(SULFUR_MATERIAL).requiresTool()), settings(), false);
	Item SULFUR = register("sulfur", new Item(settings()));

	Block HOOK_BLOCK = register("hook_block", new HookBlock(QuiltBlockSettings.of(Material.WOOL).strength(0.2F), false), settings(), false);
	Block METAL_HOOK_BLOCK = register("metal_hook_block", new HookBlock(QuiltBlockSettings.of(Material.WOOL).strength(0.2F), true), settings(), false);
	Block JAR = register("jar", new JarBlock(QuiltBlockSettings.of(Material.GLASS).strength(0.3F).sounds(BlockSoundGroup.GLASS)), settings(), true);
	Block NECRO_TABLE = register("necro", new NecroTableBlock(QuiltBlockSettings.copy(Blocks.DEEPSLATE)), settings(), false);
	Block BUTCHER_TABLE = register("butcher", new ButcherBlock(QuiltBlockSettings.copy(Blocks.DARK_OAK_PLANKS)), settings(), false);
	Block PEDESTAL = register("pedestal", new PedestalBlock(QuiltBlockSettings.copy(Blocks.DEEPSLATE_BRICKS)), settings(), true);
	Block REINFORCED_DOOR = register("reinforced_door", new ReinforcedDoorBlock(QuiltBlockSettings.copyOf(Blocks.OAK_DOOR)), settings(), BotD.isDebugMode());
	Block REINFORCED_BLOCK = register("reinforced_block", new ReinforcedBlock(QuiltBlockSettings.copyOf(Blocks.REINFORCED_DEEPSLATE)), settings(), BotD.isDebugMode());

	static Item.Settings settings() {
		return new Item.Settings();
	}

	static <T extends Block> T registerWallStanding(String name, T block, T wall, Item.Settings settings, boolean createItem) {
		BLOCKS.put(block, Constants.id(name));
		if (createItem) {
			ITEMS.put(new WallStandingBlockItem(block, wall, settings, Direction.DOWN), BLOCKS.get(block));
		}
		return block;
	}

	static <T extends Item> T register(String name, T item) {
		ITEMS.put(item, Constants.id(name));
		return item;
	}

	static <T extends Block> T register(String name, T block, Item.Settings settings, boolean createItem) {
		BLOCKS.put(block, Constants.id(name));
		if (createItem) {
			ITEMS.put(new BlockItem(block, settings), BLOCKS.get(block));
		}
		return block;
	}

	static void init() {
		BLOCKS.keySet().forEach(block -> Registry.register(Registries.BLOCK, BLOCKS.get(block), block));
		ITEMS.keySet().forEach(item -> Registry.register(Registries.ITEM, ITEMS.get(item), item));
	}
}
