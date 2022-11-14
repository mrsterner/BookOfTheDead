package dev.sterner.legemeton.common.entity;

import dev.sterner.legemeton.api.interfaces.Hauler;
import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.EulerAngle;
import net.minecraft.world.World;


public class CorpseEntity extends PathAwareEntity {
	private static final EulerAngle DEFAULT_BODY_ROTATION = new EulerAngle(0.0F, 0.0F, 0.0F);
	private EulerAngle bodyRotation = DEFAULT_BODY_ROTATION;
	public LivingEntity storedCorpseEntity;


	public CorpseEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
	}

	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(Constants.DataTrackers.STORED_CORPSE_ENTITY,  new NbtCompound());
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
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10);
	}

	@Override
	public EntityDimensions getDimensions(EntityPose pose) {
		return  EntityDimensions.changing(1, 0.5f);
	}

	@Override
	protected void dropLoot(DamageSource source, boolean causedByPlayer) {
		NbtCompound entityNbt = new NbtCompound();
		NbtCompound nbtCompound = storedCorpseEntity.writeNbt(entityNbt);
		EntityType.get(nbtCompound.getString("id")).ifPresent(type -> {
			Entity entity = type.create(this.world);
			if(entity instanceof LivingEntity livingEntity){
				Identifier identifier = livingEntity.getLootTable();
				LootTable lootTable = this.world.getServer().getLootManager().getTable(identifier);
				LootContext.Builder builder = this.getLootContextBuilder(causedByPlayer, source);
				lootTable.generateLoot(builder.build(LootContextTypes.ENTITY), this::dropStack);
			}
		});
	}

	@Override
	protected ActionResult interactMob(PlayerEntity player, Hand hand) {
		if(hand == Hand.MAIN_HAND){
			System.out.println("corpseStored: " + storedCorpseEntity);
			System.out.println("data: " + getCorpseEntity());
		}
		return super.interactMob(player, hand);
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		if(storedCorpseEntity != null){
			nbt.put(Constants.Nbt.CORPSE_ENTITY, getCorpseEntity());
		}
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		EntityType.get(nbt.getCompound(Constants.Nbt.CORPSE_ENTITY).toString()).ifPresent(type -> {
			if(type.create(this.world) instanceof LivingEntity livingEntity){
				setCorpseEntity(livingEntity);
			}
		});
	}

	//Getters and Setters


	public NbtCompound getCorpseEntity() {
		return this.dataTracker.get(Constants.DataTrackers.STORED_CORPSE_ENTITY);
	}



	public void setCorpseEntity(LivingEntity entity) {
		NbtCompound nbtCompound = new NbtCompound();
		NbtCompound entityNbt = entity.writeNbt(nbtCompound);
		//nbtCompound.put(Constants.Nbt.CORPSE_ENTITY, entityNbt);

		nbtCompound.putString("id", entity.getSavedEntityId());
		entity.writeNbt(nbtCompound);

		this.dataTracker.set(Constants.DataTrackers.STORED_CORPSE_ENTITY, nbtCompound);
		this.storedCorpseEntity = entity;
	}


/*
	@Override
	public String getIdentifierId() {
		return this.dataTracker.get(Constants.DataTrackers.IDENTIFIER_ID);
	}

	@Override
	public void setIdentifierId(String string) {
		this.dataTracker.set(Constants.DataTrackers.IDENTIFIER_ID, string);
	}

	@Override
	public NbtCompound getCorpseEntity() {
		return this.dataTracker.get(Constants.DataTrackers.STORED_CORPSE_ENTITY);
	}

	@Override
	public void setCorpseEntity(NbtCompound entityNbt) {
		this.dataTracker.set(Constants.DataTrackers.STORED_CORPSE_ENTITY, entityNbt);
	}

	public void setBodyRotation(EulerAngle angle) {
		this.bodyRotation = angle;
		this.dataTracker.set(Constants.DataTrackers.TRACKER_BODY_ROTATION, angle);
	}

	public EulerAngle getBodyRotation() {
		return this.bodyRotation;
	}


	@Override
	public void setVillagerData(VillagerData villagerData) {
		this.dataTracker.set(Constants.DataTrackers.VILLAGER_DATA, villagerData);
	}


	@Override
	public VillagerData getVillagerData() {
		return this.dataTracker.get(Constants.DataTrackers.VILLAGER_DATA);
	}

	@Override
	public void setIsBaby(boolean isBaby) {
		this.dataTracker.set(Constants.DataTrackers.IS_BABY, isBaby);
	}


	@Override
	public boolean getIsBaby() {
		return this.dataTracker.get(Constants.DataTrackers.IS_BABY);
	}


	public void setIsDying(boolean isDying) {
		this.dataTracker.set(Constants.DataTrackers.IS_DYING, isDying);
	}

	public boolean getIsDying() {
		return this.dataTracker.get(Constants.DataTrackers.IS_DYING);
	}

 */

}
