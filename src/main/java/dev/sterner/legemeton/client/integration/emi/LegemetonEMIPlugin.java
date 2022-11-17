package dev.sterner.legemeton.client.integration.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import dev.sterner.legemeton.common.recipe.ButcheringRecipe;
import dev.sterner.legemeton.common.registry.LegemetonObjects;
import dev.sterner.legemeton.common.registry.LegemetonRecipeTypes;
import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.recipe.RecipeManager;

public class LegemetonEMIPlugin implements EmiPlugin {
	private static final EmiStack ICON = EmiStack.of(LegemetonObjects.BLOODY_BUTCHER_KNIFE);
	public static final EmiRecipeCategory BUTCHERING_CATEGORY = new EmiRecipeCategory(
			Constants.id("butchering"), ICON
	);


	@Override
	public void register(EmiRegistry emiRegistry) {
		emiRegistry.addCategory(BUTCHERING_CATEGORY);
		RecipeManager manager = emiRegistry.getRecipeManager();
		for (ButcheringRecipe recipe : manager.listAllOfType(LegemetonRecipeTypes.BUTCHERING_RECIPE_RECIPE_TYPE)) {
			emiRegistry.addRecipe(new ButcheringEMIRecipe(recipe));
		}
	}
}
