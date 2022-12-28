package dev.sterner.book_of_the_dead.common.block;

import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.minecraft.block.*;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class PoppyCropBlock extends CropBlock {
	private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 10.0, 16.0),

			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 10.0, 16.0),
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0),
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0),
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0),
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0)
	};

	public PoppyCropBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	protected ItemConvertible getSeedsItem() {
		return BotDObjects.POPPY_SEEDS;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return AGE_TO_SHAPE[state.get(this.getAgeProperty())];
	}
}
