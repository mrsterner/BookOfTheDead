package dev.sterner.book_of_the_dead.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.sterner.book_of_the_dead.api.NecrotableRitual;
import dev.sterner.book_of_the_dead.api.interfaces.IRecipe;
import dev.sterner.book_of_the_dead.common.registry.BotDRecipeTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDRegistries;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.List;

public class RitualRecipe implements IRecipe {
	public final DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(8, Ingredient.EMPTY);
	public final NecrotableRitual ritual;
	public final Identifier id;

	public RitualRecipe(Identifier id, Ingredient[] ingredients, NecrotableRitual ritual) {
		this.id = id;
		for (int i = 0; i < ingredients.length; i++) {
			this.ingredients.set(i, ingredients[i]);
		}
		this.ritual = ritual;
	}

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return BotDRecipeTypes.RITUAL_RECIPE_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return BotDRecipeTypes.RITUAL_RECIPE_RECIPE_TYPE;
	}

	@Override
	public DefaultedList<Ingredient> getIngredients() {
		return ingredients;
	}
	public static class Serializer implements RecipeSerializer<RitualRecipe> {

		@Override
		public RitualRecipe read(Identifier id, JsonObject json) {
			Ingredient[] ingredients = readIngredients(JsonHelper.getArray(json, "ingredients"));
			NecrotableRitual ritual = BotDRegistries.NECROTABLE_RITUALS.get(new Identifier(JsonHelper.getString(json, "ritual")));
			return new RitualRecipe(id, ingredients, ritual);
		}

		private Ingredient[] readIngredients(JsonArray json) {
			List<Ingredient> ingredients = new ArrayList<>();
			json.forEach(jsonElement -> ingredients.add(Ingredient.fromJson(jsonElement)));
			return ingredients.toArray(new Ingredient[ingredients.size()]);
		}

		@Override
		public RitualRecipe read(Identifier id, PacketByteBuf buf) {
			int sizeIngredients = buf.readInt();
			List<Ingredient> ingredients = new ArrayList<>();
			for (int i = 0; i < sizeIngredients; i++) {
				ingredients.add(Ingredient.fromPacket(buf));
			}
			NecrotableRitual rite = BotDRegistries.NECROTABLE_RITUALS.get(buf.readIdentifier());
			return new RitualRecipe(id, ingredients.toArray(new Ingredient[ingredients.size()]), rite);
		}

		@Override
		public void write(PacketByteBuf buf, RitualRecipe recipe) {
			buf.writeInt(recipe.ingredients.size());
			recipe.ingredients.forEach(i -> i.write(buf));
			buf.writeIdentifier(recipe.ritual.getId());
		}
	}
}
