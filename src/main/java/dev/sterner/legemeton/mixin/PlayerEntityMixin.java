package dev.sterner.legemeton.mixin;

import dev.sterner.legemeton.api.interfaces.Hauler;
import dev.sterner.legemeton.common.util.Constants;
import dev.sterner.legemeton.common.util.LegemetonUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements Hauler {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "initDataTracker()V", at = @At("TAIL"))
	private void legemeton$initDataTracker(CallbackInfo info) {
		dataTracker.startTracking(Constants.DataTrackers.PLAYER_CORPSE_ENTITY, new NbtCompound());
		dataTracker.startTracking(Constants.DataTrackers.PLAYER_VILLAGER_DATA, new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 1));
		dataTracker.startTracking(Constants.DataTrackers.PLAYER_IS_BABY,false);
	}


	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void legemeton$writeCustomDataToNbt(NbtCompound compoundTag, CallbackInfo info) {
		LegemetonUtils.writeHaulerNbt(compoundTag, (PlayerEntity) (Object) this);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void legemeton$readCustomDataFromNbt(NbtCompound compoundTag, CallbackInfo info) {
		LegemetonUtils.readHaulerNbt(compoundTag, (PlayerEntity) (Object) this);
	}


	@Override
	public VillagerData getVillagerData() {
		return this.dataTracker.get(Constants.DataTrackers.PLAYER_VILLAGER_DATA);
	}

	@Override
	public void setVillagerData(VillagerData villagerData) {
		this.dataTracker.set(Constants.DataTrackers.PLAYER_VILLAGER_DATA, villagerData);
	}

	@Override
	public boolean getIsBaby() {
		return this.dataTracker.get(Constants.DataTrackers.PLAYER_IS_BABY);
	}

	@Override
	public void setIsBaby(boolean isBaby) {
		this.dataTracker.set(Constants.DataTrackers.PLAYER_IS_BABY, isBaby);
	}

	@Override
	public NbtCompound getCorpseEntity() {
		return this.dataTracker.get(Constants.DataTrackers.PLAYER_CORPSE_ENTITY);
	}

	@Override
	public void setCorpseEntity(NbtCompound entityNbt) {
		this.dataTracker.set(Constants.DataTrackers.PLAYER_CORPSE_ENTITY, entityNbt);
	}

}
