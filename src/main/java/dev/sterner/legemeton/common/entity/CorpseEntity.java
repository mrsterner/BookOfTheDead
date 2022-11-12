package dev.sterner.legemeton.common.entity;

import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.EulerAngle;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.world.World;

public class CorpseEntity extends PathAwareEntity {
	private static final TrackedData<Boolean> IS_BABY = DataTracker.registerData(CorpseEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<VillagerData> VILLAGER_DATA = DataTracker.registerData(CorpseEntity.class, TrackedDataHandlerRegistry.VILLAGER_DATA);
	private static final TrackedData<NbtCompound> CORPSE_ENTITY = DataTracker.registerData(CorpseEntity.class, TrackedDataHandlerRegistry.TAG_COMPOUND);
	public static final TrackedData<EulerAngle> TRACKER_BODY_ROTATION = DataTracker.registerData(CorpseEntity.class, TrackedDataHandlerRegistry.ROTATION);
	private static final EulerAngle DEFAULT_BODY_ROTATION = new EulerAngle(0.0F, 0.0F, 0.0F);
	private EulerAngle bodyRotation = DEFAULT_BODY_ROTATION;

	public CorpseEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
	}

	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(CORPSE_ENTITY,  new NbtCompound());
		this.dataTracker.startTracking(TRACKER_BODY_ROTATION,  DEFAULT_BODY_ROTATION);
		this.dataTracker.startTracking(VILLAGER_DATA, new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 1));
		this.dataTracker.startTracking(IS_BABY,  false);
	}

	@Override
	public boolean shouldRenderName() {
		return false;
	}

	@Override
	public boolean collides() {
		return this.age > 20 && super.collides();
	}



	public static DefaultAttributeContainer.Builder createAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 20);
	}

	public NbtCompound getCorpseEntity() {
		return this.dataTracker.get(CORPSE_ENTITY);
	}

	public void setCorpseEntity(NbtCompound entityNbt) {
		this.dataTracker.set(CORPSE_ENTITY, entityNbt);
	}

	public void setBodyRotation(EulerAngle angle) {
		this.bodyRotation = angle;
		this.dataTracker.set(TRACKER_BODY_ROTATION, angle);
	}

	public EulerAngle getBodyRotation() {
		return this.bodyRotation;
	}


	public void setVillagerData(VillagerData villagerData) {
		this.dataTracker.set(VILLAGER_DATA, villagerData);
	}


	public VillagerData getVillagerData() {
		return this.dataTracker.get(VILLAGER_DATA);
	}

	public void setIsBaby(boolean isBaby) {
		this.dataTracker.set(IS_BABY, isBaby);
	}


	public boolean getIsBaby() {
		return this.dataTracker.get(IS_BABY);
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		if (!this.getCorpseEntity().isEmpty()) {
			nbt.put(Constants.Nbt.TARGET, this.getCorpseEntity());
		}
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (nbt.contains(Constants.Nbt.TARGET, NbtElement.COMPOUND_TYPE)) {
			this.setCorpseEntity(nbt.getCompound(Constants.Nbt.TARGET));
		}
	}
}
