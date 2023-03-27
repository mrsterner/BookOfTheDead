package dev.sterner.book_of_the_dead.client.integration.rei;

import com.mojang.datafixers.util.Pair;
import dev.sterner.book_of_the_dead.common.recipe.ButcheringRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.util.Collections;
import java.util.List;

public class ButcheringDropDisplay implements Display {
	public final EntityType<?> entityType;
	public final List<EntryIngredient> outputs;
	public final DefaultedList<ItemStack> outputList;

	public ButcheringDropDisplay(ButcheringRecipe recipe) {
		entityType = recipe.entityType();
		List<ItemStack> itemStackList = recipe.getOutputs().stream().map(Pair::getFirst).toList();
		DefaultedList<ItemStack> defaultedList = DefaultedList.copyOf(ItemStack.EMPTY, itemStackList.toArray(new ItemStack[0]));
		this.outputs = Collections.singletonList(EntryIngredients.ofItemStacks(defaultedList));
		this.outputList = defaultedList;
	}

	@Override
	public List<EntryIngredient> getInputEntries() {
		return List.of();
	}

	@Override
	public List<EntryIngredient> getOutputEntries() {
		return outputs;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return BotDREIPlugin.BUTCHERING_DROPS;
	}
}
