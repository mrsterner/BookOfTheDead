package dev.sterner.legemeton.common.entity;

import dev.sterner.legemeton.common.registry.LegemetonTrades;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class OldManEntity extends VillagerEntity {
	public OldManEntity(EntityType<? extends VillagerEntity> entityType, World world) {
		super(entityType, world);
		((MobNavigation) this.getNavigation()).setCanPathThroughDoors(true);
		this.getNavigation().setCanSwim(true);
	}

	@Override
	protected void fillRecipes() {
		VillagerData villagerData = this.getVillagerData();
		TradeOfferList tradeOfferList = this.getOffers();
		if (villagerData.getLevel() == 1) {
			tradeOfferList.add(LegemetonTrades.LEGEMETON_OFFER.create(this, getRandom()));
		} else if(villagerData.getLevel() == 2){
			tradeOfferList.add(LegemetonTrades.CELLAR_KEY_OFFER.create(this, getRandom()));
		}

		Int2ObjectMap<TradeOffers.Factory[]> extraTrades = LegemetonTrades.OLD_MAN_TRADES;
		if (!extraTrades.isEmpty()) {
			TradeOffers.Factory[] tradeFactories = extraTrades.get(villagerData.getLevel());
			if (tradeFactories != null && tradeFactories.length > 0) {
				this.fillRecipesFromPool(tradeOfferList, tradeFactories, 1);
			}
		}
	}

	@Nullable
	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityTag) {
		entityData = super.initialize(world, difficulty, spawnReason, entityData, entityTag);
		initEquipment(random, difficulty);
		restock();
		setVillagerData(getVillagerData().withProfession(VillagerProfession.NITWIT));
		return entityData;
	}

	@Override
	public boolean cannotDespawn() {
		return true;
	}

	@Override
	public VillagerData getVillagerData() {
		return super.getVillagerData().withProfession(VillagerProfession.NITWIT);
	}

	@Override
	public boolean isReadyToBreed() {
		return false;
	}

	@Override
	public void tick() {
		if (age % 100 == 0) {
			heal(1);
		}
		if (age % 48000 == 0) {
			restock();
		}
		super.tick();
	}
}
