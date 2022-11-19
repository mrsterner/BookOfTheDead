package dev.sterner.book_of_the_dead.client.integration.rei;

import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.util.Constants;
import dev.sterner.book_of_the_dead.common.util.RenderUtils;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ButcheringDropCategory implements DisplayCategory<ButcheringDropDisplay> {
	public static final Text TITLE = Text.translatable("rei.book_of_the_dead.butchering_drops");
	public static final EntryStack<ItemStack> ICON = EntryStacks.of(BotDObjects.BLOODY_BUTCHER_KNIFE);

	@Override
	public Renderer getIcon() {
		return ICON;
	}

	@Override
	public Text getTitle() {
		return TITLE;
	}

	@Override
	public int getDisplayHeight() {
		return 36;
	}

	@Override
	public CategoryIdentifier<? extends ButcheringDropDisplay> getCategoryIdentifier() {
		return BotDREIPlugin.BUTCHERING_DROPS;
	}

	@Override
	public List<Widget> setupDisplay(ButcheringDropDisplay display, Rectangle bounds) {
		Entity target = display.entityType.create(MinecraftClient.getInstance().world);
		Rectangle targetBounds = new Rectangle(bounds.getMinX(), bounds.getCenterY() - 27, 54, 54);

		Point startPoint = new Point(bounds.getCenterX() - 64, bounds.getCenterY() - 8);

		List<Widget> widgets = new ArrayList<>();
		bounds.grow(5,5);
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createDrawableWidget((helper, matrices, mouseX, mouseY, delta) -> {
			if (target instanceof LivingEntity livingEntity) {
				RenderUtils.drawEntity(0,0,20, mouseX, mouseY, livingEntity, targetBounds);
			}
		}));

		widgets.add(Widgets.createTexturedWidget(Constants.id("textures/gui/icons/butchering.png"), startPoint.x + 50, startPoint.y - 9, 0, 0, 32, 32, 16, 16, 16, 16));

		widgets.add(Widgets.createSlot(new Point(startPoint.x + 84, startPoint.y)).entries(EntryIngredients.ofIngredient((Ingredient.ofStacks(display.outputList.get(0))))).markInput());
		if (display.outputList.size() > 1) {
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 18 + 84, startPoint.y)).entries(EntryIngredients.ofIngredient((Ingredient.ofStacks(display.outputList.get(1))))).markInput());
			if (display.outputList.size() > 2) {
				widgets.add(Widgets.createSlot(new Point(startPoint.x + 36 + 84, startPoint.y)).entries(EntryIngredients.ofIngredient((Ingredient.ofStacks(display.outputList.get(2))))).markInput());
				if (display.outputList.size() > 3) {
					widgets.add(Widgets.createSlot(new Point(startPoint.x + 84, startPoint.y + 18)).entries(EntryIngredients.ofIngredient((Ingredient.ofStacks(display.outputList.get(3))))).markInput());
					if (display.outputList.size() > 4) {
						widgets.add(Widgets.createSlot(new Point(startPoint.x + 18 + 84, startPoint.y + 18)).entries(EntryIngredients.ofIngredient((Ingredient.ofStacks(display.outputList.get(4))))).markInput());
						if (display.outputList.size() > 5) {
							widgets.add(Widgets.createSlot(new Point(startPoint.x + 36 + 84, startPoint.y + 18)).entries(EntryIngredients.ofIngredient((Ingredient.ofStacks(display.outputList.get(5))))).markInput());
							if (display.outputList.size() > 6) {
								widgets.add(Widgets.createSlot(new Point(startPoint.x + 18 + 84, startPoint.y + 36)).entries(EntryIngredients.ofIngredient((Ingredient.ofStacks(display.outputList.get(6))))).markInput());
								if (display.outputList.size() > 7) {
									widgets.add(Widgets.createSlot(new Point(startPoint.x + 36 + 84, startPoint.y + 36)).entries(EntryIngredients.ofIngredient((Ingredient.ofStacks(display.outputList.get(7))))).markInput());
								}
							}
						}
					}
				}
			}
		}





		return widgets;
	}
}
