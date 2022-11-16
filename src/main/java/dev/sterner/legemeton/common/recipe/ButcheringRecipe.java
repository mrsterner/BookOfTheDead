package dev.sterner.legemeton.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import dev.sterner.legemeton.common.registry.LegemetonRecipeTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
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
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.recipe.api.serializer.QuiltRecipeSerializer;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
		return LegemetonRecipeTypes.BUTCHERING_RECIPE_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return LegemetonRecipeTypes.BUTCHERING_RECIPE_RECIPE_TYPE;
	}

	public static class Serializer implements QuiltRecipeSerializer<ButcheringRecipe> {

		@Override
		public ButcheringRecipe read(Identifier id, JsonObject json) {
			final EntityType<?> entityType = Registry.ENTITY_TYPE.get(new Identifier(JsonHelper.getString(json, "entity_type")));
			JsonArray array = JsonHelper.getArray(json, "results");
			final DefaultedList<Pair<ItemStack, Float>> outputs = deserializeStacks(array);
			if (outputs.isEmpty()) {
				throw new JsonParseException("No output forButchering");
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

		public static DefaultedList<Pair<ItemStack, Float>> deserializeStacks(JsonArray array) {
			if (array.isJsonArray()) {
				return arrayStream(array.getAsJsonArray()).map(entry -> deserializeStack(entry.getAsJsonObject())).collect(DefaultedListCollector.toList());
			} else {
				return DefaultedList.copyOf(deserializeStack(array.getAsJsonObject()));
			}
		}

		public static Stream<JsonElement> arrayStream(JsonArray array) {
			return IntStream.range(0, array.size()).mapToObj(array::get);
		}

		public static @NotNull Pair<ItemStack, Float> deserializeStack(JsonObject object) {
			final Identifier id = new Identifier(JsonHelper.getString(object, "item"));
			final Item item = Registry.ITEM.get(id);
			if (Items.AIR == item) {
				throw new IllegalStateException("Invalid item: " + item);
			}
			int count = 1;
			float chance = 1;
			if (object.has("count")) {
				count = JsonHelper.getInt(object, "count");
			}
			if (object.has("chance")) {
				chance = JsonHelper.getFloat(object, "chance");
			}
			final ItemStack stack = new ItemStack(item, count);
			if (object.has("nbt")) {
				final NbtCompound tag = (NbtCompound) Dynamic.convert(JsonOps.INSTANCE, NbtOps.INSTANCE, object.get("nbt"));
				stack.setNbt(tag);
			}
			return Pair.of(stack, chance);
		}

		public static class DefaultedListCollector<T> implements Collector<T, DefaultedList<T>, DefaultedList<T>> {

			private static final Set<Characteristics> CH_ID = Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));

			public static <T> DefaultedListCollector<T> toList() {
				return new DefaultedListCollector<>();
			}

			@Override
			public Supplier<DefaultedList<T>> supplier() {
				return DefaultedList::of;
			}

			@Override
			public BiConsumer<DefaultedList<T>, T> accumulator() {
				return DefaultedList::add;
			}

			@Override
			public BinaryOperator<DefaultedList<T>> combiner() {
				return (left, right) -> {
					left.addAll(right);
					return left;
				};
			}

			@Override
			public Function<DefaultedList<T>, DefaultedList<T>> finisher() {
				return i -> (DefaultedList<T>) i;
			}

			@Override
			public Set<Characteristics> characteristics() {
				return CH_ID;
			}
		}
	}
}
