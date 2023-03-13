package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.BotD;
import dev.sterner.book_of_the_dead.common.block.*;
import dev.sterner.book_of_the_dead.common.item.AllBlackSwordItem;
import dev.sterner.book_of_the_dead.common.item.BotDItem;
import dev.sterner.book_of_the_dead.common.item.DebugWandItem;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BotDObjects {
	public static final Map<Block, Identifier> BLOCKS = new LinkedHashMap<>();
	public static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

	public static final Item DEBUG_WAND = register("debug_wand", new DebugWandItem(settings()));

	public static final Item PAPER_AND_QUILL = register("paper_and_quill", new Item(settings().maxCount(1)));
	public static final Item CARPENTER_TOOLS = register("carpenter_tools", new Item(settings().maxCount(1).maxDamage(32)));

	public static final Item BUTCHER_KNIFE = register("butcher_knife", new AxeItem(ToolMaterials.NETHERITE, 6, -2, settings()));
	public static final Item BLOODY_BUTCHER_KNIFE = register("bloody_butcher_knife", new AxeItem(ToolMaterials.NETHERITE, 6, -2, new Item.Settings()));
	public static final Item BOOK_OF_THE_DEAD = register("book_of_the_dead", new BotDItem(Constants.id("book_of_the_dead"), settings()));
	public static final Item ALL_BLACK = register("all_black", new AllBlackSwordItem(ToolMaterials.NETHERITE, 8, -2, settings(), true));

	public static final Item CELLAR_KEY = register("cellar_key", new Item(settings()));
	public static final Item CONTRACT = register("contract", new Item(settings()));
	public static final Item SIGNED_CONTRACT = register("signed_contract", new Item(settings()));
	public static final Item PACKET = register("packet", new Item(settings()));
	public static final Item CAGE = register("cage", new Item(settings()));
	public static final Item HOOK = register("hook", new Item(settings()));
	public static final Item METAL_HOOK = register("metal_hook", new Item(settings()));
	public static final Item OLD_LETTER = register("old_letter", new Item(settings()){
		@Override
		public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
			tooltip.add(Text.translatable("tooltip.book_of_the_dead.from_archive"));
			tooltip.add(Text.translatable("tooltip.book_of_the_dead.old_friend").formatted(Formatting.ITALIC));
			super.appendTooltip(stack, world, tooltip, context);
		}
	});
	public static final Block ROPE = register("rope", new RopeBlock(QuiltBlockSettings.of(Material.WOOL).strength(0.2F)), settings(), true);

	public static final Item FLESH = register("flesh", new Item(settings().food(FoodComponents.PORKCHOP)));
	public static final Item COOKED_FLESH = register("cooked_flesh", new Item(settings().food(FoodComponents.COOKED_PORKCHOP)));
	public static final Item FAT = register("fat", new Item(settings()));
	public static final Item SKIN = register("skin", new Item(settings()));
	public static final Item BOTTLE_OF_BLOOD = register("bottle_of_blood", new Item(settings()));

	public static final Block VILLAGER_HEAD = register("villager_head", new BotDSkullBlock(BotDSkullBlock.Type.VILLAGER, QuiltBlockSettings.copyOf(Blocks.ZOMBIE_HEAD)), settings(), true);
	public static final Block VILLAGER_WALL_HEAD = register("villager_wall_head", new BotDWallSkullBlock(BotDSkullBlock.Type.VILLAGER, QuiltBlockSettings.copyOf(Blocks.ZOMBIE_HEAD).dropsLike(VILLAGER_HEAD)), settings(), false);

	public static final Block RETORT_FLASK_BLOCK = register("retort_flask_block", new RetortFlaskBlock(QuiltBlockSettings.of(Material.GLASS)), settings(), false);
	public static final Item RETORT_FLASK = register("retort_flask", new BlockItem(RETORT_FLASK_BLOCK, settings()));
	//public static final Item QUICKSILVER = register("quicksilver", new Item(settings()));
	//public static final Item SOUL_GEM = register("soul_gem", new Item(settings()));
	//public static final Item CINNABAR = register("cinnabar", new Item(settings()));
	public static final Item EMERALD_TABLET = register("emerald_tablet", new Item(settings()));


	public static final Item POPPY_POD = register("poppy_pod", new Item(settings()));
	public static final Block POPPY_CROP = register("poppy_crop", new PoppyCropBlock(QuiltBlockSettings.copy(Blocks.WHEAT)), settings(), false);
	public static final Item POPPY_SEEDS = register("poppy_seeds", new AliasedBlockItem(POPPY_CROP, settings()));

	public static final Block HOOK_BLOCK = register("hook_block", new HookBlock(QuiltBlockSettings.of(Material.WOOL).strength(0.2F), false), settings(), false);
	public static final Block METAL_HOOK_BLOCK = register("metal_hook_block", new HookBlock(QuiltBlockSettings.of(Material.WOOL).strength(0.2F), true), settings(), false);
	public static final Block JAR = register("jar", new JarBlock(QuiltBlockSettings.of(Material.GLASS).strength(0.3F).sounds(BlockSoundGroup.GLASS)), settings(),true);
	public static final Block NECRO_TABLE = register("necro", new NecroTableBlock(QuiltBlockSettings.copy(Blocks.DEEPSLATE)), settings(),false);
	public static final Block BUTCHER_TABLE = register("butcher", new ButcherBlock(QuiltBlockSettings.copy(Blocks.DARK_OAK_PLANKS)), settings(),false);
	public static final Block RITUAL = register("ritual", new RitualBlock(QuiltBlockSettings.copy(Blocks.DEEPSLATE_BRICKS)), settings(), BotD.isDebugMode());
	public static final Block PEDESTAL = register("pedestal", new PedestalBlock(QuiltBlockSettings.copy(Blocks.DEEPSLATE_BRICKS)), settings(),true);
	public static final Block REINFORCED_DOOR = register("reinforced_door", new ReinforcedDoorBlock(QuiltBlockSettings.copyOf(Blocks.OAK_DOOR)),settings(), BotD.isDebugMode());
	public static final Block REINFORCED_BLOCK = register("reinforced_block", new ReinforcedBlock(QuiltBlockSettings.copyOf(Blocks.REINFORCED_DEEPSLATE)), settings(), BotD.isDebugMode());

	private static Item.Settings settings() {
		return new Item.Settings().group(Constants.BOTD_GROUP);
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
