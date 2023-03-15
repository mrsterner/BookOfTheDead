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
	private boolean isBloody = false;
	private boolean isFilthy = false;

	public ButcherTableBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.BUTCHER, pos, state);
	}

	public ActionResult onUse(World world, BlockState state, BlockPos pos, PlayerEntity player, Hand hand, boolean isNeighbour) {
		return onUse(world, state, pos, player, hand, 0.5d, 0d, isNeighbour);
	}

	public boolean isBloody() {
		return isBloody;
	}

	public void setBloody(boolean bloody) {
		isBloody = bloody;
	}

	public boolean isFilthy() {
		return isFilthy;
	}

	public void setFilthy(boolean filthy) {
		isFilthy = filthy;
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		nbt.putBoolean(Constants.Nbt.BLOODY, isBloody());
		nbt.putBoolean(Constants.Nbt.FILTHY, isFilthy());
		super.writeNbt(nbt);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		setBloody(nbt.getBoolean(Constants.Nbt.BLOODY));
		setFilthy(nbt.getBoolean(Constants.Nbt.FILTHY));
		super.readNbt(nbt);
	}

	public double filthyfy(@NotNull World world) {
		RandomGenerator randomGenerator = world.getRandom();
		if(randomGenerator.nextDouble() > 0.75d){
			if(isFilthy()){
				return 0.25d;
			}
			if(isBloody()){
				this.setFilthy(true);
				this.setBloody(false);
				return 0.25d;
			}
			this.setBloody(true);
			return 0.75d;
		}
		return 1;
	}
}
