package dev.sterner.book_of_the_dead.common.block;

import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import dev.sterner.book_of_the_dead.common.block.entity.PedestalBlockEntity;
import dev.sterner.book_of_the_dead.common.block.entity.RitualBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RitualBlock extends BlockWithEntity {
	protected static final VoxelShape OUTLINE_SHAPE = VoxelShapes.union(createCuboidShape(0D, 0D, 0D, 16D, 2D, 16D));

	public RitualBlock(Settings settings) {
		super(settings.nonOpaque());
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return OUTLINE_SHAPE;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient()) {
			if(world.getBlockEntity(pos) instanceof RitualBlockEntity ritualBlockEntity){
				return ritualBlockEntity.onUse(world, state, pos, player, hand);
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new RitualBlockEntity(pos, state);
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, net.minecraft.util.random.RandomGenerator random) {
		if(world.getBlockEntity(pos) instanceof RitualBlockEntity ritualBlockEntity) {
			List<Pair<ItemStack, BlockPos>> pedestalInfo = ritualBlockEntity.getPedestalInfo(world);
			List<BlockPos> blockPosList = pedestalInfo.stream().map(Pair::getRight).toList();

			for(BlockPos pedestalPos : RitualBlockEntity.PEDESTAL_POS_LIST){
				if(!blockPosList.contains(pedestalPos)){
					BlockPos specificPos = pedestalPos.add(pos);
					for(int i = 0; i  < 4; i++) {
						double x = (double)specificPos.getX() + 0.5D;
						double y = (double)specificPos.getY() + 0.0625D;
						double z = (double)specificPos.getZ() + 0.5D;
						world.addParticle(ParticleTypes.WITCH, x, y, z, 0,0.1,0);
					}
				}
			}
		}
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return (tickerWorld, pos, tickerState, blockEntity) -> RitualBlockEntity.tick(tickerWorld, pos, tickerState, (RitualBlockEntity) blockEntity);
	}
}
