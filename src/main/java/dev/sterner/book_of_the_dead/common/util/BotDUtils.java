package dev.sterner.book_of_the_dead.common.util;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BotDUtils {

	/**
	 * Try to add an itemStack to a players Hand, otherwise drops the item
	 *
	 * @param player player
	 * @param hand   hand to receive item
	 * @param toAdd  item to add
	 */
	public static void addItemToInventoryAndConsume(PlayerEntity player, Hand hand, ItemStack toAdd) {
		ItemStack stack = player.getStackInHand(hand);
		if (stack.getCount() == 1 && !player.isCreative()) {
			player.setStackInHand(hand, toAdd);
			return;
		}
		stack.decrement(1);
		try {
			if (!player.getInventory().insertStack(toAdd)) {
				player.dropItem(toAdd, false, true);
			}
		} finally {
			if (stack.isEmpty()) {
				player.setStackInHand(hand, ItemStack.EMPTY);
			} else {
				player.setStackInHand(hand, stack);
			}
		}
	}

	/**
	 * Rotates a VoxelShape on any axis a certain amount of times
	 *
	 * @param times amount of times to rotate
	 * @param shape the shape input
	 * @param axis which axis to rotate on
	 * @return new VoxelShape after rotation
	 */
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

	/**
	 * Rotates the VoxelShape on y-axis
	 *
	 * @param from direction
	 * @param to direction
	 * @param shape shape
	 * @return new voxel shape for the new direction
	 */
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

	/**
	 * Parse an nbt to a vec3d
	 *
	 * @param compound nbt
	 * @return vec3d
	 */
	public static Vec3d toVec3d(NbtCompound compound) {
		return new Vec3d(compound.getDouble("X"), compound.getDouble("Y"), compound.getDouble("Z"));
	}

	/**
	 * Make a nbt from a Vec3d
	 *
	 * @param pos vec3d position
	 * @return nbt
	 */
	public static NbtCompound fromVec3d(Vec3d pos) {
		NbtCompound nbtCompound = new NbtCompound();
		nbtCompound.putDouble("X", pos.getX());
		nbtCompound.putDouble("Y", pos.getY());
		nbtCompound.putDouble("Z", pos.getZ());
		return nbtCompound;
	}

	public static void writeChancesNbt(NbtCompound nbt, DefaultedList<Float> floats) {
		NbtList nbtList = new NbtList();
		for (float aFloat : floats) {
			NbtCompound nbtCompound = new NbtCompound();
			nbtCompound.putFloat(Constants.Nbt.CHANCE, aFloat);
			nbtList.add(nbtCompound);
		}
		nbt.put(Constants.Nbt.CHANCES, nbtList);
	}

	public static void readChanceNbt(NbtCompound nbt, DefaultedList<Float> floats) {
		NbtList nbtList = nbt.getList(Constants.Nbt.CHANCES, NbtElement.COMPOUND_TYPE);
		for (int i = 0; i < nbtList.size(); ++i) {
			NbtCompound nbtCompound = nbtList.getCompound(i);
			float j = nbtCompound.getFloat(Constants.Nbt.CHANCE);
			floats.set(i, j);
		}
	}

	/**
	 * Gets the closest entity of a specific type
	 *
	 * @param entityList list of entities to test
	 * @param entityType       entityType to look for
	 * @param pos        position to measure distance from
	 * @return Entity closest to pos from entityList
	 */

	@Nullable
	public static <T extends LivingEntity> T getClosestEntity(List<? extends T> entityList, EntityType<?> entityType, BlockPos pos) {
		Optional<? extends T> closestEntity = entityList.stream().filter(entity -> entity.getType() == entityType)
				.min(Comparator.comparingDouble(entity -> entity.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ())));
		return closestEntity.orElse(null);
	}
}
