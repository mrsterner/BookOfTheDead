package dev.sterner.book_of_the_dead.api;

import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class NecrotableRitual {
	private final Identifier id;
	public final Identifier smallCircleSprite;
	public final Identifier largeCircleSprite;
	public final int duration;

	public NecrotableRitual(Identifier id, Identifier largeCircleSprite, Identifier smallCircleSprite, int duration) {
		this.id = id;
		this.smallCircleSprite = smallCircleSprite;
		this.largeCircleSprite = largeCircleSprite;
		this.duration = duration;
	}

	public void tick(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
	}

	public void onStopped(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity){

	}

	public void onStart(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity){

	}

	public Identifier getId() {
		return id;
	}
}
