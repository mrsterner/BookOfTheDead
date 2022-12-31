package dev.sterner.book_of_the_dead.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import dev.sterner.book_of_the_dead.common.registry.BotDRecipeTypes;
import dev.sterner.book_of_the_dead.common.util.RecipeUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.Inventory;
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


public class ButcheringRecipe implements Recipe<Inventory> {
	private final Identifier identifier;
	public final EntityType<?> entityType;
	private final DefaultedList<Pair<ItemStack, Float>> outputs;

	public ButcheringRecipe(Identifier id, EntityType<?> entityType, DefaultedList<Pair<ItemStack, Float>> outputs) {
		this.identifier = id;
		this.entityType = entityType;
		this.outputs = outputs;
	}

	@Override
	public boolean matches(Inventory inventory, World world) {
		return false;
	}

	@Override
	public ItemStack craft(Inventory inventory) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean fits(int width, int height) {
		return false;
	}

	@Override
	public ItemStack getOutput() {
		return ItemStack.EMPTY;
	}

	public DefaultedList<Pair<ItemStack, Float>> getOutputs() {
		return outputs;
	}

	@Override
	public Identifier getId() {
		return identifier;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return BotDRecipeTypes.BUTCHERING_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return BotDRecipeTypes.BUTCHERING_RECIPE_TYPE;
	}

	public static class Serializer implements QuiltRecipeSerializer<ButcheringRecipe> {

		@Override
		public ButcheringRecipe read(Identifier id, JsonObject json) {
			EntityType<?> entityType = Registry.ENTITY_TYPE.get(new Identifier(JsonHelper.getString(json, "entityType")));
			JsonArray array = JsonHelper.getArray(json, "results");
			DefaultedList<Pair<ItemStack, Float>> outputs = RecipeUtils.deserializeStacks(array);
			if (outputs.isEmpty()) {
				throw new JsonParseException("No output for Butchering");
			} else if (outputs.size() > 8) {
				throw new JsonParseException("Too many outputs for Butchering recipe");
			}
			return new ButcheringRecipe(id, entityType, outputs);
		}

		@Override
		public ButcheringRecipe read(Identifier id, PacketByteBuf buf) {
			EntityType<?> entityType = Registry.ENTITY_TYPE.get(new Identifier(buf.readString()));
			DefaultedList<Pair<ItemStack, Float>> outputs = DefaultedList.ofSize(buf.readInt(), Pair.of(ItemStack.EMPTY, 1.1F));
			outputs.replaceAll(ignored -> Pair.of(buf.readItemStack(), buf.readFloat()));

			return new ButcheringRecipe(id, entityType, outputs);

		}

		@Override
		public void write(PacketByteBuf buf, ButcheringRecipe recipe) {
			buf.writeString(Registry.ENTITY_TYPE.getId(recipe.entityType).toString());
			buf.writeInt(recipe.outputs.size());
			for (Pair<ItemStack, Float> pair : recipe.outputs) {
				buf.writeItemStack(pair.getFirst());
				buf.writeFloat(pair.getSecond());
			}
		}

		@Override
		public JsonObject toJson(ButcheringRecipe recipe) {
			var obj = new JsonObject();//TODO
			return obj;
		}
	}
}
