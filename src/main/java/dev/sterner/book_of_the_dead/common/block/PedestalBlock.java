package dev.sterner.book_of_the_dead.common.block;

import dev.sterner.book_of_the_dead.common.block.entity.PedestalBlockEntity;
import dev.sterner.book_of_the_dead.common.block.entity.RitualBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
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
		if(entityCheck instanceof PedestalBlockEntity entity) {
			if(!entity.isCrafting()) {
				if(!playerStack.isEmpty()) {
					ItemStack entityStack = entity.getStack();
					if(!entityStack.isItemEqual(playerStack)) {
						if(!world.isClient) {
							ItemStack newStack = playerStack.copy();
							newStack.setCount(1);
							entity.setStack(newStack);
							playerStack.decrement(1);
							if(!entityStack.isEmpty()) {
								if(playerStack.isEmpty()) {
									player.setStackInHand(hand, entityStack);
								}
								else if(!player.getInventory().insertStack(entityStack)) {
									player.dropItem(entityStack, false, true);
								}
							}
						}
						return ActionResult.success(world.isClient);
					}
					return ActionResult.FAIL;
				}
				else {
					if(!entity.getStack().isEmpty()) {
						if(!world.isClient) {
							if(!player.getInventory().insertStack(entity.getStack().copy())) {
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
		/*
		double x = (double)pos.getX() + 0.5D;
		double y = (double)pos.getY() + 1.0625D;
		double z = (double)pos.getZ() + 0.5D;
		BlockEntity testEntity = world.getBlockEntity(pos);
		if(testEntity instanceof PedestalBlockEntity entity) {
			if(entity.isCrafting()) {
				for(int i = 0; i  < 4; i++) {
					world.addParticle(ParticleTypes.WITCH, x, y, z, random.nextDouble() - 0.5D, random.nextDouble() - 0.5D, random.nextDouble() - 0.5D);
				}
			}
		}

		 */
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return (tickerWorld, pos, tickerState, blockEntity) -> PedestalBlockEntity.tick(tickerWorld, pos, tickerState, (PedestalBlockEntity) blockEntity);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return OUTLINE_SHAPE;
	}
}
