package dev.sterner.book_of_the_dead.common.block;

import dev.sterner.book_of_the_dead.api.block.HorizontalDoubleBlock;
import dev.sterner.book_of_the_dead.api.enums.HorizontalDoubleBlockHalf;
import dev.sterner.book_of_the_dead.common.block.entity.ButcherTableBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ButcherBlock extends HorizontalDoubleBlock implements BlockEntityProvider {
	protected static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 16, 16);
	protected static final VoxelShape EXTRA_SHAPE = Block.createCuboidShape(5, 16.0, 5, 11, 18, 11);

	public ButcherBlock(Settings settings) {
		super(settings.nonOpaque());
		this.stateManager
				.getDefaultState()
				.with(FACING, Direction.NORTH)
				.with(HorizontalDoubleBlock.HHALF, HorizontalDoubleBlockHalf.RIGHT);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return (tickerWorld, pos, tickerState, blockEntity) -> ButcherTableBlockEntity.tick(tickerWorld, pos, tickerState, (ButcherTableBlockEntity) blockEntity);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient()) {
			if(world.getBlockEntity(pos) instanceof ButcherTableBlockEntity butcherTableBlockEntity){
				return butcherTableBlockEntity.onUse(world, state, pos, player, hand);
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ButcherTableBlockEntity(pos, state);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if(world.getBlockEntity(pos) instanceof ButcherTableBlockEntity be){
			if(!be.getCorpseEntity().isEmpty()){
				return VoxelShapes.union(SHAPE, EXTRA_SHAPE);
			}
		}
		return SHAPE;
	}
}
