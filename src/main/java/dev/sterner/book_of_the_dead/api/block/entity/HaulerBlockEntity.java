package dev.sterner.book_of_the_dead.api.block.entity;

import dev.sterner.book_of_the_dead.api.interfaces.IHauler;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public abstract class HaulerBlockEntity extends BaseBlockEntity implements IHauler {
	public boolean headVisible = true;
	public boolean rArmVisible = true;
	public boolean lArmVisible = true;
	public boolean rLegVisible = true;
	public boolean lLegVisible = true;

	public HaulerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public boolean getHeadVisible() {
		return headVisible;
	}

	@Override
	public boolean getRArmVisible() {
		return rArmVisible;
	}

	@Override
	public boolean getLArmVisible() {
		return lArmVisible;
	}

	@Override
	public boolean getRLegVisible() {
		return rLegVisible;
	}

	@Override
	public boolean getLLegVisible() {
		return lLegVisible;
	}

	@Override
	public void setHeadVisible(boolean visible) {
		this.headVisible = visible;
	}

	@Override
	public void setRArmVisible(boolean visible) {
		this.rArmVisible = visible;
	}

	@Override
	public void setLArmVisible(boolean visible) {
		this.lArmVisible = visible;
	}

	@Override
	public void setRLegVisible(boolean visible) {
		this.rLegVisible = visible;
	}

	@Override
	public void setLLegVisible(boolean visible) {
		this.lLegVisible = visible;
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putBoolean(Constants.Nbt.HEAD_VISIBLE, getHeadVisible());
		nbt.putBoolean(Constants.Nbt.RIGHT_ARM_VISIBLE, getRLegVisible());
		nbt.putBoolean(Constants.Nbt.LEFT_ARM_VISIBLE, getLArmVisible());
		nbt.putBoolean(Constants.Nbt.RIGHT_LEG_VISIBLE, getRLegVisible());
		nbt.putBoolean(Constants.Nbt.LEFT_LEG_VISIBLE, getLLegVisible());
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		setHeadVisible(nbt.getBoolean(Constants.Nbt.HEAD_VISIBLE));
		setRArmVisible(nbt.getBoolean(Constants.Nbt.RIGHT_ARM_VISIBLE));
		setLArmVisible(nbt.getBoolean(Constants.Nbt.LEFT_ARM_VISIBLE));
		setRLegVisible(nbt.getBoolean(Constants.Nbt.RIGHT_LEG_VISIBLE));
		setLLegVisible(nbt.getBoolean(Constants.Nbt.LEFT_LEG_VISIBLE));
	}
}
