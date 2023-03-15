package dev.sterner.book_of_the_dead.common.block.entity;

import dev.sterner.book_of_the_dead.api.block.entity.BaseButcherBlockEntity;
import dev.sterner.book_of_the_dead.common.registry.*;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;


public class ButcherTableBlockEntity extends BaseButcherBlockEntity {
	private static final int MAX_FILTH = 5;
	private int filthLevel = 0;
	public static final int MAX_CLEANING_PROGRESS = 20 * 5;
	private int cleaningProgress = 0;
	public int latter = 0;

	public ButcherTableBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.BUTCHER, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState state, ButcherTableBlockEntity blockEntity) {
		if(blockEntity.latter > 0){
			blockEntity.progressCleaning();
			if(world.getTime() % 20 == 0){
				blockEntity.latter--;
				blockEntity.markDirty();
			}
		}
	}

	public ActionResult onUse(World world, BlockState state, BlockPos pos, PlayerEntity player, Hand hand, boolean isNeighbour) {
		return onUse(world, state, pos, player, hand, 0.5d, 0d, isNeighbour);
	}

	public int getFilthLevel() {
		return filthLevel;
	}

	public void setFilthLevel(int filthLevel) {
		this.filthLevel = filthLevel;
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		nbt.putInt(Constants.Nbt.FILTHY, getFilthLevel());
		nbt.putInt(Constants.Nbt.LATTER, latter);
		nbt.putInt(Constants.Nbt.CLEANING, getCleaningProgress());
		super.writeNbt(nbt);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		setFilthLevel(nbt.getInt(Constants.Nbt.FILTHY));
		latter = nbt.getInt(Constants.Nbt.LATTER);
		setCleaningProgress(nbt.getInt(Constants.Nbt.CLEANING));
		super.readNbt(nbt);
	}

	@Override
	public void makeFilth(@NotNull World world) {
		RandomGenerator randomGenerator = world.getRandom();
		if(randomGenerator.nextDouble() > 0.75d){
			if(getFilthLevel() < MAX_FILTH){
				setFilthLevel(getFilthLevel() + 1);
			}
		}
	}

	public int getCleaningProgress() {
		return cleaningProgress;
	}

	public void setCleaningProgress(int cleaningProgress) {
		this.cleaningProgress = cleaningProgress;
	}

	public void progressCleaning(){
		if(getFilthLevel() > 0){
			if(getCleaningProgress() < MAX_CLEANING_PROGRESS){
				setCleaningProgress(getCleaningProgress() + 1);
			}
			if(getCleaningProgress() >= MAX_CLEANING_PROGRESS){
				setFilthLevel(getFilthLevel() - 1);
				setCleaningProgress(0);
			}
			this.markDirty();
		}
	}
}
