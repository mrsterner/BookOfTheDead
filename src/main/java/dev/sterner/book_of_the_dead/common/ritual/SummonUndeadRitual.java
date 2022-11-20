package dev.sterner.book_of_the_dead.common.ritual;

import dev.sterner.book_of_the_dead.api.NecrotableRitual;
import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class SummonUndeadRitual extends NecrotableRitual {
	public World world = null;
	public SummonUndeadRitual(Identifier id, Identifier largeCircleSprite, Identifier smallCircleSprite, int duration) {
		super(id, largeCircleSprite, smallCircleSprite, duration);


	}

	@Override
	public void tick(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
		this.world = world;
		if(summons.isEmpty()){
			this.summons.add(0, EntityType.ZOMBIE.create(world));
		}
	}

	@Override
	public void onStart(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity) {

		super.onStart(world, blockPos, blockEntity);
	}
}
