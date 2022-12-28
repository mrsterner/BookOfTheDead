package dev.sterner.book_of_the_dead.common.ritual;

import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class SummonUndeadRitual extends SummonRitual {
	public SummonUndeadRitual(Identifier id, Identifier largeCircleSprite, Identifier smallCircleSprite, int duration) {
		super(id, largeCircleSprite, smallCircleSprite, duration);
	}

}
