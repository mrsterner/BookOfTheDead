package dev.sterner.book_of_the_dead.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.sterner.book_of_the_dead.api.NecrotableRitual;
import dev.sterner.book_of_the_dead.api.interfaces.IRecipe;
import dev.sterner.book_of_the_dead.common.registry.BotDRecipeTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDRegistries;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RitualRecipe implements Recipe<Inventory> {
	public final DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(8, Ingredient.EMPTY);
	public final ItemStack output;
	public final NecrotableRitual ritual;
	public final Identifier id;
	public final int duration;

	public final EntityType<?> inputEntityType;
	public final int inputEntityCount;

	public final EntityType<?> outputEntityType;
	public final int outputEntityCount;

	public final boolean requireBotD;
	public final boolean requireEmeraldTablet;

	public RitualRecipe(
			Identifier id,
			Ingredient[] ingredients,
			EntityType<?> inputEntityType,
			int inputEntityCount,
			ItemStack output,
			EntityType<?> outputEntityType,
			int outputEntityCount,
			NecrotableRitual ritual,
			int duration,
			boolean requireBotD,
			boolean requireEmeraldTablet
	) {
		this.id = id;
		for (int i = 0; i < ingredients.length; i++) {
			this.ingredients.set(i, ingredients[i]);
		}
		this.ritual = ritual;
		this.output = output;
		this.duration = duration;
		this.outputEntityType = outputEntityType;
		this.outputEntityCount = outputEntityCount;
		this.inputEntityCount = inputEntityCount;
		this.inputEntityType = inputEntityType;
		this.requireBotD = requireBotD;
		this.requireEmeraldTablet = requireEmeraldTablet;
	}

	public int getDuration(){
		return this.duration;
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

	@Override
	public DefaultedList<Ingredient> getIngredients() {
		return ingredients;
	}

	public static class Serializer implements RecipeSerializer<RitualRecipe> {

		@Override
		public RitualRecipe read(Identifier id, JsonObject json) {
			NecrotableRitual ritual = BotDRegistries.NECROTABLE_RITUALS.get(new Identifier(JsonHelper.getString(json, "ritual")));
			boolean requireBotD = JsonHelper.getBoolean(json, "requireBotD", false);
			boolean requireEmeraldTablet = JsonHelper.getBoolean(json, "requireEmeraldTablet", false);

			EntityType<?> inputEntityType = Registry.ENTITY_TYPE.get(new Identifier(JsonHelper.getString(json, "inputEntityType", "chicken")));
			int inputEntityCount = JsonHelper.getInt(json,"inputEntityCount", 1);

			EntityType<?> outputEntityType = Registry.ENTITY_TYPE.get(new Identifier(JsonHelper.getString(json, "outputEntityType", "chicken")));
			int outputEntityCount = JsonHelper.getInt(json,"outputEntityCount", 1);

			Ingredient[] ingredients = readIngredients(JsonHelper.getArray(json, "ingredients"));

			Item outputItem = Registry.ITEM.getOrEmpty(new Identifier(JsonHelper.getString(json, "outputItem"))).orElseThrow(() -> new JsonSyntaxException("No such item " + JsonHelper.getString(json, "outputItem")));
			ItemStack output = new ItemStack(outputItem, JsonHelper.getInt(json,"outputCount", 1));

			int duration = JsonHelper.getInt(json, "duration", 20 * 8);

			return new RitualRecipe(id, ingredients, inputEntityType, inputEntityCount, output, outputEntityType, outputEntityCount, ritual, duration, requireBotD, requireEmeraldTablet);
		}

		private Ingredient[] readIngredients(JsonArray json) {
			List<Ingredient> ingredients = new ArrayList<>();
			json.forEach(jsonElement -> ingredients.add(Ingredient.fromJson(jsonElement)));
			return ingredients.toArray(new Ingredient[ingredients.size()]);
		}

		@Override
		public RitualRecipe read(Identifier id, PacketByteBuf buf) {
			NecrotableRitual rite = BotDRegistries.NECROTABLE_RITUALS.get(buf.readIdentifier());
			boolean requireBotD = buf.readBoolean();
			boolean requireEmeraldTablet = buf.readBoolean();
			int sizeIngredients = buf.readInt();
			ItemStack itemStack = buf.readItemStack();
			List<Ingredient> ingredients = new ArrayList<>();
			for (int i = 0; i < sizeIngredients; i++) {
				ingredients.add(Ingredient.fromPacket(buf));
			}
			final EntityType<?> inputEntityType = Registry.ENTITY_TYPE.get(new Identifier(buf.readString()));
			int inputEntityCount = buf.readInt();

			final EntityType<?> outputEntityType = Registry.ENTITY_TYPE.get(new Identifier(buf.readString()));
			int outputEntityCount = buf.readInt();
			int duration = buf.readInt();
			return new RitualRecipe(id, ingredients.toArray(new Ingredient[ingredients.size()]), inputEntityType, inputEntityCount, itemStack, outputEntityType, outputEntityCount, rite, duration, requireBotD, requireEmeraldTablet);
		}

		@Override
		public void write(PacketByteBuf buf, RitualRecipe recipe) {
			buf.writeIdentifier(recipe.ritual.getId());
			buf.writeBoolean(recipe.requireBotD);
			buf.writeBoolean(recipe.requireEmeraldTablet);
			buf.writeInt(recipe.ingredients.size());
			buf.writeItemStack(recipe.output);
			recipe.ingredients.forEach(i -> i.write(buf));
			buf.writeString(Registry.ENTITY_TYPE.getId(recipe.inputEntityType).toString());
			buf.writeInt(recipe.inputEntityCount);
			buf.writeString(Registry.ENTITY_TYPE.getId(recipe.outputEntityType).toString());
			buf.writeInt(recipe.outputEntityCount);
			buf.writeInt(recipe.duration);
		}
	}
}
