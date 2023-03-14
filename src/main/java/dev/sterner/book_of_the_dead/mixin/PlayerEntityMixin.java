package dev.sterner.book_of_the_dead.mixin;

import dev.sterner.book_of_the_dead.api.interfaces.IHauler;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements IHauler {
	@Unique
	public LivingEntity storedCorpseEntity;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "initDataTracker()V", at = @At("TAIL"))
	private void book_of_the_dead$initDataTracker(CallbackInfo info) {
		dataTracker.startTracking(Constants.DataTrackers.PLAYER_CORPSE_ENTITY, new NbtCompound());
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void book_of_the_dead$writeCustomDataToNbt(NbtCompound compoundTag, CallbackInfo info) {
		if(storedCorpseEntity != null){
			compoundTag.put(Constants.Nbt.CORPSE_ENTITY, getCorpseEntity());
		}
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void book_of_the_dead$readCustomDataFromNbt(NbtCompound compoundTag, CallbackInfo info) {
		EntityType.getEntityFromNbt(compoundTag.getCompound(Constants.Nbt.CORPSE_ENTITY), this.world).ifPresent(type -> {
			if(type instanceof LivingEntity livingEntity){
				setCorpseEntity(livingEntity);
			}
		});
	}

	@Override
	public void clearCorpseData() {
		this.dataTracker.set(Constants.DataTrackers.PLAYER_CORPSE_ENTITY, new NbtCompound());
		this.storedCorpseEntity = null;
	}

	@Override
	public NbtCompound getCorpseEntity() {
		return this.dataTracker.get(Constants.DataTrackers.PLAYER_CORPSE_ENTITY);
	}

	@Override
	public LivingEntity getCorpseLiving() {
		return storedCorpseEntity;
	}

	@Override
	public void setCorpseEntity(LivingEntity entity) {
		NbtCompound nbtCompound = new NbtCompound();
		nbtCompound.putString("id", entity.getSavedEntityId());
		entity.writeNbt(nbtCompound);
		this.dataTracker.set(Constants.DataTrackers.PLAYER_CORPSE_ENTITY, nbtCompound);
		this.storedCorpseEntity = entity;
	}
}
