package dev.sterner.book_of_the_dead.common.block;

import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class SulfurLayerBlock extends SnowBlock {
	public SulfurLayerBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		if(state.isOf(this) && state.get(LAYERS) > 1){
			world.setBlockState(pos, state.with(LAYERS, state.get(LAYERS) - 1), Block.NOTIFY_ALL);
		}
		super.onBroken(world, pos, state);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos.down());
		return Block.isFaceFullSquare(blockState.getCollisionShape(world, pos.down()), Direction.UP) || blockState.isOf(this) && blockState.get(LAYERS) == 8;
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return true;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random) {
		boolean sulf = world.getBlockState(pos.up()).isOf(BotDObjects.SULFUR);
		if(world.getBlockState(pos.up()).isAir() || sulf){
			boolean bl = false;
			for(Direction direction : Properties.HORIZONTAL_FACING.getValues()){
				if(world.getBlockState(pos.offset(direction)).isOf(Blocks.LAVA)){
					bl = true;
					break;
				}
			}
			if(bl){
				int layer = sulf ? world.getBlockState(pos.up()).get(LAYERS) : 0;
				layer = MathHelper.clamp(layer, 0, 7);
				world.setBlockState(pos.up(), BotDObjects.SULFUR.getDefaultState().with(LAYERS, 1 + layer));
			}
		}
	}
}
