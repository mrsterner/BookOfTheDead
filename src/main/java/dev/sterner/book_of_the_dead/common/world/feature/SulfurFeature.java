package dev.sterner.book_of_the_dead.common.world.feature;

import dev.sterner.book_of_the_dead.common.block.SulfurLayerBlock;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SulfurFeature extends Feature<DefaultFeatureConfig> {
	public SulfurFeature() {
		super(DefaultFeatureConfig.CODEC);
	}

	@Override
	public boolean place(FeatureContext<DefaultFeatureConfig> context) {
		StructureWorldAccess world = context.getWorld();
		BlockPos blockPos = context.getOrigin();
		RandomGenerator random = context.getRandom();

		int radius = 4;

		// Find a lava block within the chunk
		BlockPos lavaBlockPos = blockPos.add(random.nextInt(16), 0, random.nextInt(16));
		while (lavaBlockPos.getY() > 0 && !world.getBlockState(lavaBlockPos).isOf(Blocks.LAVA)) {
			lavaBlockPos = lavaBlockPos.down();
		}

		if (!world.getBlockState(lavaBlockPos).isOf(Blocks.LAVA)) {
			return false;
		}
		double skipChance = 0.05;
		int radiusSquared = radius * radius; // squared radius for faster distance calculations

		// Replace solid blocks around the lava block with sulfur blocks
		int minY = lavaBlockPos.getY() - 2;
		for (int x = lavaBlockPos.getX() - radius; x <= lavaBlockPos.getX() + radius; x++) {
			for (int y = minY; y <= lavaBlockPos.getY() + 2; y++) {
				for (int z = lavaBlockPos.getZ() - radius; z <= lavaBlockPos.getZ() + radius; z++) {
					BlockPos placedPos = new BlockPos(x, y, z);

					// Calculate the distance between the current block and the lava block
					double dx = placedPos.getX() - lavaBlockPos.getX();
					double dy = placedPos.getY() - lavaBlockPos.getY();
					double dz = placedPos.getZ() - lavaBlockPos.getZ();
					double distanceSquared = dx * dx + dy * dy + dz * dz;

					// Only place sulfur blocks within the circular area
					if (distanceSquared <= radiusSquared && random.nextDouble() > skipChance) {
						BlockState state = world.getBlockState(placedPos);
						BlockState belowState = world.getBlockState(placedPos.down());

						if (state.isAir() && belowState.isSolidBlock(world, placedPos.down())) {
							if (random.nextBoolean()) {
								int i = random.nextInt(2);
								world.setBlockState(placedPos, BotDObjects.SULFUR.getDefaultState().with(SulfurLayerBlock.LAYERS, 1 + i), Block.NOTIFY_ALL);
							}
							world.setBlockState(placedPos.down(), BotDObjects.SULFUR.getDefaultState().with(SulfurLayerBlock.LAYERS, 8), Block.NOTIFY_ALL);
							if(random.nextBoolean()){
								world.setBlockState(placedPos.down().down(), BotDObjects.SULFUR.getDefaultState().with(SulfurLayerBlock.LAYERS, 8), Block.NOTIFY_ALL);
							}

						}
					}
				}
			}
		}

		return true;
	}
}
