package dev.sterner.legemeton.common.block;

import dev.sterner.legemeton.common.block.entity.JarBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class JarBlock extends BlockWithEntity {
	protected static final VoxelShape OPEN_SHAPE = VoxelShapes.union(
			createCuboidShape(4,0,4,12,11,5),
			createCuboidShape(4,0,4,5,11,12),
			createCuboidShape(4, 0, 11, 12, 11, 12),
			createCuboidShape(11, 0, 5, 12 ,11, 12),
			createCuboidShape(5,11,5,11,12,6),
			createCuboidShape(5,11,5,6,12,11),
			createCuboidShape(10, 11, 5, 11, 12, 11),
			createCuboidShape(5, 11, 10, 11 ,12, 11),
			createCuboidShape(4.5,12,4.5,11.5,14,5.5),
			createCuboidShape(4.5,12,4.5,5.5,14,11.5),
			createCuboidShape(4.5,12,10.5,11.5,14,11.5),
			createCuboidShape(10.5,12,4.5,11.5,14,11.5)
	);

	protected static final VoxelShape CLOSED_SHAPE = VoxelShapes.union(createCuboidShape(5, 14, 5, 11, 16, 11));
	public static final BooleanProperty OPEN = BooleanProperty.of("open");

	public JarBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(OPEN,false));
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return (tickerWorld, pos, tickerState, blockEntity) -> JarBlockEntity.tick(tickerWorld, pos, tickerState, (JarBlockEntity) blockEntity);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if(!world.isClient() && hand == Hand.MAIN_HAND){
			world.setBlockState(pos, state.with(OPEN, !state.get(OPEN)));
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new JarBlockEntity(pos, state);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(OPEN);
	}


	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return state.get(OPEN) ? OPEN_SHAPE : VoxelShapes.union(CLOSED_SHAPE, OPEN_SHAPE);
	}
}
