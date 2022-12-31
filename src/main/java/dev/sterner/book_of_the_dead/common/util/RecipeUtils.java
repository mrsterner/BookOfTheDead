package dev.sterner.book_of_the_dead.common.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RecipeUtils {



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
		float chance = 1.2f;
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
