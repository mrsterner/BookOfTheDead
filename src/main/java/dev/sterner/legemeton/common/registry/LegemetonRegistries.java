package dev.sterner.legemeton.common.registry;

import dev.sterner.legemeton.api.NecrotableRitual;
import dev.sterner.legemeton.common.block.entity.NecroTableBlockEntity;
import dev.sterner.legemeton.common.util.Constants;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.registry.Registry;

import java.util.Objects;
import java.util.stream.Collectors;

public class LegemetonRegistries {
	public static final Registry<NecrotableRitual> NECROTABLE_RITUALS = FabricRegistryBuilder.createSimple(NecrotableRitual.class, Constants.id("necro_rituals")).buildAndRegister();


}
