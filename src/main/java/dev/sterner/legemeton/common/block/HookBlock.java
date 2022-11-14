package dev.sterner.legemeton.common.block;

import dev.sterner.legemeton.common.block.entity.HookBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class HookBlock extends Block implements BlockEntityProvider {
	protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(5, 2.0, 5, 11, 16.0, 11);
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

	public HookBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return NORTH_SHAPE;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new HookBlockEntity(pos, state);
	}
}
