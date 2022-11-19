package dev.sterner.legemeton.common.registry;

import dev.sterner.legemeton.common.util.Constants;
import dev.sterner.legemeton.common.world.OldHouse;
import net.minecraft.structure.StructureType;
import net.minecraft.util.registry.Registry;

public class LegemetonWorldGenerators {
	public static StructureType<OldHouse> OLD_HOUSE;


	public static void init() {
		OLD_HOUSE = Registry.register(Registry.STRUCTURE_TYPE, Constants.id("old_house"), () -> OldHouse.CODEC);
	}
}
