package dev.sterner.book_of_the_dead.common.block;

import dev.sterner.book_of_the_dead.common.block.entity.RetortFlaskBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.state.property.Properties.LIT;


public class RetortFlaskBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	protected static final VoxelShape SHAPE = VoxelShapes.union(createCuboidShape(4.5, 4, 4.5, 11.5, 12, 11.5));
	public RetortFlaskBlock(Settings settings) {
		super(settings.nonOpaque());
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(Properties.WATERLOGGED, false).with(LIT, false));
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return (tickerWorld, pos, tickerState, blockEntity) -> RetortFlaskBlockEntity.tick(tickerWorld, pos, tickerState, (RetortFlaskBlockEntity) blockEntity);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient()) {
			if(world.getBlockEntity(pos) instanceof RetortFlaskBlockEntity retortFlaskBlockEntity){
				return retortFlaskBlockEntity.onUse(world, state, pos, player, hand);
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (state.get(Properties.WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
			state = state.with(LIT, false);
		}
		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

	@Override
	public BlockRenderType getRenderType(BlockState blockState) {
		return BlockRenderType.INVISIBLE;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new RetortFlaskBlockEntity(pos, state);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		PlayerEntity player = ctx.getPlayer();
		return this.getDefaultState().with(Properties.WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER).with(FACING, player != null && player.isSneaking() ? ctx.getPlayerFacing().getOpposite() : ctx.getPlayerFacing()).with(LIT, false);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.WATERLOGGED, FACING, LIT);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
}
