package dev.sterner.book_of_the_dead.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import dev.sterner.book_of_the_dead.api.interfaces.IRecipe;
import dev.sterner.book_of_the_dead.common.registry.BotDRecipeTypes;
import dev.sterner.book_of_the_dead.common.util.RecipeUtils;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.quiltmc.qsl.recipe.api.serializer.QuiltRecipeSerializer;

import java.util.ArrayList;
import java.util.List;

public record RetortRecipe(Identifier id, int color, DefaultedList<Ingredient> ingredients, ItemStack output) implements IRecipe {

	@Override
	public boolean matches(Inventory inventory, World world) {
		return matches(inventory, ingredients);
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
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

			//Inputs
			DefaultedList<Ingredient> inputs = RecipeUtils.deserializeIngredients(JsonHelper.getArray(json, "inputs"));

			//Output
			Item outputItem = Registries.ITEM.getOrEmpty(new Identifier(JsonHelper.getString(json, "outputItem"))).orElseThrow(() -> new JsonSyntaxException("No such item " + JsonHelper.getString(json, "outputItem")));
			ItemStack output = new ItemStack(outputItem, JsonHelper.getInt(json, "outputCount", 1));

			//Color
			int color = Integer.parseInt(JsonHelper.getString(json, "color").substring(2), 16);
			return new RetortRecipe(id, color, inputs, output);
		}

		@Override
		public RetortRecipe read(Identifier id, PacketByteBuf buf) {
			//Inputs
			DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readVarInt(), Ingredient.EMPTY);
			inputs.replaceAll(ignored -> Ingredient.fromPacket(buf));

			//Output
			ItemStack itemStack = buf.readItemStack();

			//Color
			int color = Integer.parseInt(buf.readString().substring(2), 16);
			return new RetortRecipe(id, color, inputs, itemStack);
		}

		@Override
		public void write(PacketByteBuf buf, RetortRecipe recipe) {

			//Inputs
			buf.writeInt(recipe.ingredients.size());
			recipe.ingredients.forEach(ingredient -> ingredient.write(buf));

			//Output
			buf.writeItemStack(recipe.output);

			//Color
			buf.writeInt(recipe.color);
		}

		@Override
		public JsonObject toJson(RetortRecipe recipe) {
			return null;
		}
	}
}
