package dev.sterner.book_of_the_dead.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import dev.sterner.book_of_the_dead.client.EyeManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public class PlayerEyeComponent implements AutoSyncedComponent, ClientTickingComponent {
	public PlayerEntity player;
	public EyeManager manager = null;

	public PlayerEyeComponent(PlayerEntity player){
		this.player = player;

	}

	@Override
	public void clientTick() {
		if(manager == null){
			this.manager = new EyeManager(player.getRandom());
		}
		this.manager.tick();
	}



	@Override
	public void readFromNbt(@NotNull NbtCompound nbt) {

	}

	@Override
	public void writeToNbt(@NotNull NbtCompound nbt) {

	}
}
