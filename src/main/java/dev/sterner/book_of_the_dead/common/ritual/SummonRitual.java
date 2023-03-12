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
	public void onStopped(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		super.onStopped(world, blockPos, blockEntity);

		for(Entity entity : this.summons){
			Vec3d worldPos = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
			entity.refreshPositionAndAngles(new BlockPos(worldPos), world.random.nextFloat(), world.random.nextFloat());
			world.spawnEntity(entity);
		}
	}
}
