package dev.sterner.book_of_the_dead.common.block;

import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
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
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;


public class CandleWallBlock extends Block {
	public static final IntProperty HEIGHT = IntProperty.of("height", 0, 4);
	public static final float[] PARTICLE_HEIGHT = {0, 10 / 16f, 13 / 16f, 15 / 16f, 17 / 16f};
	public static final float[] VOXEL_HEIGHT = {4, 8, 11, 13, 15};

	public CandleWallBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this.stateManager
				.getDefaultState()
				.with(Properties.LIT, false)
				.with(HEIGHT, 4)
				.with(Properties.HORIZONTAL_FACING, Direction.NORTH)
		);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack handStack = player.getMainHandStack();
		if (hand == Hand.MAIN_HAND) {
			if (handStack.isOf(BotDObjects.FAT) || handStack.isOf(Items.HONEYCOMB)) {
				if (!state.get(Properties.LIT) && state.get(HEIGHT) < 4) {
					world.setBlockState(pos, state.with(HEIGHT, state.get(HEIGHT) + 1));
					if (!player.isCreative()) {
						handStack.decrement(1);
					}
					return ActionResult.CONSUME;
				}
			} else if (handStack.isEmpty() && state.get(Properties.LIT)) {
				world.setBlockState(pos, state.with(Properties.LIT, false));
				world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1.0F, 1.0F);

			} else if (state.get(HEIGHT) > 0 && (handStack.isOf(Items.FLINT_AND_STEEL) || handStack.isOf(Items.FIRE_CHARGE))) {
				world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
				world.setBlockState(pos, state.with(Properties.LIT, true), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
				world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
				handStack.damage(1, player, p -> p.sendToolBreakStatus(hand));
				return ActionResult.CONSUME;
			}
		}

		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, VOXEL_HEIGHT[state.get(HEIGHT)], 10.0);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction direction = state.get(Properties.HORIZONTAL_FACING);
		BlockPos blockPos = pos.offset(direction.getOpposite());
		BlockState blockState = world.getBlockState(blockPos);
		return blockState.isSideSolidFullSquare(world, blockPos, direction);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState blockState = this.getDefaultState();
		WorldView worldView = ctx.getWorld();
		BlockPos blockPos = ctx.getBlockPos();
		Direction[] directions = ctx.getPlacementDirections();

		for (Direction direction : directions) {
			if (direction.getAxis().isHorizontal()) {
				Direction direction2 = direction.getOpposite();
				blockState = blockState.with(Properties.HORIZONTAL_FACING, direction2);
				if (blockState.canPlaceAt(worldView, blockPos)) {
					return blockState;
				}
			}
		}

		return null;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return direction.getOpposite() == state.get(Properties.HORIZONTAL_FACING) && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : state;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.LIT, HEIGHT, Properties.HORIZONTAL_FACING);
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(Properties.HORIZONTAL_FACING, rotation.rotate(state.get(Properties.HORIZONTAL_FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(Properties.HORIZONTAL_FACING)));
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, RandomGenerator random) {
		if (state.get(Properties.LIT) && state.get(HEIGHT) > 0) {
			double d = (double) pos.getX() + 0.5;
			double e = (double) pos.getY() + PARTICLE_HEIGHT[state.get(HEIGHT)];
			double f = (double) pos.getZ() + 0.5;
			world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
			world.addParticle(ParticleTypes.FLAME, d, e, f, 0.0, 0.0, 0.0);
		}
	}
}
