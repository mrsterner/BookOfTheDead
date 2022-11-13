package dev.sterner.legemeton.mixin;

import dev.sterner.legemeton.api.interfaces.Hauler;
import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity   {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "initDataTracker()V", at = @At("TAIL"))
	private void legemeton$initDataTracker(CallbackInfo info) {
		//dataTracker.startTracking(Constants.DataTrackers.PLAYER_CORPSE_ENTITY, new NbtCompound());
	}


	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void legemeton$writeCustomDataToNbt(NbtCompound compoundTag, CallbackInfo info) {
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void legemeton$readCustomDataFromNbt(NbtCompound compoundTag, CallbackInfo info) {
	}



}
