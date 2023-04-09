package dev.sterner.book_of_the_dead.common.block;

import dev.sterner.book_of_the_dead.common.block.entity.TabletBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class TabletBlock extends BookBlock {
	public TabletBlock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TabletBlockEntity(pos, state);
	}
}
