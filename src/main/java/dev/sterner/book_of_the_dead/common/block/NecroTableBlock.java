package dev.sterner.book_of_the_dead.common.block;

import dev.sterner.book_of_the_dead.api.enums.HorizontalDoubleBlockHalf;
import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.function.ToIntFunction;

public class NecroTableBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	protected static final VoxelShape WEST_SHAPE = VoxelShapes.union(
			createCuboidShape(0,0,4,16,3,16),
			createCuboidShape(2,0,6,14,10,16),
			createCuboidShape(1,10,5,15,13,16),
			createCuboidShape(-2,13,2,18,16,16)
	);

	protected static final VoxelShape NORTH_SHAPE = VoxelShapes.union(
			createCuboidShape(0,0,0,12,3,16),
			createCuboidShape(0,0,2,10,10,14),
			createCuboidShape(0,10,1,11,13,15),
			createCuboidShape(0,13,-2,14,16,18)
	);

	protected static final VoxelShape EAST_SHAPE = VoxelShapes.union(
			createCuboidShape(0,0,0,16,3,12),
			createCuboidShape(2,0,0,14,10,10),
			createCuboidShape(1,10,0,15,13,11),
			createCuboidShape(-2,13,0,18,16,14)
	);

	protected static final VoxelShape SOUTH_SHAPE = VoxelShapes.union(
			createCuboidShape(4,0,0,16,3,16),
			createCuboidShape(6,0,2,16,10,14),
			createCuboidShape(5,10,1,16,13,15),
			createCuboidShape(2,13,-2,16,16,18)
	);

	public static final BooleanProperty LIT = Properties.LIT;
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	public static final EnumProperty<HorizontalDoubleBlockHalf> HHALF = EnumProperty.of("half", HorizontalDoubleBlockHalf.class);
	public static final ToIntFunction<BlockState> STATE_TO_LUMINANCE = state -> state.get(LIT) ? 14 : 0;

	public NecroTableBlock(Settings settings) {
		super(settings.nonOpaque().luminance(STATE_TO_LUMINANCE));
		this.stateManager
				.getDefaultState()
				.with(FACING, Direction.NORTH)
				.with(HHALF, HorizontalDoubleBlockHalf.RIGHT)
				.with(LIT, Boolean.FALSE);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return (tickerWorld, pos, tickerState, blockEntity) -> NecroTableBlockEntity.tick(tickerWorld, pos, tickerState, (NecroTableBlockEntity) blockEntity);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient()) {
			if(world.getBlockEntity(pos) instanceof NecroTableBlockEntity necroTableBlockEntity){
				return necroTableBlockEntity.onUse(world, state, pos, player, hand);
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		Direction direction = state.get(FACING);
		boolean bl = state.get(HHALF) == HorizontalDoubleBlockHalf.RIGHT;
		return switch (direction) {
			case NORTH -> bl ? NORTH_SHAPE : SOUTH_SHAPE;
			case SOUTH -> bl ? SOUTH_SHAPE : NORTH_SHAPE;
			case EAST -> bl ? EAST_SHAPE : WEST_SHAPE;
			case WEST -> bl ? WEST_SHAPE : EAST_SHAPE;
			default -> NORTH_SHAPE;
		};


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
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, RandomGenerator random) {
		if (state.get(LIT)) {

			if(state.get(HHALF) == HorizontalDoubleBlockHalf.RIGHT){
				var offset1 = new Vec3d(0.45, 1.33, 0.65);
				var offset2 = new Vec3d(0.25, 1.2, 0.55);
				var offset3 = new Vec3d(0.3, 1.45, 0.75);

				spawnCandleParticles(world, offset1.add(pos.getX(), pos.getY(), pos.getZ()), random);
				spawnCandleParticles(world, offset2.add(pos.getX(), pos.getY(), pos.getZ()), random);
				spawnCandleParticles(world, offset3.add(pos.getX(), pos.getY(), pos.getZ()), random);
			}

		}
	}

	private static void spawnCandleParticles(World world, Vec3d vec3d, RandomGenerator random) {
		float f = random.nextFloat();
		if (f < 0.3F) {
			world.addParticle(ParticleTypes.SMOKE, vec3d.x, vec3d.y, vec3d.z, 0.0, 0.0, 0.0);
			if (f < 0.17F) {
				world.playSound(
						vec3d.x + 0.5,
						vec3d.y + 0.5,
						vec3d.z + 0.5,
						SoundEvents.BLOCK_CANDLE_AMBIENT,
						SoundCategory.BLOCKS,
						1.0F + random.nextFloat(),
						random.nextFloat() * 0.7F + 0.3F,
						false
				);
			}
		}

		world.addParticle(ParticleTypes.SMALL_FLAME, vec3d.x, vec3d.y, vec3d.z, 0.0, 0.0, 0.0);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(HHALF, FACING, LIT);
	}



	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new NecroTableBlockEntity(pos, state);
	}
}
