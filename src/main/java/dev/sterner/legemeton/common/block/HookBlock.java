package dev.sterner.legemeton.common.block;

import dev.sterner.legemeton.common.block.entity.HookBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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
			HookBlockEntity blockEntity = (HookBlockEntity) world.getBlockEntity(pos);
			if(blockEntity != null){
				blockEntity.markDirty();
				blockEntity.sync();
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
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
