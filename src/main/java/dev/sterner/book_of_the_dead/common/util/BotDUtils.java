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

	/**
	 * Try to add an itemStack to a players Hand, otherwise drops the item
	 * @param player player
	 * @param hand hand to receive item
	 * @param toAdd item to add
	 */
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
}
