package dev.sterner.book_of_the_dead.common.ritual;

import dev.sterner.book_of_the_dead.api.NecrotableRitual;
import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SummonRitual extends NecrotableRitual {

	public SummonRitual(Identifier id, Identifier largeCircleSprite, Identifier smallCircleSprite, int duration) {
		super(id, largeCircleSprite, smallCircleSprite, duration);
	}

	@Override
	public void tick(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
		super.tick(world, blockPos, blockEntity);
		for(Entity entity : summons){


			int index = summons.indexOf(entity);
			if(blockEntity.posList != null && blockEntity.timer < 20 * 2){
				Vec3d vec3d = blockEntity.posList.get(index);
				Direction direction = world.getBlockState(blockPos).get(HorizontalFacingBlock.FACING);
				double xOffset = 0;
				double zOffset = 0;
				if(direction == Direction.NORTH){
					xOffset = 1;
					zOffset = -1;
				}else if(direction == Direction.SOUTH){
					xOffset = 1;
					zOffset = 2.5;
				}else if(direction == Direction.EAST){
					xOffset = 1.5;
					zOffset = 0;
				}else{
					xOffset = -0.5;
					zOffset = 1;
				}

				for (int i = 0; i < 12; i++) {
					world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.GRASS_BLOCK.getDefaultState()),
							ritualCenter.getX() + vec3d.getX() + xOffset + MathHelper.nextDouble(world.getRandom(), -1, 1),
							blockEntity.getPos().getY(),
							ritualCenter.getZ() + vec3d.getZ() + zOffset + MathHelper.nextDouble(world.getRandom(), -1, 1),
							0, 0, 0);
				}
			}
		}
	}

	@Override
	public void onStopped(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity) {

		super.onStopped(world, blockPos, blockEntity);
	}
}
