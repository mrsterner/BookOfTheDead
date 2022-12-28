package dev.sterner.book_of_the_dead.common.ritual;

import dev.sterner.book_of_the_dead.api.NecrotableRitual;
import dev.sterner.book_of_the_dead.common.block.entity.RitualBlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FunnyRitual extends NecrotableRitual {
	public FunnyRitual(Identifier id, Identifier largeCircleSprite, Identifier smallCircleSprite, int duration) {
		super(id, largeCircleSprite, smallCircleSprite, duration);
	}

	@Override
	public void tick(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		super.tick(world, blockPos, blockEntity);
	}
}
