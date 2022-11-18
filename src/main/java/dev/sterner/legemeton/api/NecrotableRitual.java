package dev.sterner.legemeton.api;

import dev.sterner.legemeton.common.block.entity.NecroTableBlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class NecrotableRitual {
	public final Identifier smallCircleSprite;
	public final Identifier largeCircleSprite;
	public final int duration;

	public NecrotableRitual(Identifier largeCircleSprite, Identifier smallCircleSprite, int duration) {
		this.smallCircleSprite = smallCircleSprite;
		this.largeCircleSprite = largeCircleSprite;
		this.duration = duration;
	}

	public void tick(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
	}

	public void onStopped(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity){

	}
}
