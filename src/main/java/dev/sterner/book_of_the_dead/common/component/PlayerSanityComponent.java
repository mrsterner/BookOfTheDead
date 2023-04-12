package dev.sterner.book_of_the_dead.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import dev.sterner.book_of_the_dead.BotD;
import dev.sterner.book_of_the_dead.client.EyeManager;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public class PlayerSanityComponent implements AutoSyncedComponent, ClientTickingComponent {

	public PlayerEntity player;
	public EyeManager manager;
	public int MAX_SANITY = 100;
	private int sanity = MAX_SANITY;

	public PlayerSanityComponent(PlayerEntity player) {
		this.player = player;
		this.manager = new EyeManager(player.getRandom());
	}

	@Override
	public void clientTick() {
		if(this.manager == null){
			return;
		}
		this.manager.tick();
	}

	public void updateSanity() {
		if(this.manager == null || player == null){
			return;
		}
		double percent = sanity / 100d;
		this.manager.blinkDelay = (int)(10 + 20 * 8 * percent);
		this.manager.blinkTopCoord = sanity >= 80 ? 4 : sanity >= 60 ? 3 : sanity >= 40 ? 2 : sanity >= 20 ? 1 : 0;
		this.manager.moveDelayHorizontal =(int)( 10 + 20 * 3 * percent);
		this.manager.moveDelayVertical =(int)( 10 + 20 * 3 * percent);
		this.manager.chanceToLook = sanity < 10 ? 0.9f : sanity < 20 ? 0.5f : 0;

		if(BotD.isDebugMode()){
			System.out.println("Delay: " +this.manager.blinkDelay + " : Coord: " + this.manager.blinkTopCoord + " : ");
		}
	}

	@Override
	public void readFromNbt(@NotNull NbtCompound nbt) {
		setSanity(nbt.getInt(Constants.Nbt.SANITY));
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound nbt) {
		nbt.putInt(Constants.Nbt.SANITY, getSanity());
	}

	public void increaseSanity(int amount) {
		if (getSanity() + amount <= MAX_SANITY) {
			setSanity(getSanity() + amount);
		}
	}

	public void decreaseSanity(int amount) {
		if (getSanity() - amount >= 0) {
			setSanity(getSanity() - amount);
		}
	}

	public int getSanity() {
		return sanity;
	}

	public void setSanity(int sanity) {
		this.sanity = sanity;
		BotDComponents.SANITY_COMPONENT.sync(this.player);
		updateSanity();
	}
}
