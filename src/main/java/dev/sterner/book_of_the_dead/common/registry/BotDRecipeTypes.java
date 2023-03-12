package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.common.block.entity.RitualBlockEntity;
import dev.sterner.book_of_the_dead.common.recipe.ButcheringRecipe;
import dev.sterner.book_of_the_dead.common.recipe.RetortRecipe;
import dev.sterner.book_of_the_dead.common.recipe.RitualRecipe;
import dev.sterner.book_of_the_dead.common.util.Constants;
import dev.sterner.book_of_the_dead.common.util.BotDUtils;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.stream.Collectors;

public class BotDRecipeTypes {
	public static final RecipeSerializer<ButcheringRecipe> BUTCHERING_RECIPE_SERIALIZER = new ButcheringRecipe.Serializer();
	public static final RecipeType<ButcheringRecipe> BUTCHERING_RECIPE_TYPE = new RecipeType<>() {
		@Override
		public String toString() {
			return Constants.MOD_ID + ":butchering";
		}
	};

	public static final RecipeSerializer<RitualRecipe> RITUAL_RECIPE_SERIALIZER = new RitualRecipe.Serializer();
	public static final RecipeType<RitualRecipe> RITUAL_RECIPE_TYPE = new RecipeType<>() {
		@Override
		public String toString() {
			return Constants.MOD_ID + ":ritual";
		}
	};

	public static final RecipeSerializer<RetortRecipe> RETORT_RECIPE_SERIALIZER = new RetortRecipe.Serializer();
	public static final RecipeType<RetortRecipe> RETORT_RECIPE_TYPE = new RecipeType<>() {
		@Override
		public String toString() {
			return Constants.MOD_ID + ":retort";
		}
	};


	public static void init(){
		Registry.register(Registry.RECIPE_SERIALIZER, Constants.id("butchering"), BUTCHERING_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_TYPE,Constants.id("butchering"), BUTCHERING_RECIPE_TYPE);

		Registry.register(Registry.RECIPE_SERIALIZER, Constants.id("ritual"), RITUAL_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_TYPE,Constants.id("ritual"), RITUAL_RECIPE_TYPE);

		Registry.register(Registry.RECIPE_SERIALIZER, Constants.id("retort"), RETORT_RECIPE_SERIALIZER);
		Registry.register(Registry.RECIPE_TYPE,Constants.id("retort"), RETORT_RECIPE_TYPE);
	}


	public static RitualRecipe getRiteRecipe(RitualBlockEntity ritualBlockEntity) {
		World world = ritualBlockEntity.getWorld();
		return world.getRecipeManager().listAllOfType(RITUAL_RECIPE_TYPE).stream()
				.filter(r -> BotDUtils.containsAllIngredients(r.inputs.stream()
						.filter(ingredient -> !ingredient.isEmpty()).collect(Collectors.toList()), ritualBlockEntity.getPedestalInfo(world).stream().map(Pair::getLeft).toList()))
				.findFirst().orElse(null);
	}


}
