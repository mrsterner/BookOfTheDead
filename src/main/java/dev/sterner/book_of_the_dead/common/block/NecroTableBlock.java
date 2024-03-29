package dev.sterner.book_of_the_dead.common.block;

import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.ToIntFunction;

public class NecroTableBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	protected static final VoxelShape WEST_SHAPE, NORTH_SHAPE, EAST_SHAPE, SOUTH_SHAPE;
	protected static final Vec3d[] NORTH_PARTICLES, SOUTH_PARTICLES, WEST_PARTICLES, EAST_PARTICLES;

	public static final BooleanProperty LIT = Properties.LIT;
	public static final ToIntFunction<BlockState> STATE_TO_LUMINANCE = state -> state.get(LIT) ? 10 : 0;

	public NecroTableBlock(Settings settings) {
		super(settings.nonOpaque().luminance(STATE_TO_LUMINANCE));
		this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(LIT, Boolean.FALSE);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		PlayerEntity player = ctx.getPlayer();
		return this.getDefaultState().with(FACING, player != null && player.isSneaking() ? ctx.getPlayerFacing().getOpposite() : ctx.getPlayerFacing()).with(LIT, false);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return (tickerWorld, pos, tickerState, blockEntity) -> {
			if (blockEntity instanceof NecroTableBlockEntity be) {
				be.tick(world, pos, state);
			}
		};
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient()) {
			if (world.getBlockEntity(pos) instanceof NecroTableBlockEntity necroTableBlockEntity) {
				return necroTableBlockEntity.onUse(world, state, pos, player, hand);
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		Direction direction = state.get(HorizontalFacingBlock.FACING);
		var NS = VoxelShapes.combine(NORTH_SHAPE.offset(0.5D, 0, 0), SOUTH_SHAPE.offset(-0.5D, 0, 0), BooleanBiFunction.OR);
		var EW = VoxelShapes.combine(EAST_SHAPE.offset(0D, 0, 0.5), WEST_SHAPE.offset(0D, 0, -0.5), BooleanBiFunction.OR);
		return switch (direction) {
			case EAST, WEST -> EW;
			default -> NS;
		};
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (world.getBlockEntity(pos) instanceof NecroTableBlockEntity be) {
			if (be.hasBotD) {
				ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), BotDObjects.BOOK_OF_THE_DEAD.asItem().getDefaultStack());
			}
			if (be.hasEmeraldTablet) {
				ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), BotDObjects.EMERALD_TABLET.asItem().getDefaultStack());
			}
		}
		super.onBreak(world, pos, state, player);
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, RandomGenerator random) {
		if (state.get(LIT)) {
			Vec3d[] vec3ds = switch (state.get(FACING)) {
				case NORTH -> NORTH_PARTICLES;
				case SOUTH -> SOUTH_PARTICLES;
				case WEST -> WEST_PARTICLES;
				default -> EAST_PARTICLES;
			};

			spawnCandleParticles(world, vec3ds[0].add(pos.getX(), pos.getY(), pos.getZ()), random);
			spawnCandleParticles(world, vec3ds[1].add(pos.getX(), pos.getY(), pos.getZ()), random);
			spawnCandleParticles(world, vec3ds[2].add(pos.getX(), pos.getY(), pos.getZ()), random);

			spawnCandleParticles(world, vec3ds[3].add(pos.getX(), pos.getY(), pos.getZ()), random);
			spawnCandleParticles(world, vec3ds[4].add(pos.getX(), pos.getY(), pos.getZ()), random);
		}
	}

	private static void spawnCandleParticles(World world, Vec3d vec3d, RandomGenerator random) {
		float f = random.nextFloat();
		if (f < 0.3F) {
			world.addParticle(ParticleTypes.SMOKE, vec3d.x, vec3d.y, vec3d.z, 0.0, 0.0, 0.0);
			if (f < 0.17F) {
				world.playSound(vec3d.x + 0.5, vec3d.y + 0.5, vec3d.z + 0.5, SoundEvents.BLOCK_CANDLE_AMBIENT, SoundCategory.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
			}
		}
		world.addParticle(ParticleTypes.SMALL_FLAME, vec3d.x, vec3d.y, vec3d.z, 0.0, 0.0, 0.0);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(LIT).add(FACING);
		super.appendProperties(builder);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new NecroTableBlockEntity(pos, state);
	}

	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(Blocks.DEEPSLATE_TILES);
	}

	static {
		WEST_SHAPE = VoxelShapes.union(createCuboidShape(0, 0, 4, 16, 3, 16), createCuboidShape(2, 0, 6, 14, 10, 16), createCuboidShape(1, 10, 5, 15, 13, 16), createCuboidShape(-2, 13, 2, 18, 16, 16));
		NORTH_SHAPE = VoxelShapes.union(createCuboidShape(0, 0, 0, 12, 3, 16), createCuboidShape(0, 0, 2, 10, 10, 14), createCuboidShape(0, 10, 1, 11, 13, 15), createCuboidShape(0, 13, -2, 14, 16, 18));
		EAST_SHAPE = VoxelShapes.union(createCuboidShape(0, 0, 0, 16, 3, 12), createCuboidShape(2, 0, 0, 14, 10, 10), createCuboidShape(1, 10, 0, 15, 13, 11), createCuboidShape(-2, 13, 0, 18, 16, 14));
		SOUTH_SHAPE = VoxelShapes.union(createCuboidShape(4, 0, 0, 16, 3, 16), createCuboidShape(6, 0, 2, 16, 10, 14), createCuboidShape(5, 10, 1, 16, 13, 15), createCuboidShape(2, 13, -2, 16, 16, 18));

		NORTH_PARTICLES = new Vec3d[]{
			new Vec3d(1.25, 1.2, 0.45),
			new Vec3d(1.05, 1.33, 0.35),
			new Vec3d(1.2, 1.45, 0.25),

			new Vec3d(0.1, 1.33, 0.20),
			new Vec3d(0.15, 1.2, 0.35)
		};
		SOUTH_PARTICLES = new Vec3d[]{
			new Vec3d(-0.25, 1.2, 0.55),
			new Vec3d(-0.05, 1.33, 0.65),
			new Vec3d(-0.2, 1.45, 0.75),

			new Vec3d(0.92, 1.33, 0.78),
			new Vec3d(0.88, 1.2, 0.65)
		};
		EAST_PARTICLES = new Vec3d[]{
			new Vec3d(0.55, 1.2, 1.25),
			new Vec3d(0.65, 1.33, 1.05),
			new Vec3d(0.75, 1.45, 1.2),

			new Vec3d(0.65, 1.2, 0.15),
			new Vec3d(0.78, 1.33, 0.10)
		};
		WEST_PARTICLES = new Vec3d[]{
			new Vec3d(0.45, 1.2, -0.25),
			new Vec3d(0.35, 1.33, -0.05),
			new Vec3d(0.25, 1.45, -0.2),

			new Vec3d(0.20, 1.33, 0.9),
			new Vec3d(0.35, 1.2, 0.85)
		};
	}
}
