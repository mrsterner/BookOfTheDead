package dev.sterner.book_of_the_dead.common.event;

import dev.sterner.book_of_the_dead.common.util.Constants;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

import static dev.sterner.book_of_the_dead.common.registry.BotDObjects.*;

public class BotDItemGroupEvents {

	public static void init() {
		ItemGroupEvents.modifyEntriesEvent(Constants.BOTD_GROUP).register(BotDItemGroupEvents::mainGroup);
	}

	private static void mainGroup(FabricItemGroupEntries e) {
		e.addItem(DEBUG_WAND);
		e.addItem(BOOK_OF_THE_DEAD);
		e.addItem(MEAT_CLEAVER);
		e.addItem(PAPER_AND_QUILL);
		e.addItem(CARPENTER_TOOLS);
		e.addItem(CELLAR_KEY);
		e.addItem(CONTRACT);
		e.addItem(OLD_LETTER);
		e.addItem(PACKET);
		e.addItem(CAGE);
		e.addItem(HOOK);
		e.addItem(METAL_HOOK);
		e.addItem(ROPE);
		e.addItem(RETORT_FLASK);
		e.addItem(SULFUR);
		e.addItem(SULFURIC_ACID);
		e.addItem(SYRINGE);
		e.addItem(SOAP);
		e.addItem(ALL_BLACK);
		e.addItem(SKELETON_HELMET);
		e.addItem(SKELETON_CHESTPLATE);
		e.addItem(SKELETON_LEGGINGS);
		e.addItem(SKELETON_BOOTS);
		e.addItem(WITHER_HELMET);
		e.addItem(WITHER_CHESTPLATE);
		e.addItem(WITHER_LEGGINGS);
		e.addItem(WITHER_BOOTS);
		e.addItem(EMERALD_TABLET);
		e.addItem(PHILOSOPHERS_STONE);
		e.addItem(POPPY_POD);
		e.addItem(POPPY_SEEDS);
		e.addItem(FLESH);
		e.addItem(COOKED_FLESH);
		e.addItem(FAT);
		e.addItem(SKIN);
		e.addItem(BRAIN);
		e.addItem(HEART);
		e.addItem(EYE);
		e.addItem(BOTTLE_OF_BLOOD);
		e.addItem(VILLAGER_HEAD);
		e.addItem(REINFORCED_DOOR);
		e.addItem(JAR);
		e.addItem(PEDESTAL);
		e.addItem(REINFORCED_BLOCK);
	}
}
