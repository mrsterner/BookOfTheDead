package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import dev.sterner.book_of_the_dead.common.recipe.ButcheringRecipe;
import dev.sterner.book_of_the_dead.common.recipe.RitualRecipe;
import dev.sterner.book_of_the_dead.common.util.Constants;
import dev.sterner.book_of_the_dead.common.util.BotDUtils;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.registry.Registry;

import java.util.stream.Collectors;

public class BotDRecipeTypes {
	public static final RecipeSerializer<ButcheringRecipe> BUTCHERING_RECIPE_RECIPE_SERIALIZER = new ButcheringRecipe.Serializer();
	public static final RecipeType<ButcheringRecipe> BUTCHERING_RECIPE_RECIPE_TYPE = new RecipeType<>() {
		@Override
		public String toString() {
			return Constants.MOD_ID + ":butchering";
		}
	};

	public static final RecipeSerializer<RitualRecipe> RITUAL_RECIPE_RECIPE_SERIALIZER = new RitualRecipe.Serializer();
	public static final RecipeType<RitualRecipe> RITUAL_RECIPE_RECIPE_TYPE = new RecipeType<>() {
		@Override
		public String toString() {
			return Constants.MOD_ID + ":ritual";
		}
	};


	public static void init(){
		Registry.register(Registry.RECIPE_SERIALIZER, Constants.id("butchering"), BUTCHERING_RECIPE_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_TYPE,Constants.id("butchering"), BUTCHERING_RECIPE_RECIPE_TYPE);

		Registry.register(Registry.RECIPE_SERIALIZER, Constants.id("ritual"), RITUAL_RECIPE_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_TYPE,Constants.id("ritual"), RITUAL_RECIPE_RECIPE_TYPE);
	}

	public static RitualRecipe getRiteRecipe(NecroTableBlockEntity necroTableBlockEntity) {
		return necroTableBlockEntity.getWorld().getRecipeManager().listAllOfType(RITUAL_RECIPE_RECIPE_TYPE).stream()
				.filter(r -> BotDUtils.containsAllIngredients(r.ingredients.stream().filter(ingredient -> !ingredient.isEmpty()).collect(Collectors.toList()), necroTableBlockEntity.getItems()))
				.findFirst().orElse(null);
	}
}
