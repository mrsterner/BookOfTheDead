package dev.sterner.book_of_the_dead.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import dev.sterner.book_of_the_dead.api.interfaces.IRecipe;
import dev.sterner.book_of_the_dead.common.registry.BotDRecipeTypes;
import dev.sterner.book_of_the_dead.common.util.RecipeUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.quiltmc.qsl.recipe.api.serializer.QuiltRecipeSerializer;


public class ButcheringRecipe implements IRecipe {
	private final Identifier identifier;
	public final EntityType<?> entityType;
	private final DefaultedList<Pair<ItemStack, Float>> outputs;
	public final Pair<ItemStack, Float> headDrop;

	public ButcheringRecipe(Identifier id, EntityType<?> entityType, DefaultedList<Pair<ItemStack, Float>> outputs, Pair<ItemStack, Float> headDrop) {
		this.identifier = id;
		this.entityType = entityType;
		this.outputs = outputs;
		this.headDrop = headDrop;
	}

	@Override
	public boolean matches(Inventory inventory, World world) {
		return false;
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
			EntityType<?> entityType = Registries.ENTITY_TYPE.get(new Identifier(JsonHelper.getString(json, "entityType")));
			JsonArray array = JsonHelper.getArray(json, "results");
			DefaultedList<Pair<ItemStack, Float>> outputs = RecipeUtils.deserializeStackPairs(array);
			if (outputs.isEmpty()) {
				throw new JsonParseException("No output for Butchering");
			} else if (outputs.size() > 8) {
				throw new JsonParseException("Too many outputs for Butchering recipe");
			}
			Pair<ItemStack, Float> headDrop = Pair.of(ItemStack.EMPTY, 1.0f);
			if(JsonHelper.hasArray(json, "head")){
				JsonArray headArray = JsonHelper.getArray(json, "head");
				headDrop = RecipeUtils.deserializeStackPairs(headArray).get(0);
			}

			return new ButcheringRecipe(id, entityType, outputs, headDrop);
		}

		@Override
		public ButcheringRecipe read(Identifier id, PacketByteBuf buf) {
			EntityType<?> entityType = Registries.ENTITY_TYPE.get(new Identifier(buf.readString()));
			DefaultedList<Pair<ItemStack, Float>> outputs = DefaultedList.ofSize(buf.readInt(), Pair.of(ItemStack.EMPTY, 1.0F));
			outputs.replaceAll(ignored -> Pair.of(buf.readItemStack(), buf.readFloat()));
			Pair<ItemStack, Float> headDrop = Pair.of(buf.readItemStack(), buf.readFloat());

			return new ButcheringRecipe(id, entityType, outputs, headDrop);

		}

		@Override
		public void write(PacketByteBuf buf, ButcheringRecipe recipe) {
			buf.writeString(Registries.ENTITY_TYPE.getId(recipe.entityType).toString());
			buf.writeInt(recipe.outputs.size());
			for (Pair<ItemStack, Float> pair : recipe.outputs) {
				buf.writeItemStack(pair.getFirst());
				buf.writeFloat(pair.getSecond());
			}
			buf.writeItemStack(recipe.headDrop.getFirst());
			buf.writeFloat(recipe.headDrop.getSecond());
		}

		@Override
		public JsonObject toJson(ButcheringRecipe recipe) {
			var obj = new JsonObject();//TODO
			return obj;
		}
	}
}
