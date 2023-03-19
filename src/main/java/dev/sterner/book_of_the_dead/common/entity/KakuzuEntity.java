package dev.sterner.book_of_the_dead.common.entity;

import dev.sterner.book_of_the_dead.common.entity.goal.FlyingWanderAroundGoal;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class KakuzuEntity extends PathAwareEntity {
	public static final TrackedData<Integer> VARIANT = DataTracker.registerData(KakuzuEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public static final TrackedData<Optional<UUID>> OWNER = DataTracker.registerData(KakuzuEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);


	public KakuzuEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
		this.moveControl = new FlightMoveControl(this, 20, true);
		this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(1, new FlyingWanderAroundGoal(this));
	}

	public void setVariant(int variant){
		this.getDataTracker().set(VARIANT, variant);
	}

	public int getVariant(){
		return this.getDataTracker().get(VARIANT);
	}

	public void setOwner(UUID uuid){
		this.getDataTracker().set(OWNER, Optional.ofNullable(uuid));
	}

	@Nullable
	public UUID getOwner(){
		return this.getDataTracker().get(OWNER).orElse(null);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(VARIANT, 1);
		dataTracker.startTracking(OWNER, Optional.empty());
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound tag) {
		super.readCustomDataFromNbt(tag);
		setVariant(tag.getInt(Constants.Nbt.VARIANT));
		if(tag.contains(Constants.Nbt.OWNER)){
			setOwner(tag.getUuid(Constants.Nbt.OWNER));
		}
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound tag) {
		super.writeCustomDataToNbt(tag);
		tag.putInt(Constants.Nbt.VARIANT, getVariant());
		if(getOwner() != null){
			tag.putUuid(Constants.Nbt.OWNER, getOwner());
		}
	}
}
