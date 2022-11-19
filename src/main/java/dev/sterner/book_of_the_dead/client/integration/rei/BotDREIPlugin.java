package dev.sterner.book_of_the_dead.client.integration.rei;

import dev.sterner.book_of_the_dead.common.recipe.ButcheringRecipe;
import dev.sterner.book_of_the_dead.common.util.Constants;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;

public class BotDREIPlugin implements REIClientPlugin {
	public static final CategoryIdentifier<ButcheringDropDisplay> BUTCHERING_DROPS = CategoryIdentifier.of(Constants.id("butchering_drops"));


	@Override
	public void registerCategories(CategoryRegistry registry) {
		registry.add(new ButcheringDropCategory());
	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		registry.registerFiller(ButcheringRecipe.class, ButcheringDropDisplay::new);
	}
}
