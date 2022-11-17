package dev.sterner.legemeton.common.block;

import dev.sterner.legemeton.api.enums.HorizontalDoubleBlockHalf;
import dev.sterner.legemeton.common.block.entity.NecroTableBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class NecroTableBlock extends Block implements BlockEntityProvider {
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	public static final EnumProperty<HorizontalDoubleBlockHalf> HHALF = EnumProperty.of("half", HorizontalDoubleBlockHalf.class);

	public NecroTableBlock(Settings settings) {
		super(settings.nonOpaque());
		this.stateManager
				.getDefaultState()
				.with(FACING, Direction.NORTH)
				.with(HHALF, HorizontalDoubleBlockHalf.RIGHT);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		if(state.get(HHALF) == HorizontalDoubleBlockHalf.LEFT){
			Direction targetDirection = state.get(FACING).rotateClockwise(Direction.Axis.Y);
			BlockPos targetPos = pos.offset(targetDirection);
			return world.getBlockState(targetPos).isOf(this);
		}else{
			Direction targetDirection = state.get(FACING).rotateCounterclockwise(Direction.Axis.Y);
			BlockPos targetPos = pos.offset(targetDirection);
			return world.getBlockState(targetPos).isOf(this);
		}
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockPos targetPos;
		if(state.get(HHALF) == HorizontalDoubleBlockHalf.LEFT){
			Direction targetDirection = state.get(FACING).rotateClockwise(Direction.Axis.Y);
			targetPos = pos.offset(targetDirection);
		}else{
			Direction targetDirection = state.get(FACING).rotateCounterclockwise(Direction.Axis.Y);
			targetPos = pos.offset(targetDirection);
		}
		world.breakBlock(targetPos, false);
		world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, targetPos, Block.getRawIdFromState(world.getBlockState(targetPos)));
		super.onBreak(world, pos, state, player);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random) {
		if (!state.canPlaceAt(world, pos)) {
			world.breakBlock(pos, true);
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(HHALF, FACING);
	}



	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new NecroTableBlockEntity(pos, state);
	}
}
