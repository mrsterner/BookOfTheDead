package dev.sterner.book_of_the_dead.common.block.entity;

import dev.sterner.book_of_the_dead.api.NecrotableRitual;
import dev.sterner.book_of_the_dead.common.registry.BotDBlockEntityTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDRituals;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class RitualBlockEntity extends BlockEntity {
	public NecrotableRitual currentNecrotableRitual = BotDRituals.SUMMON_ZOMBIE;
	private boolean loaded = false;
	public int timer = 0;
	public long age = 0;
	public UUID user = null;

	public boolean test = true;//TODO remove

	public RitualBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.RITUAL, pos, state);
	}

	public void getPedestalStacks(){

	}

	public static void tick(World world, BlockPos pos, BlockState blockState, RitualBlockEntity blockEntity) {
		if (world != null) {
			if (!blockEntity.loaded) {
				blockEntity.markDirty();
				blockEntity.loaded = true;
			}
			blockEntity.age++;
			NecrotableRitual ritual = blockEntity.currentNecrotableRitual;
			if (ritual != null) {
				if(blockEntity.test){
					ritual.onStart(world, pos, blockEntity);
					blockEntity.test = false;
				}
				blockEntity.timer++;
				if (blockEntity.timer >= 0) {
					ritual.tick(world, pos, blockEntity);
				}
				if(blockEntity.timer >= blockEntity.currentNecrotableRitual.duration){
					blockEntity.currentNecrotableRitual.onStopped(world, pos, blockEntity);
					blockEntity.currentNecrotableRitual = null;
					blockEntity.timer = 0;
					blockEntity.markDirty();
				}
			}
		}
	}
}
