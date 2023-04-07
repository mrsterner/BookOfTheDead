package dev.sterner.book_of_the_dead.common.entity;

import dev.sterner.book_of_the_dead.common.registry.BotDEntityTypes;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class BloodParticleEntity extends Entity {
	public static final TrackedData<Integer> VARIANT = DataTracker.registerData(BloodParticleEntity.class, TrackedDataHandlerRegistry.INTEGER);

	public BloodParticleEntity(EntityType<?> variant, World world) {
		super(variant, world);
		int rand = world.getRandom().nextInt(8);
		this.dataTracker.set(BloodParticleEntity.VARIANT, rand);
	}


	@Override
	protected void initDataTracker() {
		dataTracker.startTracking(VARIANT, 0);
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		dataTracker.set(VARIANT, nbt.getInt(Constants.Nbt.VARIANT));
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		nbt.putInt(Constants.Nbt.VARIANT, dataTracker.get(VARIANT));
	}
}
