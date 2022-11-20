package dev.sterner.book_of_the_dead.api;

import dev.sterner.book_of_the_dead.common.block.NecroTableBlock;
import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;


public class NecrotableRitual {
	private final Identifier id;
	public final Identifier smallCircleSprite;
	public final Identifier largeCircleSprite;
	public final int duration;
	public BlockPos ritualCenter = null;
	public List<Entity> summons = new ArrayList<>();
	public World world = null;

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
		if(world.getBlockState(blockPos).isOf(BotDObjects.NECRO_TABLE)){
			if(this.world == null){
				this.world = world;
			}
			Direction direction = world.getBlockState(blockPos).get(HorizontalFacingBlock.FACING);
			ritualCenter = blockPos.offset(direction, 2).add(0.5,0.5,0.5);
		}
	}

	public Identifier getId() {
		return id;
	}
}
