package dev.sterner.book_of_the_dead.common.block;

import dev.sterner.book_of_the_dead.api.block.HorizontalDoubleBlock;
import dev.sterner.book_of_the_dead.api.enums.HorizontalDoubleBlockHalf;
import dev.sterner.book_of_the_dead.common.block.entity.ButcherTableBlockEntity;
import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ButcherBlock extends HorizontalDoubleBlock implements BlockEntityProvider {
	protected static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 16, 16);

	public ButcherBlock(Settings settings) {
		super(settings.nonOpaque());
		this.stateManager
				.getDefaultState()
				.with(FACING, Direction.NORTH)
				.with(HorizontalDoubleBlock.HHALF, HorizontalDoubleBlockHalf.RIGHT);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient()) {
			if (world.getBlockEntity(pos) instanceof ButcherTableBlockEntity butcherTableBlockEntity) {
				if (state.get(HorizontalDoubleBlock.HHALF) == HorizontalDoubleBlockHalf.RIGHT) {
					return butcherTableBlockEntity.onUse(world, state, pos, player, hand, false);
				} else {
					ButcherTableBlockEntity nButch = getNeighbourButcherBlockEntity(world, state, pos);
					if (nButch != null) {
						return nButch.onUse(world, state, pos, player, hand, true);
					}
				}
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return (tickerWorld, pos, tickerState, blockEntity) -> {
			if(blockEntity instanceof ButcherTableBlockEntity be){
				be.tick(world, pos, state);
			}
		};
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ButcherTableBlockEntity(pos, state);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	public static ButcherTableBlockEntity getNeighbourButcherBlockEntity(World world, BlockState blockState, BlockPos blockPos) {
		Direction targetDirection = blockState.get(FACING).rotateClockwise(Direction.Axis.Y);
		blockPos = blockPos.offset(targetDirection);
		if (world.getBlockEntity(blockPos) instanceof ButcherTableBlockEntity butcherTableBlockEntity1) {
			return butcherTableBlockEntity1;
		}
		return null;
	}

	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(Blocks.DARK_OAK_PLANKS);
	}
}
