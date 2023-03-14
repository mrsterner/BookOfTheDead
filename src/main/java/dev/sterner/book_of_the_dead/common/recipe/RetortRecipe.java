package dev.sterner.book_of_the_dead.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.sterner.book_of_the_dead.common.registry.BotDRecipeTypes;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.quiltmc.qsl.recipe.api.serializer.QuiltRecipeSerializer;

import java.util.ArrayList;
import java.util.List;

public class RetortRecipe implements Recipe<Inventory> {
	private final Identifier id;
	public final int color;
	public final DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(4, Ingredient.EMPTY);
	public final ItemStack output;

	public RetortRecipe(Identifier id, int color, Ingredient[] ingredients, ItemStack output) {
		this.id = id;
		for (int i = 0; i < ingredients.length; i++) {
			this.ingredients.set(i, ingredients[i]);
		}
		this.output = output;
		this.color = color;
	}

	@Override
	public boolean matches(Inventory inventory, World world) {
		return matches(inventory, ingredients);
	}

	@Override
	public ItemStack craft(Inventory inventory) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getOutput() {
		return output;
	}

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return BotDRecipeTypes.RETORT_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return BotDRecipeTypes.RETORT_RECIPE_TYPE;
	}

	@Override
	public DefaultedList<Ingredient> getIngredients() {
		return ingredients;
	}

	public static boolean matches(Inventory inv, DefaultedList<Ingredient> input) {
		List<ItemStack> checklist = new ArrayList<>();
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (!stack.isEmpty()) {
				checklist.add(stack);
			}
		}
		if (input.size() != checklist.size()) {
			return false;
		}
		for (Ingredient ingredient : input) {
			boolean found = false;
			for (ItemStack stack : checklist) {
				if (ingredient.test(stack)) {
					found = true;
					checklist.remove(stack);
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}

	public static class Serializer implements QuiltRecipeSerializer<RetortRecipe> {

		@Override
		public RetortRecipe read(Identifier id, JsonObject json) {
			Ingredient[] ingredients = readIngredients(JsonHelper.getArray(json, "ingredients"));
			Item outputItem = Registry.ITEM.getOrEmpty(new Identifier(JsonHelper.getString(json, "outputItem"))).orElseThrow(() -> new JsonSyntaxException("No such item " + JsonHelper.getString(json, "outputItem")));
			ItemStack output = new ItemStack(outputItem, JsonHelper.getInt(json,"outputCount", 1));
			int color = Integer.parseInt(JsonHelper.getString(json, "color").substring(2), 16);
			return new RetortRecipe(id, color, ingredients, output);
		}

		private Ingredient[] readIngredients(JsonArray json) {
			List<Ingredient> ingredients = new ArrayList<>();
			json.forEach(jsonElement -> ingredients.add(Ingredient.fromJson(jsonElement)));
			return ingredients.toArray(new Ingredient[ingredients.size()]);
		}

		@Override
		public RetortRecipe read(Identifier id, PacketByteBuf buf) {
			int sizeIngredients = buf.readInt();
			ItemStack itemStack = buf.readItemStack();
			List<Ingredient> ingredients = new ArrayList<>();
			for (int i = 0; i < sizeIngredients; i++) {
				ingredients.add(Ingredient.fromPacket(buf));
			}
			int color = Integer.parseInt(buf.readString().substring(2), 16);
			return new RetortRecipe(id, color, ingredients.toArray(new Ingredient[ingredients.size()]), itemStack);
		}

		@Override
		public void write(PacketByteBuf buf, RetortRecipe recipe) {
			buf.writeInt(recipe.ingredients.size());
			buf.writeItemStack(recipe.output);
			recipe.ingredients.forEach(i -> i.write(buf));
			buf.writeInt(recipe.color);
		}

		@Override
		public JsonObject toJson(RetortRecipe recipe) {
			return null;
		}
	}
}
