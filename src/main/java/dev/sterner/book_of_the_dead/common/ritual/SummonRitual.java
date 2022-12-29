package dev.sterner.book_of_the_dead.common.ritual;

import dev.sterner.book_of_the_dead.api.NecrotableRitual;
import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import dev.sterner.book_of_the_dead.common.block.entity.RitualBlockEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;

public class SummonRitual extends NecrotableRitual {

	public SummonRitual(Identifier id, Identifier largeCircleSprite, Identifier smallCircleSprite) {
		super(id, largeCircleSprite, smallCircleSprite);
	}

	@Override
	public void tick(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		super.tick(world, blockPos, blockEntity);
		for(Entity entity : summons){
			entity.teleport(entity.getX(), entity.getY()+ 0.01, entity.getZ());
			/*
			int index = summons.indexOf(entity);
			if(blockEntity.posList != null && blockEntity.timer < 20 * 2){
				Vec3d vec3d = blockEntity.posList.get(index);
				Direction direction = world.getBlockState(blockPos).get(HorizontalFacingBlock.FACING);
				double xOffset;
				double zOffset;
				if(direction == Direction.NORTH){
					xOffset = 1; zOffset = -1;
				}else if(direction == Direction.SOUTH){
					xOffset = 1; zOffset = 2.5;
				}else if(direction == Direction.EAST){
					xOffset = 1.5; zOffset = 0;
				}else{
					xOffset = -0.5; zOffset = 1;
				}
				for (int i = 0; i < 12; i++) {
					world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.GRASS_BLOCK.getDefaultState()),
							ritualCenter.getX() + vec3d.getX() + xOffset + MathHelper.nextDouble(world.getRandom(), -1, 1),
							blockEntity.getPos().getY(),
							ritualCenter.getZ() + vec3d.getZ() + zOffset + MathHelper.nextDouble(world.getRandom(), -1, 1),
							0, 0, 0);
				}
			}

			 */
		}
	}

	@Override
	public void onStart(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		super.onStart(world, blockPos, blockEntity);
		if(world instanceof ServerWorld serverWorld) {

			if (summons.isEmpty()) {
				this.summons.add(0, EntityType.ZOMBIE.create(serverWorld));
				this.summons.add(1, EntityType.ZOMBIE.create(serverWorld));
				this.summons.add(2, EntityType.SKELETON.create(serverWorld));
			} else {
				for (Entity entity : this.summons) {
					int index = this.summons.indexOf(entity);
					System.out.println(index);
					Vec3d worldPos = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()).add(2, 2, 2);
					entity.setInvulnerable(true);
					entity.refreshPositionAndAngles(worldPos.x, worldPos.y, worldPos.z, 0, 0);
					System.out.println(entity.getBlockPos());
					serverWorld.spawnEntity(entity);
				}
			}
		}

	}

	@Override
	public void onStopped(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		super.onStopped(world, blockPos, blockEntity);
		for(Entity entity : this.summons){
			entity.setInvulnerable(false);
		}
		/*
		ArrayList<Vec3d> posList = blockEntity.posList;
		ArrayList<Float> yawList = blockEntity.yawList;
		for(Entity entity : this.summons){
			int index = this.summons.indexOf(entity);
			entity.setBodyYaw(yawList.get(index));
			Vec3d worldPos = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()).add(posList.get(index));
			entity.refreshPositionAfterTeleport(worldPos);
			world.spawnEntity(entity);
		}

		 */

	}
}
