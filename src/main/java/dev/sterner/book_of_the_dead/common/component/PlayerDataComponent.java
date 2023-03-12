package dev.sterner.book_of_the_dead.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class PlayerDataComponent implements AutoSyncedComponent {
	private final PlayerEntity player;
	private int butcheringLevel = 0;
	private final int MAX_BUTCHERING_LEVEL = 100;

	public PlayerDataComponent(PlayerEntity player) {
		this.player = player;
	}

	public double getButcheringModifier(){
		return 1.0D + butcheringLevel > 0 ? (double)(butcheringLevel / MAX_BUTCHERING_LEVEL) : 0;
	}

	public int getButcheringLevel(){
		return butcheringLevel;
	}

	public void setButcheringLevel(int butcheringLevel){
		this.butcheringLevel = butcheringLevel;
		BotDComponents.CORPSE_COMPONENT.sync(this.player);
	}

	public void increaseButcheringLevel(int amount){
		if(getButcheringLevel() + amount <= MAX_BUTCHERING_LEVEL){
			setButcheringLevel(getButcheringLevel() + amount);
		}
	}

	public void decreaseButcheringLevel(int amount){
		if(getButcheringLevel() - amount >= 0){
			setButcheringLevel(getButcheringLevel() - amount);
		}
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putInt(Constants.Nbt.BUTCHERING_LEVEL, butcheringLevel);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		this.butcheringLevel = nbt.getInt(Constants.Nbt.BUTCHERING_LEVEL);
	}
}
