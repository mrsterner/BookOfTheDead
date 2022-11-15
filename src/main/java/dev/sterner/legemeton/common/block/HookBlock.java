package dev.sterner.legemeton.common.block;

import dev.sterner.legemeton.common.block.entity.HookBlockEntity;
import dev.sterner.legemeton.common.registry.LegemetonObjects;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class HookBlock extends Block implements BlockEntityProvider {
	protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(5, 2.0, 5, 11, 16.0, 11);
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

	public HookBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return (tickerWorld, pos, tickerState, blockEntity) -> HookBlockEntity.tick(tickerWorld, pos, tickerState, (HookBlockEntity) blockEntity);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient()) {
			if(world.getBlockEntity(pos) instanceof HookBlockEntity hookBlockEntity){
				hookBlockEntity.onUse(player, hand);
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random) {
		if (!state.canPlaceAt(world, pos)) {
			world.breakBlock(pos, true);
		}
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (direction == Direction.UP && !state.canPlaceAt(world, pos)) {
			world.scheduleBlockTick(pos, LegemetonObjects.ROPE, 1);
		}
		if (direction != Direction.DOWN || !neighborState.isOf(LegemetonObjects.ROPE) && !neighborState.isOf(LegemetonObjects.ROPE)) {
			return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
		} else {
			return this.copyState(state, this.getDefaultState());
		}
	}

	protected BlockState copyState(BlockState from, BlockState to) {
		return to;
	}


	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos.up()).isSolidBlock(world, pos) || world.getBlockState(pos.up()).isOf(LegemetonObjects.ROPE);
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
