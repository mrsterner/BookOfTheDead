package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.common.util.Constants;
import dev.sterner.book_of_the_dead.common.world.OldHouse;
import net.minecraft.structure.StructureType;
import net.minecraft.util.registry.Registry;

public class BotDWorldGenerators {
	public static StructureType<OldHouse> OLD_HOUSE;


	public static void init() {
		OLD_HOUSE = Registry.register(Registry.STRUCTURE_TYPE, Constants.id("old_house"), () -> OldHouse.CODEC);
	}
}
