package dev.sterner.book_of_the_dead.common.event;

import dev.sterner.book_of_the_dead.common.util.Constants;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

import static dev.sterner.book_of_the_dead.common.registry.BotDObjects.*;

public class BotDItemGroupEvents {

	public static void init(){
		ItemGroupEvents.modifyEntriesEvent(Constants.BOTD_GROUP).register(BotDItemGroupEvents::mainGroup);
	}

	private static void mainGroup(FabricItemGroupEntries e) {
		e.addItem(BUTCHER_KNIFE);
	}
}
