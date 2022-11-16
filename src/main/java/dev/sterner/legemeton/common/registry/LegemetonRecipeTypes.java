package dev.sterner.legemeton.common.registry;

import dev.sterner.legemeton.common.recipe.ButcheringRecipe;
import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.registry.Registry;

public class LegemetonRecipeTypes {
	public static final RecipeSerializer<ButcheringRecipe> BUTCHERING_RECIPE_RECIPE_SERIALIZER = new ButcheringRecipe.Serializer();
	public static final RecipeType<ButcheringRecipe> BUTCHERING_RECIPE_RECIPE_TYPE = new RecipeType<>() {
		@Override
		public String toString() {
			return Constants.MOD_ID + ":butchering";
		}
	};

	public static void init(){
		Registry.register(Registry.RECIPE_SERIALIZER, Constants.id("butchering"), BUTCHERING_RECIPE_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_TYPE,Constants.id("butchering"), BUTCHERING_RECIPE_RECIPE_TYPE);
	}
}
