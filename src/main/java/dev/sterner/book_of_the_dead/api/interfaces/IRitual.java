package dev.sterner.book_of_the_dead.api.interfaces;

import dev.sterner.book_of_the_dead.common.block.entity.RitualBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IRitual {
	void onStart(World world, BlockPos blockPos, RitualBlockEntity blockEntity);

	void tick(World world, BlockPos blockPos, RitualBlockEntity blockEntity);

	void onStopped(World world, BlockPos blockPos, RitualBlockEntity blockEntity);
}
