package dev.sterner.book_of_the_dead.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CandleBlock extends Block {
	public static final IntProperty HEIGHT = IntProperty.of("height", 0, 4);
	public static final VoxelShape BOUNDING_SHAPE = Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 10.0, 10.0);

	public CandleBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this.stateManager
				.getDefaultState()
				.with(Properties.LIT,true)
				.with(HEIGHT, 4)
		);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return BOUNDING_SHAPE;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.LIT, HEIGHT);
		super.appendProperties(builder);
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return state.get(HEIGHT) > 0 && state.get(Properties.LIT);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random) {
		//TODO make candle consume itself

		super.randomTick(state, world, pos, random);
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, RandomGenerator random) {
		if(state.get(Properties.LIT) && state.get(HEIGHT) > 0){
			double d = (double)pos.getX() + 0.5;
			double e = (double)pos.getY() + 0.7 + state.get(HEIGHT) * 0.25 - 0.5;
			double f = (double)pos.getZ() + 0.5;
			world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
			world.addParticle(ParticleTypes.FLAME, d, e, f, 0.0, 0.0, 0.0);
		}
	}
}
