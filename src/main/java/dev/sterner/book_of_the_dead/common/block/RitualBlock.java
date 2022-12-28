package dev.sterner.book_of_the_dead.common.block;

import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import dev.sterner.book_of_the_dead.common.block.entity.RitualBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RitualBlock extends BlockWithEntity {
	protected static final VoxelShape OUTLINE_SHAPE = VoxelShapes.union(createCuboidShape(0D, 0D, 0D, 16D, 2D, 16D));

	public RitualBlock(Settings settings) {
		super(settings.nonOpaque());
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return OUTLINE_SHAPE;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new RitualBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return (tickerWorld, pos, tickerState, blockEntity) -> RitualBlockEntity.tick(tickerWorld, pos, tickerState, (RitualBlockEntity) blockEntity);
	}
}
