package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.api.PedestalInfo;
import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import dev.sterner.book_of_the_dead.common.recipe.ButcheringRecipe;
import dev.sterner.book_of_the_dead.common.recipe.RetortRecipe;
import dev.sterner.book_of_the_dead.common.recipe.RitualRecipe;
import dev.sterner.book_of_the_dead.common.util.Constants;
import dev.sterner.book_of_the_dead.common.util.RecipeUtils;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.stream.Collectors;

public interface BotDRecipeTypes {
	RecipeSerializer<ButcheringRecipe> BUTCHERING_RECIPE_SERIALIZER = new ButcheringRecipe.Serializer();
	RecipeType<ButcheringRecipe> BUTCHERING_RECIPE_TYPE = new RecipeType<>() {
		@Override
		public String toString() {
			return Constants.MODID + ":butchering";
		}
	};

	RecipeSerializer<RitualRecipe> RITUAL_RECIPE_SERIALIZER = new RitualRecipe.Serializer();
	RecipeType<RitualRecipe> RITUAL_RECIPE_TYPE = new RecipeType<>() {
		@Override
		public String toString() {
			return Constants.MODID + ":ritual";
		}
	};

	RecipeSerializer<RetortRecipe> RETORT_RECIPE_SERIALIZER = new RetortRecipe.Serializer();
	RecipeType<RetortRecipe> RETORT_RECIPE_TYPE = new RecipeType<>() {
		@Override
		public String toString() {
			return Constants.MODID + ":retort";
		}
	};


	static void init() {
		Registry.register(Registries.RECIPE_SERIALIZER, Constants.id("butchering"), BUTCHERING_RECIPE_SERIALIZER);
		Registry.register(Registries.RECIPE_TYPE, Constants.id("butchering"), BUTCHERING_RECIPE_TYPE);

		Registry.register(Registries.RECIPE_SERIALIZER, Constants.id("ritual"), RITUAL_RECIPE_SERIALIZER);
		Registry.register(Registries.RECIPE_TYPE, Constants.id("ritual"), RITUAL_RECIPE_TYPE);

		Registry.register(Registries.RECIPE_SERIALIZER, Constants.id("retort"), RETORT_RECIPE_SERIALIZER);
		Registry.register(Registries.RECIPE_TYPE, Constants.id("retort"), RETORT_RECIPE_TYPE);
	}


	static RitualRecipe getRiteRecipe(NecroTableBlockEntity ritualBlockEntity) {
		World world = ritualBlockEntity.getWorld();
		if (world == null) {
			return null;
		}
		return world.getRecipeManager().listAllOfType(RITUAL_RECIPE_TYPE).stream()
				.filter(r -> RecipeUtils.containsAllIngredients(r.inputs.stream()
						.filter(ingredient -> !ingredient.isEmpty()).collect(Collectors.toList()), ritualBlockEntity.getPedestalInfo(world).stream().map(PedestalInfo::stack).toList()))
				.findFirst().orElse(null);
	}


}
