package dev.sterner.book_of_the_dead.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
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

		SanityClientData data = SanityClientData.getDataFromSanity(getSanity());
		System.out.println(getSanity() + "blinkDelay: " + data.blinkDelay + " : " + data.eyeLevel);
		this.manager.blinkDelay = data.blinkDelay;
		this.manager.blinkTopCoord = data.eyeLevel;
		this.manager.moveDelayVertical = data.moveDelayVertical;
		this.manager.moveDelayHorizontal = data.moveDelayHorizontal;
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
		BotDComponents.EYE_COMPONENT.sync(this.player);
	}



	public record SanityClientData(int blinkDelay, int eyeLevel, int moveDelayHorizontal, int moveDelayVertical) {

		public static SanityClientData of(int blinkDelay, int eyeLevel, int moveDelayHorizontal, int moveDelayVertical) {
			return new SanityClientData(blinkDelay, eyeLevel, moveDelayHorizontal, moveDelayVertical);
		}

		public static SanityClientData getDataFromSanity(int sanity) {
			int percent = sanity / 100;

			int blinkDelay = 10 + 20 * 8 * ( percent);
			int eyeLevel = sanity >= 80 ? 4 : sanity >= 60 ? 3 : sanity >= 40 ? 2 : sanity >= 20 ? 1 : 0;
			int moveDelayHorizontal = 10 + 20 * 3 * percent;
			int moveDelayVertical = 10 + 20 * 3 * percent;

			return of(blinkDelay, eyeLevel, moveDelayHorizontal, moveDelayVertical);
		}
	}
}
