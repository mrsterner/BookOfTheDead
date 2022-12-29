package dev.sterner.book_of_the_dead.api;

import dev.sterner.book_of_the_dead.common.block.RitualBlock;
import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import dev.sterner.book_of_the_dead.common.block.entity.RitualBlockEntity;
import dev.sterner.book_of_the_dead.common.recipe.RitualRecipe;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
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
	public BlockPos ritualCenter = null;
	public RitualRecipe recipe;
	public List<Entity> summons = new ArrayList<>();
	public World world = null;
	public int ticker = 0;

	public NecrotableRitual(Identifier id, Identifier largeCircleSprite, Identifier smallCircleSprite) {
		this.id = id;
		this.smallCircleSprite = smallCircleSprite;
		this.largeCircleSprite = largeCircleSprite;
	}

	public void tick(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		ticker++;
		BlockPos blockPos1 = blockPos.add(0.5,0.5,0.5);
		ritualCenter = blockPos1;
		world.addParticle(ParticleTypes.SMALL_FLAME, blockPos1.getX(), blockPos1.getY(), blockPos1.getZ(), 0,0,0);
	}

	public void onStopped(World world, BlockPos blockPos, RitualBlockEntity blockEntity){
		ticker = 0;

	}

	public void onStart(World world, BlockPos blockPos, RitualBlockEntity blockEntity){
		if(world.getBlockState(blockPos).isOf(BotDObjects.NECRO_TABLE)){
			if(this.world == null){
				this.world = world;
			}
			ritualCenter = blockPos.add(0.5,0.5,0.5);
		}
	}

	public Identifier getId() {
		return id;
	}
}
