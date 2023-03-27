package dev.sterner.book_of_the_dead.common.block;

import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import dev.sterner.book_of_the_dead.common.block.entity.PedestalBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


public class PedestalBlock extends Block implements BlockEntityProvider {
	protected static final VoxelShape OUTLINE_SHAPE = VoxelShapes.union(
			createCuboidShape(2D, 0D, 2D, 14D, 3D, 14D),
			createCuboidShape(4D, 3D, 4D, 12D, 11D, 12D),
			createCuboidShape(3D, 11D, 3D, 13D, 13D, 13D)
	);

	public PedestalBlock(Settings settings) {
		super(settings.nonOpaque());
	}

	@Override
	public BlockRenderType getRenderType(BlockState blockState) {
		return BlockRenderType.MODEL;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new PedestalBlockEntity(pos, state);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack playerStack = player.getStackInHand(hand);
		BlockEntity entityCheck = world.getBlockEntity(pos);
		if (entityCheck instanceof PedestalBlockEntity entity) {
			if (!entity.isCrafting()) {
				if (!playerStack.isEmpty()) {
					ItemStack entityStack = entity.getStack();
					if (!entityStack.isItemEqualIgnoreDamage(playerStack)) {
						if (!world.isClient) {
							ItemStack newStack = playerStack.copy();
							newStack.setCount(1);
							entity.setStack(newStack);
							playerStack.decrement(1);
							if (!entityStack.isEmpty()) {
								if (playerStack.isEmpty()) {
									player.setStackInHand(hand, entityStack);
								} else if (!player.getInventory().insertStack(entityStack)) {
									player.dropItem(entityStack, false, true);
								}
							}
						}
						return ActionResult.success(world.isClient);
					}
					return ActionResult.FAIL;
				} else {
					if (!entity.getStack().isEmpty()) {
						if (!world.isClient) {
							if (!player.getInventory().insertStack(entity.getStack().copy())) {
								player.dropItem(entity.getStack().copy(), false, true);
							}
							entity.setStack(ItemStack.EMPTY);
						}
						return ActionResult.success(world.isClient);
					}
					return ActionResult.FAIL;
				}
			}
		}
		return ActionResult.FAIL;
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, net.minecraft.util.random.RandomGenerator random) {

	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (world.getBlockEntity(pos) instanceof PedestalBlockEntity be) {
			ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), be.getStack());
		}
		super.onBreak(world, pos, state, player);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return (tickerWorld, pos, tickerState, blockEntity) -> {
			if(blockEntity instanceof PedestalBlockEntity be){
				be.tick(world, pos, state);
			}
		};
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return OUTLINE_SHAPE;
	}
}
