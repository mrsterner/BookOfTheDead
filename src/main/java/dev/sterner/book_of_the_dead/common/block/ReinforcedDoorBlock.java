package dev.sterner.book_of_the_dead.common.block;

import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class ReinforcedDoorBlock extends DoorBlock {
	public static final BooleanProperty LOCKED =  BooleanProperty.of("locked");
	public ReinforcedDoorBlock(Settings settings) {
		super(settings.strength(-1.0F, 3600000.0F), SoundEvents.BLOCK_WOODEN_DOOR_CLOSE, SoundEvents.BLOCK_WOODEN_DOOR_OPEN);
		this.stateManager.getDefaultState().with(LOCKED, true);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if(player.getMainHandStack().isOf(BotDObjects.CELLAR_KEY)){
			world.setBlockState(pos, state.cycle(LOCKED));
			world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 1,1);
			if(!player.isCreative()){
				player.getMainHandStack().decrement(1);
			}
			return ActionResult.CONSUME;
		}else if (state.get(LOCKED)) {
			player.sendMessage(Text.translatable("info.book_of_the_dead.door_locked"), true);
			return ActionResult.PASS;
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		DoubleBlockHalf doubleBlockHalf = state.get(HALF);
		if (direction.getAxis() != Direction.Axis.Y || doubleBlockHalf == DoubleBlockHalf.LOWER != (direction == Direction.UP)) {
			return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
		} else {
			return neighborState.isOf(this) && neighborState.get(HALF) != doubleBlockHalf
					? state.with(FACING, neighborState.get(FACING))
					.with(OPEN, neighborState.get(OPEN))
					.with(HINGE, neighborState.get(HINGE))
					.with(POWERED, neighborState.get(POWERED))
					.with(LOCKED, neighborState.get(LOCKED))
					: Blocks.AIR.getDefaultState();
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(LOCKED);
		super.appendProperties(builder);

	}
}
