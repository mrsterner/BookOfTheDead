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
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.quiltmc.qsl.recipe.api.serializer.QuiltRecipeSerializer;


public class ButcheringRecipe implements Recipe<Inventory> {
	private final Identifier identifier;
	public final EntityType<?> entity_type;
	private final DefaultedList<Pair<ItemStack, Float>> output;

	public ButcheringRecipe(Identifier id, EntityType<?> entity_type, DefaultedList<Pair<ItemStack, Float>> output) {
		this.identifier = id;
		this.entity_type = entity_type;
		this.output = output;
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
		return output;
	}

	@Override
	public Identifier getId() {
		return identifier;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return BotDRecipeTypes.BUTCHERING_RECIPE_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return BotDRecipeTypes.BUTCHERING_RECIPE_RECIPE_TYPE;
	}

	public static class Serializer implements QuiltRecipeSerializer<ButcheringRecipe> {

		@Override
		public ButcheringRecipe read(Identifier id, JsonObject json) {
			final EntityType<?> entityType = Registry.ENTITY_TYPE.get(new Identifier(JsonHelper.getString(json, "entity_type")));
			JsonArray array = JsonHelper.getArray(json, "results");
			final DefaultedList<Pair<ItemStack, Float>> outputs = RecipeUtils.deserializeStacks(array);
			if (outputs.isEmpty()) {
				throw new JsonParseException("No output for Butchering");
			} else if (outputs.size() > 8) {
				throw new JsonParseException("Too many outputs for Butchering recipe");
			}
			return new ButcheringRecipe(id, entityType, outputs);
		}

		@Override
		public ButcheringRecipe read(Identifier id, PacketByteBuf buf) {
			final EntityType<?> entityType = Registry.ENTITY_TYPE.get(new Identifier(buf.readString()));
			final DefaultedList<Pair<ItemStack, Float>> outputs = DefaultedList.ofSize(buf.readVarInt(), Pair.of(ItemStack.EMPTY, 1F));
			outputs.replaceAll(ignored -> Pair.of(buf.readItemStack(), buf.readFloat()));
			return new ButcheringRecipe(id, entityType, outputs);
		}

		@Override
		public void write(PacketByteBuf buf, ButcheringRecipe recipe) {
			buf.writeString(Registry.ENTITY_TYPE.getId(recipe.entity_type).toString());
			buf.writeVarInt(recipe.output.size());
			for (var stack : recipe.output) {
				buf.writeItemStack(stack.getFirst());
			}
		}

		@Override
		public JsonObject toJson(ButcheringRecipe recipe) {
			return null;
		}


	}
}
