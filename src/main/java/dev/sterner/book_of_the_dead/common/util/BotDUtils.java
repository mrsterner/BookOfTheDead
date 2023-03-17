package dev.sterner.book_of_the_dead.common.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

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

	public static VoxelShape rotateShape(int times, VoxelShape shape, char axis) {
		VoxelShape[] buffer = new VoxelShape[]{shape, VoxelShapes.empty()};
		VoxelShape emptyShape = VoxelShapes.empty();
		for (int i = 0; i < times; i++) {
			buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> {
				switch (axis) {
					case 'x' -> buffer[1] = VoxelShapes.combine(buffer[1],
							VoxelShapes.cuboid(minX, 1 - maxZ, minY, maxX, 1 - minZ, maxY), BooleanBiFunction.OR);
					case 'y' -> buffer[1] = VoxelShapes.combine(buffer[1],
							VoxelShapes.cuboid(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX), BooleanBiFunction.OR);
					case 'z' -> buffer[1] = VoxelShapes.combine(buffer[1],
							VoxelShapes.cuboid(minY, minX, 1 - maxZ, maxY, maxX, 1 - minZ), BooleanBiFunction.OR);
					default -> throw new IllegalArgumentException("Invalid axis argument: " + axis);
				}
			});
			buffer[0] = buffer[1];
			buffer[1] = emptyShape;
		}
		return buffer[0];
	}

	public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
		VoxelShape[] buffer = new VoxelShape[]{shape, VoxelShapes.empty()};

		int times = (to.getHorizontal() - from.getHorizontal() + 4) % 4;
		for (int i = 0; i < times; i++) {
			buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.combine(buffer[1], VoxelShapes.cuboid(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX), BooleanBiFunction.OR));
			buffer[0] = buffer[1];
			buffer[1] = VoxelShapes.empty();
		}
		return buffer[0];
	}

	public static Vec3d toVec3d(NbtCompound compound) {
		return new Vec3d(compound.getDouble("X"), compound.getDouble("Y"), compound.getDouble("Z"));
	}

	public static NbtCompound fromVec3d(Vec3d pos) {
		NbtCompound nbtCompound = new NbtCompound();
		nbtCompound.putDouble("X", pos.getX());
		nbtCompound.putDouble("Y", pos.getY());
		nbtCompound.putDouble("Z", pos.getZ());
		return nbtCompound;
	}
}
