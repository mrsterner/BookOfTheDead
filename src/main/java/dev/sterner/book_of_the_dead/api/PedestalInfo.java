package dev.sterner.book_of_the_dead.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class PedestalInfo {
	public BlockPos blockPos;
	public ItemStack itemStack;

	public PedestalInfo(ItemStack itemStack, BlockPos blockPos) {
		this.blockPos = blockPos;
		this.itemStack = itemStack;
	}

	public static PedestalInfo of(ItemStack itemStack, BlockPos blockPos) {
		return new PedestalInfo(itemStack, blockPos);
	}

	public BlockPos getBlockPos() {
		return blockPos;
	}

	public ItemStack getStack() {
		return itemStack;
	}
}
