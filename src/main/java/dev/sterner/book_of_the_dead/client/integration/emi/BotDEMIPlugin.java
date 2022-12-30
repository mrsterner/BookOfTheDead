package dev.sterner.book_of_the_dead.client.integration.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import dev.sterner.book_of_the_dead.common.recipe.ButcheringRecipe;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.registry.BotDRecipeTypes;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.recipe.RecipeManager;

public class BotDEMIPlugin implements EmiPlugin {
	private static final EmiStack ICON = EmiStack.of(BotDObjects.BLOODY_BUTCHER_KNIFE);
	public static final EmiRecipeCategory BUTCHERING_CATEGORY = new EmiRecipeCategory(
			Constants.id("butchering"), ICON
	);


	@Override
	public void register(EmiRegistry emiRegistry) {
		emiRegistry.addCategory(BUTCHERING_CATEGORY);
		RecipeManager manager = emiRegistry.getRecipeManager();
		for (ButcheringRecipe recipe : manager.listAllOfType(BotDRecipeTypes.BUTCHERING_RECIPE_TYPE)) {
			emiRegistry.addRecipe(new ButcheringEMIRecipe(recipe));
		}
	}
}
