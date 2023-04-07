package dev.sterner.book_of_the_dead.common.block;

import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class CandleBlock extends Block {
	public static final IntProperty HEIGHT = IntProperty.of("height", 0, 4);
	public static final float[] PARTICLE_HEIGHT = { 0, 7 / 16f, 10 / 16f, 12 / 16f, 14 / 16f };
	public static final float[] VOXEL_HEIGHT = { 2, 5, 8, 10, 12 };

	public CandleBlock(Settings settings) {
		super(settings
			.luminance(state -> state.get(Properties.LIT) ? 12 : 0).nonOpaque()
			.strength(0.1F)
			.sounds(BlockSoundGroup.CANDLE));

		this.setDefaultState(
			this.stateManager
				.getDefaultState()
				.with(Properties.LIT,false)
				.with(HEIGHT, 4)
		);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack handStack = player.getMainHandStack();
		if(hand == Hand.MAIN_HAND){
			if(handStack.isOf(BotDObjects.FAT) || handStack.isOf(Items.HONEYCOMB)){
				if(!state.get(Properties.LIT) && state.get(HEIGHT) < 4){
					world.setBlockState(pos, state.with(HEIGHT, state.get(HEIGHT) + 1));
					if(!player.isCreative()){
						handStack.decrement(1);
					}
					return ActionResult.CONSUME;
				}
			}else if(handStack.isEmpty() && state.get(Properties.LIT)){
				world.setBlockState(pos, state.with(Properties.LIT, false));
				world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1.0F, 1.0F);

			} else if(handStack.isOf(Items.FLINT_AND_STEEL) || handStack.isOf(Items.FIRE_CHARGE)){
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
		if (state.get(HEIGHT) > 0 && state.get(Properties.LIT) && random.nextFloat() < 0.001F) {
			BlockState newState = state.with(HEIGHT, state.get(HEIGHT) - 1).with(Properties.LIT, state.get(HEIGHT) - 1 != 0);
			world.setBlockState(pos, newState);
		}
		super.randomTick(state, world, pos, random);
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, RandomGenerator random) {
		if(state.get(Properties.LIT) && state.get(HEIGHT) > 0){
			double d = (double)pos.getX() + 0.5;
			double e = (double)pos.getY() + PARTICLE_HEIGHT[state.get(HEIGHT)];
			double f = (double)pos.getZ() + 0.5;
			world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
			world.addParticle(ParticleTypes.FLAME, d, e, f, 0.0, 0.0, 0.0);
		}
	}
}
