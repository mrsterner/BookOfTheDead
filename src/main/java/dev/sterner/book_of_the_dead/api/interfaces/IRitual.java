package dev.sterner.book_of_the_dead.api.interfaces;

import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IRitual {

	/**
	 * @param world       world
	 * @param blockPos    ritual origin
	 * @param blockEntity ritualBlockEntity
	 */
	void tick(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity);

	/**
	 * Should run once after ritual has ended
	 *
	 * @param world       world
	 * @param blockPos    ritual origin
	 * @param blockEntity ritualBlockEntity
	 */
	void onStopped(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity);
}
