package dev.sterner.legemeton.common.entity;

import dev.sterner.legemeton.api.interfaces.Hauler;
import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;


public class CorpseEntity extends PathAwareEntity implements Hauler {
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



	public static DefaultAttributeContainer.Builder createAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10);
	}


	@Override
	public EntityDimensions getDimensions(EntityPose pose) {
		/*
		if(storedCorpseEntity != null) {
			EntityDimensions dimensions = storedCorpseEntity.getType().getDimensions();
			return EntityDimensions.changing(dimensions.width, dimensions.height);
		}

		 */
		return EntityDimensions.changing(1, 0.5f);
	}

	@Override
	protected void dropLoot(DamageSource source, boolean causedByPlayer) {
		if(storedCorpseEntity != null) {
			Identifier identifier = storedCorpseEntity.getLootTable();
			LootTable lootTable = this.world.getServer().getLootManager().getTable(identifier);
			LootContext.Builder builder = this.getLootContextBuilder(causedByPlayer, source);
			lootTable.generateLoot(builder.build(LootContextTypes.ENTITY), this::dropStack);
		}
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
		EntityType.getEntityFromNbt(nbt.getCompound(Constants.Nbt.CORPSE_ENTITY), this.world).ifPresent(type -> {
			if(type instanceof LivingEntity livingEntity){
				setCorpseEntity( livingEntity);
			}
		});
	}

	@Override
	public NbtCompound getCorpseEntity() {
		return this.dataTracker.get(Constants.DataTrackers.STORED_CORPSE_ENTITY);
	}

	@Override
	public void setCorpseEntity(LivingEntity entity) {
		NbtCompound nbtCompound = new NbtCompound();
		nbtCompound.putString("id", entity.getSavedEntityId());
		entity.writeNbt(nbtCompound);
		this.dataTracker.set(Constants.DataTrackers.STORED_CORPSE_ENTITY, nbtCompound);
		this.storedCorpseEntity = entity;
	}

	@Override
	public void clearCorpseData() {
		this.storedCorpseEntity = null;
		this.dataTracker.set(Constants.DataTrackers.STORED_CORPSE_ENTITY, new NbtCompound());
	}
}
