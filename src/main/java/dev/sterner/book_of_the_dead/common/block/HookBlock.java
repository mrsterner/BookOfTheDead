package dev.sterner.book_of_the_dead.common.block;

import dev.sterner.book_of_the_dead.common.block.entity.HookBlockEntity;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.registry.BotDParticleTypes;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;


public class HookBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	protected static final VoxelShape SHAPE = Block.createCuboidShape(5, 2.0, 5, 11, 16.0, 11);
	protected static final VoxelShape HOOKED_SHAPE = Block.createCuboidShape(4, 0.0, 4, 12, 16.0, 12);
	public boolean isMetal;

	public HookBlock(Settings settings, boolean isMetal) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
		this.isMetal = isMetal;
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, RandomGenerator random) {
		super.randomDisplayTick(state, world, pos, random);
		if(world.getBlockEntity(pos) instanceof HookBlockEntity hookBlockEntity && !hookBlockEntity.getCorpseEntity().isEmpty() && hookBlockEntity.hookedAge < Constants.Values.BLEEDING){
			for (int i = 0; i < random.nextInt(1) + 1; ++i) {
				this.trySpawnDripParticles(world, pos, state);
			}
		}
	}

	private void trySpawnDripParticles(World world, BlockPos pos, BlockState state) {
		if (state.getFluidState().isEmpty() && !(world.random.nextFloat() < 0.3F)) {
			VoxelShape voxelshape = state.getCollisionShape(world, pos);
			double d0 = voxelshape.getMax(Direction.Axis.Y);
			if (d0 >= 1.0D && !state.isIn(BlockTags.IMPERMEABLE)) {
				double d1 = voxelshape.getMin(Direction.Axis.Y);
				if (d1 > 0.0D) {
					this.spawnParticle(world, pos, voxelshape, (double) pos.getY() + d1 - 0.05D);
				} else {
					BlockPos blockpos = pos.down();
					BlockState blockstate = world.getBlockState(blockpos);
					VoxelShape voxelshape1 = blockstate.getCollisionShape(world, blockpos);
					double d2 = voxelshape1.getMax(Direction.Axis.Y);
					if ((d2 < 1.0D || !blockstate.isFullCube(world, blockpos)) && blockstate.getFluidState().isEmpty()) {
						this.spawnParticle(world, pos, voxelshape, (double) pos.getY() - 0.05D);
					}
				}
			}

		}
	}

	private void spawnParticle(World world, BlockPos pos, VoxelShape shape, double y) {
		this.spawnFluidParticle(world, (double) pos.getX() + shape.getMin(Direction.Axis.X), (double) pos.getX() + shape.getMax(Direction.Axis.X), (double) pos.getZ() + shape.getMin(Direction.Axis.Z), (double) pos.getZ() + shape.getMax(Direction.Axis.Z), y);
	}

	private void spawnFluidParticle(World world, double minX, double maxX, double minZ, double maxZ, double y) {
		world.addParticle(BotDParticleTypes.DRIPPING_BLOOD, MathHelper.lerp(world.random.nextDouble(), minX, maxX), y, MathHelper.lerp(world.random.nextDouble(), minZ, maxZ), 0.0D, 0.0D, 0.0D);
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
				return hookBlockEntity.onUse(world, state, pos, player, hand, false);
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
			world.scheduleBlockTick(pos, BotDObjects.ROPE, 1);
		}
		if (direction != Direction.DOWN || !neighborState.isOf(BotDObjects.ROPE) && !neighborState.isOf(BotDObjects.ROPE)) {
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
		return world.getBlockState(pos.up()).isSolidBlock(world, pos) || world.getBlockState(pos.up()).isOf(BotDObjects.ROPE);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if(world.getBlockEntity(pos) instanceof HookBlockEntity hookBlockEntity && !hookBlockEntity.getCorpseEntity().isEmpty()){
			return HOOKED_SHAPE;
		}
		return SHAPE;
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
