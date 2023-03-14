package dev.sterner.book_of_the_dead.common.util;

import com.mojang.datafixers.util.Pair;
import dev.sterner.book_of_the_dead.mixin.StructurePoolAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BotDUtils {

	public static void addItemToInventoryAndConsume(PlayerEntity player, Hand hand, ItemStack toAdd) {
		boolean shouldAdd = false;
		ItemStack stack = player.getStackInHand(hand);
		if (stack.getCount() == 1) {
			if (player.isCreative()) {
				shouldAdd = true;
			} else {
				player.setStackInHand(hand, toAdd);
			}
		} else {
			stack.decrement(1);
			shouldAdd = true;
		}
		if (shouldAdd) {
			if (!player.getInventory().insertStack(toAdd)) {
				player.dropItem(toAdd, false, true);
			}
		}
	}

	public static void tryAddElementToPool(Identifier targetPool, StructurePool pool, String elementId, StructurePool.Projection projection, int weight) {
		if(targetPool.equals(pool.getId())) {
			StructurePoolElement element = StructurePoolElement.ofProcessedLegacySingle(elementId, StructureProcessorLists.EMPTY).apply(projection);
			for (int i = 0; i < weight; i++) {
				((StructurePoolAccessor)pool).book_of_the_dead$getElements().add(element);
			}
			((StructurePoolAccessor)pool).book_of_the_dead$getElementCounts().add(Pair.of(element, weight));
		}
	}

	public static boolean containsAllIngredients(List<Ingredient> ingredients, List<ItemStack> items) {
		List<Integer> checkedIndexes = new ArrayList<>();
		for (Ingredient ingredient : ingredients) {
			for (int i = 0; i < items.size(); i++) {
				if (!checkedIndexes.contains(i)) {
					if (ingredient.test(items.get(i))) {
						checkedIndexes.add(i);
						break;
					}
				}
			}
		}
		return checkedIndexes.size() == ingredients.size();
	}

	public static NbtCompound fromVec3d(Vec3d pos) {
		NbtCompound nbtCompound = new NbtCompound();
		nbtCompound.putDouble("X", pos.getX());
		nbtCompound.putDouble("Y", pos.getY());
		nbtCompound.putDouble("Z", pos.getZ());
		return nbtCompound;
	}

	public static Vec3d toVec3d(NbtCompound compound) {
		return new Vec3d(compound.getDouble("X"), compound.getDouble("Y"), compound.getDouble("Z"));
	}

	public static Pair<ArrayList<Vec3d>, ArrayList<Float>> genRandomPos(double interval){
		ArrayList<Vec3d> list = new ArrayList<>();
		ArrayList<Float> yawList = new ArrayList<>();
		for(int i = 0; i < 10; i++) {
			Random randomX = new Random();
			Random randomZ = new Random();

			list.add(i, new Vec3d(nextDouble(randomX, -interval, interval), 0, nextDouble(randomZ, -interval, interval)));
			yawList.add(i, randomX.nextFloat());
		}
		return Pair.of(list, yawList);
	}

	public static double nextDouble(Random random, double min, double max) {
		return min >= max ? min : random.nextDouble() * (max - min) + min;
	}
}
