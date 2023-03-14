package dev.sterner.book_of_the_dead.common.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PlayerCorpseEntity extends MobEntity {
	private static final TrackedData<NbtCompound> GAME_PROFILE = DataTracker.registerData(PlayerCorpseEntity.class, TrackedDataHandlerRegistry.TAG_COMPOUND);

	public PlayerCorpseEntity(EntityType<? extends MobEntity> entityType, World world) {
		super(entityType, world);
	}

	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(GAME_PROFILE, new NbtCompound());
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		if (getSkinProfile() != null) {
			NbtCompound nbtCompound = new NbtCompound();
			NbtHelper.writeGameProfile(nbtCompound, this.getSkinProfile());
			nbt.put("SkinProfile", nbtCompound);
		}
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (nbt.contains("SkinProfile", NbtElement.COMPOUND_TYPE)) {
			this.setSkinProfile(NbtHelper.toGameProfile(nbt.getCompound("SkinProfile")));
		}
	}

	@Nullable
	public GameProfile getSkinProfile() {
		NbtCompound nbt = this.dataTracker.get(GAME_PROFILE);
		return NbtHelper.toGameProfile(nbt);
	}

	public void setSkinProfile(@Nullable GameProfile profile) {
		NbtCompound nbt = new NbtCompound();
		if(profile != null){
			NbtHelper.writeGameProfile(nbt, profile);
		}
		this.dataTracker.set(GAME_PROFILE, nbt);
	}

	@Override
	protected void initGoals() {

	}
}
