package dev.sterner.book_of_the_dead.common.block;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class RopeBlock extends Block {

	protected static final VoxelShape MIDDLE_SHAPE = Block.createCuboidShape(6.5, 0.0, 6.5, 9.5, 16.0, 9.5);
	protected static final VoxelShape BOTTOM_SHAPE = Block.createCuboidShape(6.5, 2.0, 6.5, 9.5, 16.0, 9.5);
	public static final EnumProperty<Rope> ROPE = EnumProperty.of("rope", Rope.class);
	public RopeBlock(Settings settings) {
		super(settings);
		this.stateManager.getDefaultState().with(ROPE, Rope.BOTTOM);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos.up()).isSolidBlock(world, pos) || world.getBlockState(pos.up()).isOf(this);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random) {
		if (!state.canPlaceAt(world, pos)) {
			world.breakBlock(pos, true);
		}
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(ROPE, Rope.BOTTOM);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (direction == Direction.UP && !state.canPlaceAt(world, pos)) {
			world.scheduleBlockTick(pos, this, 1);
		}
		if (direction != Direction.DOWN || !neighborState.isOf(this) && !neighborState.isOf(this)) {
			return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
		} else {
			return this.copyState(state, this.getDefaultState());
		}
	}

	protected BlockState copyState(BlockState from, BlockState to) {
		return to;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		boolean bl2 = state.get(ROPE) == Rope.MIDDLE;
		return bl2 ? MIDDLE_SHAPE : BOTTOM_SHAPE;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(ROPE);
	}

	public enum Rope implements StringIdentifiable {
		MIDDLE,
		BOTTOM;

		public String toString() {
			return this.asString();
		}

		@Override
		public String asString() {
			return this == MIDDLE ? "middle" : "bottom";
		}
	}
}

