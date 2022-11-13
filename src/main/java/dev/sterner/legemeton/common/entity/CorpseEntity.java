package dev.sterner.legemeton.common.entity;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import dev.sterner.legemeton.Legemeton;
import dev.sterner.legemeton.api.interfaces.Hauler;
import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.EulerAngle;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.world.World;

public class CorpseEntity extends PathAwareEntity implements Hauler {
	private static final EulerAngle DEFAULT_BODY_ROTATION = new EulerAngle(0.0F, 0.0F, 0.0F);
	private EulerAngle bodyRotation = DEFAULT_BODY_ROTATION;

	public CorpseEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
	}

	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(Constants.DataTrackers.STORED_CORPSE_ENTITY,  new NbtCompound());
		this.dataTracker.startTracking(Constants.DataTrackers.TRACKER_BODY_ROTATION,  DEFAULT_BODY_ROTATION);
		this.dataTracker.startTracking(Constants.DataTrackers.VILLAGER_DATA, new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 1));
		this.dataTracker.startTracking(Constants.DataTrackers.IS_BABY,  false);
		this.dataTracker.startTracking(Constants.DataTrackers.IS_DYING,  true);
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

	@Override
	protected void dropLoot(DamageSource source, boolean causedByPlayer) {
		NbtCompound nbtCompound = getCorpseEntity();
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
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean(Constants.Nbt.IS_BABY, getIsBaby());
		nbt.putBoolean(Constants.Nbt.IS_DYING, getIsDying());
		if (!this.getCorpseEntity().isEmpty()) {
			nbt.put(Constants.Nbt.CORPSE_ENTITY, this.getCorpseEntity());
		}
		VillagerData.CODEC
				.encodeStart(NbtOps.INSTANCE, this.getVillagerData())
				.resultOrPartial(Legemeton.LOGGER::error)
				.ifPresent(nbtElement -> nbt.put(Constants.Nbt.VILLAGER_DATA, nbtElement));
		if (!DEFAULT_BODY_ROTATION.equals(getBodyRotation())) {
			nbt.put(Constants.Nbt.TARGET_ROT, getBodyRotation().toNbt());
		}
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		setIsBaby(nbt.getBoolean(Constants.Nbt.IS_BABY));
		setIsDying(nbt.getBoolean(Constants.Nbt.IS_DYING));
		if (nbt.contains(Constants.Nbt.CORPSE_ENTITY, NbtElement.COMPOUND_TYPE)) {
			this.setCorpseEntity(nbt.getCompound(Constants.Nbt.CORPSE_ENTITY));
		}
		if (nbt.contains(Constants.Nbt.VILLAGER_DATA, NbtElement.COMPOUND_TYPE)) {
			DataResult<VillagerData> dataResult = VillagerData.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, nbt.get(Constants.Nbt.VILLAGER_DATA)));
			dataResult.resultOrPartial(Legemeton.LOGGER::error).ifPresent(this::setVillagerData);
		}
		NbtList nbtList2 = nbt.getList(Constants.Nbt.TARGET_ROT, NbtElement.FLOAT_TYPE);
		setBodyRotation(nbtList2.isEmpty() ? DEFAULT_BODY_ROTATION : new EulerAngle(nbtList2));
	}

	//Getters and Setters

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

}
