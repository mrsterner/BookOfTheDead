package dev.sterner.book_of_the_dead.api.interfaces;

import dev.sterner.book_of_the_dead.common.block.entity.RitualBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IRitual {
	/**
	 * Should run once at the initiation of the ritual
	 * @param world world
	 * @param blockPos ritual origin
	 * @param blockEntity ritualBlockEntity
	 */
	void onStart(World world, BlockPos blockPos, RitualBlockEntity blockEntity);

	/**
	 * Should run after {@link IRitual#onStart(World, BlockPos, RitualBlockEntity)} continuously until ritual duration is up
	 * @param world world
	 * @param blockPos ritual origin
	 * @param blockEntity ritualBlockEntity
	 */
	void tick(World world, BlockPos blockPos, RitualBlockEntity blockEntity);

	/**
	 * Should run once after ritual has ended
	 * @param world world
	 * @param blockPos ritual origin
	 * @param blockEntity ritualBlockEntity
	 */
	void onStopped(World world, BlockPos blockPos, RitualBlockEntity blockEntity);
}
