package dev.sterner.book_of_the_dead.common.world;

import com.mojang.authlib.GameProfile;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.PlayerSkullBlock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.PlayerManager;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BotDWorldState extends PersistentState {
	public final List<GameProfile> gameProfileList = new ArrayList<>();

	public static BotDWorldState readNbt(NbtCompound nbt) {
		BotDWorldState universalWorldState = new BotDWorldState();
		NbtList nbtList = nbt.getList("GameProfileList", NbtType.COMPOUND);
		for (int i = 0; i < nbtList.size(); i++) {
			GameProfile gameProfile = new GameProfile(nbtList.getCompound(i).getUuid("Uuid"), nbtList.getCompound(i).getString("Name"));
			universalWorldState.gameProfileList.add(gameProfile);
		}

		return universalWorldState;
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		NbtList nbtList = new NbtList();
		for (GameProfile gameProfile : this.gameProfileList) {
			NbtCompound nbtCompound = new NbtCompound();
			nbtCompound.putUuid("Uuid", gameProfile.getId());
			nbtCompound.putString("Name", gameProfile.getName());
			nbtList.add(nbtCompound);
		}
		nbt.put("GameProfileList", nbtList);

		return nbt;
	}

	@SuppressWarnings("ConstantConditions")
	public static BotDWorldState get(World world) {
		return world.getServer().getOverworld().getPersistentStateManager().getOrCreate(BotDWorldState::readNbt, BotDWorldState::new, Constants.MOD_ID);
	}
}
