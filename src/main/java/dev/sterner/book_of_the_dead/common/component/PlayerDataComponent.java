package dev.sterner.book_of_the_dead.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlayerDataComponent implements AutoSyncedComponent {
	private final PlayerEntity player;
	private int butcheringLevel = 0;
	private static final int MAX_BUTCHERING_LEVEL = 10;
	private int necromancerLevel = 0;
	private static final int MAX_NECROMANCER_LEVEL = 10;

	private static final int MAX_INSANITY_LEVEL = 10;
	private static final int MAX_ABILITY_LEVEL = 3;

	//Negatives
	private int healthDebuff = 0;
	private int saturationDebuff = 0;
	private int experienceDebuff = 0;
	private int aggressionDebuff = 0;
	private int reputationDebuff = 0;
	private int mobSpawnRateDebuff = 0;
	private int insanityDebuff = 0;

	//Positives
	private int undeadAggressionBuff = 0;
	private int resistanceBuff = 0;
	private int deathsTouchBuff = 0;
	private int statusEffectConversionBuff = 0;
	private int necroAuraBuff = 0;

	private int kakuzu = 0;
	private int dispatchedKakuzuMinions = 0;

	private boolean isLich = false;

	private UUID entangledUuid = null;

	private boolean phantomImmunity = false;

	public PlayerDataComponent(PlayerEntity player) {
		this.player = player;
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putInt(Constants.Nbt.BUTCHERING_LEVEL, butcheringLevel);
		nbt.putInt(Constants.Nbt.NECROMANCER_LEVEL, getNecromancerLevel());
		nbt.putInt(Constants.Nbt.HEALTH_DEBUFF, getHealthDebuff());
		nbt.putInt(Constants.Nbt.SATURATION_DEBUFF, getSaturationDebuff());
		nbt.putInt(Constants.Nbt.EXPERIENCE_DEBUFF, getExperienceDebuff());
		nbt.putInt(Constants.Nbt.AGGRESSION_DEBUFF, getAggressionDebuff());
		nbt.putInt(Constants.Nbt.REPUTATION_DEBUFF, getReputationDebuff());
		nbt.putInt(Constants.Nbt.MOB_SPAWN_RATE_DEBUFF, getMobSpawnRateDebuff());
		nbt.putInt(Constants.Nbt.INSANITY_DEBUFF, getInsanityDebuff());
		nbt.putInt(Constants.Nbt.UNDEAD_AGGRESSION_BUFF, getUndeadAggressionBuff());
		nbt.putInt(Constants.Nbt.RESISTANCE_BUFF, getResistanceBuff());
		nbt.putInt(Constants.Nbt.DEATHS_TOUCH_BUFF, getDeathsTouchBuff());
		nbt.putInt(Constants.Nbt.NECRO_AURA_BUFF, getNecroAuraBuff());
		nbt.putInt(Constants.Nbt.EXTRA_LIVES, getKakuzu());
		nbt.putInt(Constants.Nbt.DISPATCHED_MINIONS, getDispatchedKakuzuMinions());
		nbt.putBoolean(Constants.Nbt.PHANTOM_IMMUNITY, getPhantomImmunity());
		nbt.putBoolean(Constants.Nbt.IS_LICH, getLich());
		if (getEntangledUuid() != null) {
			nbt.putUuid(Constants.Nbt.UUID, getEntangledUuid());
		}
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		setButcheringLevel(nbt.getInt(Constants.Nbt.BUTCHERING_LEVEL));
		setNecromancerLevel(nbt.getInt(Constants.Nbt.NECROMANCER_LEVEL));
		setHealthDebuff(nbt.getInt(Constants.Nbt.HEALTH_DEBUFF));
		setSaturationDebuff(nbt.getInt(Constants.Nbt.SATURATION_DEBUFF));
		setExperienceDebuff(nbt.getInt(Constants.Nbt.EXPERIENCE_DEBUFF));
		setAggressionDebuff(nbt.getInt(Constants.Nbt.AGGRESSION_DEBUFF));
		setReputationDebuff(nbt.getInt(Constants.Nbt.REPUTATION_DEBUFF));
		setMobSpawnRateDebuff(nbt.getInt(Constants.Nbt.MOB_SPAWN_RATE_DEBUFF));
		setInsanityDebuff(nbt.getInt(Constants.Nbt.INSANITY_DEBUFF));
		setUndeadAggressionBuff(nbt.getInt(Constants.Nbt.UNDEAD_AGGRESSION_BUFF));
		setReputationDebuff(nbt.getInt(Constants.Nbt.RESISTANCE_BUFF));
		setDeathsTouchBuff(nbt.getInt(Constants.Nbt.DEATHS_TOUCH_BUFF));
		setNecroAuraBuff(nbt.getInt(Constants.Nbt.NECRO_AURA_BUFF));
		setKakuzu(nbt.getInt(Constants.Nbt.EXTRA_LIVES));
		setDispatchedKakuzuMinions(nbt.getInt(Constants.Nbt.DISPATCHED_MINIONS));
		setPhantomImmunity(nbt.getBoolean(Constants.Nbt.PHANTOM_IMMUNITY));
		setLich(nbt.getBoolean(Constants.Nbt.IS_LICH));
		if (getEntangled()) {
			setEntangled(nbt.getUuid(Constants.Nbt.UUID));
		}
	}

	public float getNecroAuraBuffModifier() {
		return PlayerAbilityData.NECRO_AURA[getNecroAuraBuff()];
	}

	public float getStatusEffectConversionBuffModifier() {
		return PlayerAbilityData.STATUS_EFFECT_CONVERSION[getStatusEffectConversionBuff()];
	}

	public float getDeathsTouchBuffModifier() {
		return PlayerAbilityData.DEATHS_TOUCH[getDeathsTouchBuff()];
	}

	public float getResistanceBuffModifier() {
		return PlayerAbilityData.INCREASE_RESISTANCE[getResistanceBuff()];
	}

	public float getUndeadAggressionBuffModifier() {
		return PlayerAbilityData.UNDEAD_AGGRESSION_DECREASE[getUndeadAggressionBuff()];
	}

	public float getInsanityDebuffModifier() {
		return PlayerAbilityData.INSANITY[getInsanityDebuff()];
	}

	public float getMobSpawnDebuffModifier() {
		return PlayerAbilityData.MOB_SPAWN[getMobSpawnRateDebuff()];
	}

	public float getReputationDebuffModifier() {
		return PlayerAbilityData.REPUTATION[getReputationDebuff()];
	}

	public float getAggressionDebuffModifier() {
		return PlayerAbilityData.AGGRESSION[getAggressionDebuff()];
	}

	public float getExperienceDebuffModifier() {
		return PlayerAbilityData.EXPERIENCE_DECREASES[getExperienceDebuff()];
	}

	public float getSaturationDebuffModifier() {
		return PlayerAbilityData.SATURATION_DECREASES[getSaturationDebuff()];
	}

	public float getHealthDebuffModifier() {
		return PlayerAbilityData.HEALTH_DECREASES[getHealthDebuff()];
	}

	public float getButcheringModifier() {
		return PlayerAbilityData.BUTCHERING_MODIFIERS[getButcheringLevel()];
	}

	public float getNecromancerModifier() {
		return PlayerAbilityData.NECROMANCER_MODIFIERS[getNecromancerLevel()];
	}

	public void increaseButcheringLevel(int amount) {
		if (getButcheringLevel() + amount <= MAX_BUTCHERING_LEVEL) {
			setButcheringLevel(getButcheringLevel() + amount);
		}
	}

	public void decreaseButcheringLevel(int amount) {
		if (getButcheringLevel() - amount >= 0) {
			setButcheringLevel(getButcheringLevel() - amount);
		}
	}

	public void increaseNecromancerLevel(int amount) {
		if (getNecromancerLevel() + amount <= MAX_NECROMANCER_LEVEL) {
			setNecromancerLevel(getNecromancerLevel() + amount);
		}
	}

	public void decreaseNecromancerLevel(int amount) {
		if (getNecromancerLevel() - amount >= 0) {
			setNecromancerLevel(getNecromancerLevel() - amount);
		}
	}

	public void increaseHealthDebuffLevel() {
		if (getHealthDebuff() + 1 <= MAX_ABILITY_LEVEL) {
			setHealthDebuff(getHealthDebuff() + 1);
		}
	}

	public void decreaseHealthDebuffLevel() {
		if (getHealthDebuff() - 1 >= 0) {
			setHealthDebuff(getHealthDebuff() - 1);
		}
	}

	public void increaseSaturationDebuffLevel() {
		if (getSaturationDebuff() + 1 <= MAX_ABILITY_LEVEL) {
			setSaturationDebuff(getSaturationDebuff() + 1);
		}
	}

	public void decreaseSaturationDebuffLevel() {
		if (getSaturationDebuff() - 1 >= 0) {
			setSaturationDebuff(getSaturationDebuff() - 1);
		}
	}

	public void increaseExperienceDebuffLevel() {
		if (getExperienceDebuff() + 1 <= MAX_ABILITY_LEVEL) {
			setExperienceDebuff(getExperienceDebuff() + 1);
		}
	}

	public void decreaseExperienceDebuffLevel() {
		if (getExperienceDebuff() - 1 >= 0) {
			setExperienceDebuff(getExperienceDebuff() - 1);
		}
	}

	public void increaseAggressionDebuffLevel() {
		if (getAggressionDebuff() + 1 <= MAX_ABILITY_LEVEL) {
			setAggressionDebuff(getAggressionDebuff() + 1);
		}
	}

	public void decreaseAggressionDebuffLevel() {
		if (getAggressionDebuff() - 1 >= 0) {
			setAggressionDebuff(getAggressionDebuff() - 1);
		}
	}

	public void increaseReputationDebuffLevel() {
		if (getReputationDebuff() + 1 <= MAX_ABILITY_LEVEL) {
			setReputationDebuff(getReputationDebuff() + 1);
		}
	}

	public void decreaseReputationDebuffLevel() {
		if (getReputationDebuff() - 1 >= 0) {
			setReputationDebuff(getReputationDebuff() - 1);
		}
	}

	public void increaseMobSpawnDebuffLevel() {
		if (getMobSpawnRateDebuff() + 1 <= MAX_ABILITY_LEVEL) {
			setMobSpawnRateDebuff(getMobSpawnRateDebuff() + 1);
		}
	}

	public void decreaseMobSpawnDebuffLevel() {
		if (getMobSpawnRateDebuff() - 1 >= 0) {
			setMobSpawnRateDebuff(getMobSpawnRateDebuff() - 1);
		}
	}

	public void increaseInsanityDebuffLevel() {
		if (getInsanityDebuff() + 1 <= MAX_INSANITY_LEVEL) {
			setInsanityDebuff(getInsanityDebuff() + 1);
		}
	}

	public void decreaseInsanityDebuffLevel() {
		if (getInsanityDebuff() - 1 >= 0) {
			setInsanityDebuff(getInsanityDebuff() - 1);
		}
	}

	public void increaseUndeadAggressionBuffLevel() {
		if (getUndeadAggressionBuff() + 1 <= MAX_ABILITY_LEVEL) {
			setUndeadAggressionBuff(getUndeadAggressionBuff() + 1);
		}
	}

	public void decreaseUndeadAggressionBuffLevel() {
		if (getUndeadAggressionBuff() - 1 >= 0) {
			setUndeadAggressionBuff(getUndeadAggressionBuff() - 1);
		}
	}

	public void increaseResistanceBuffLevel() {
		if (getResistanceBuff() + 1 <= MAX_ABILITY_LEVEL) {
			setResistanceBuff(getResistanceBuff() + 1);
		}
	}

	public void decreaseResistanceBuffLevel() {
		if (getResistanceBuff() - 1 >= 0) {
			setResistanceBuff(getResistanceBuff() - 1);
		}
	}

	public void increaseDeathsTouchBuffLevel() {
		if (getDeathsTouchBuff() + 1 <= MAX_ABILITY_LEVEL) {
			setDeathsTouchBuff(getDeathsTouchBuff() + 1);
		}
	}

	public void decreaseDeathsTouchBuffLevel() {
		if (getDeathsTouchBuff() - 1 >= 0) {
			setDeathsTouchBuff(getDeathsTouchBuff() - 1);
		}
	}

	public void increaseStatusEffectConversionBuffLevel() {
		if (getStatusEffectConversionBuff() + 1 <= MAX_ABILITY_LEVEL) {
			setStatusEffectConversionBuff(getStatusEffectConversionBuff() + 1);
		}
	}

	public void decreaseStatusEffectConversionBuffLevel() {
		if (getStatusEffectConversionBuff() - 1 >= 0) {
			setStatusEffectConversionBuff(getStatusEffectConversionBuff() - 1);
		}
	}

	public void increaseNecroAuraBuffLevel() {
		if (getNecroAuraBuff() + 1 <= MAX_ABILITY_LEVEL) {
			setNecroAuraBuff(getNecroAuraBuff() + 1);
		}
	}

	public void decreaseNecroAuraBuffLevel() {
		if (getNecroAuraBuff() - 1 >= 0) {
			setNecroAuraBuff(getNecroAuraBuff() - 1);
		}
	}

	public void increaseKakuzuBuffLevel() {
		if (getKakuzu() + 1 <= MAX_ABILITY_LEVEL) {
			setKakuzu(getKakuzu() + 1);
		}
	}

	public void decreaseKakuzuBuffLevel() {
		if (getKakuzu() - 1 >= 0) {
			setKakuzu(getKakuzu() - 1);
		}
	}

	public void increaseDispatchedMinionBuffLevel() {
		if (getDispatchedKakuzuMinions() + 1 <= MAX_ABILITY_LEVEL) {
			setDispatchedKakuzuMinions(getDispatchedKakuzuMinions() + 1);
		}
	}

	public void decreaseDispatchedMinionBuffLevel() {
		if (getDispatchedKakuzuMinions() - 1 >= 0) {
			setDispatchedKakuzuMinions(getDispatchedKakuzuMinions() - 1);
		}
	}


	public int getButcheringLevel() {
		return butcheringLevel;
	}

	private void setButcheringLevel(int butcheringLevel) {
		this.butcheringLevel = butcheringLevel;
		this.syncAbility();
	}

	public int getNecromancerLevel() {
		return necromancerLevel;
	}

	private void setNecromancerLevel(int necromancerLevel) {
		this.necromancerLevel = necromancerLevel;
		this.syncAbility();
	}

	private int getHealthDebuff() {
		return healthDebuff;
	}

	private void setHealthDebuff(int healthDebuff) {
		this.healthDebuff = healthDebuff;
		this.syncAbility();
	}

	private int getSaturationDebuff() {
		return saturationDebuff;
	}

	private void setSaturationDebuff(int saturationDebuff) {
		this.saturationDebuff = saturationDebuff;
		this.syncAbility();
	}

	private int getExperienceDebuff() {
		return experienceDebuff;
	}

	private void setExperienceDebuff(int experienceDebuff) {
		this.experienceDebuff = experienceDebuff;
		this.syncAbility();
	}

	private int getAggressionDebuff() {
		return aggressionDebuff;
	}

	private void setAggressionDebuff(int aggressionDebuff) {
		this.aggressionDebuff = aggressionDebuff;
		this.syncAbility();
	}

	private int getReputationDebuff() {
		return reputationDebuff;
	}

	private void setReputationDebuff(int reputationDebuff) {
		this.reputationDebuff = reputationDebuff;
		this.syncAbility();
	}

	private int getMobSpawnRateDebuff() {
		return mobSpawnRateDebuff;
	}

	private void setMobSpawnRateDebuff(int mobSpawnRateDebuff) {
		this.mobSpawnRateDebuff = mobSpawnRateDebuff;
		this.syncAbility();
	}

	private int getInsanityDebuff() {
		return insanityDebuff;
	}

	private void setInsanityDebuff(int insanityDebuff) {
		this.insanityDebuff = insanityDebuff;
		this.syncAbility();
	}

	private int getUndeadAggressionBuff() {
		return undeadAggressionBuff;
	}

	private void setUndeadAggressionBuff(int undeadAggressionBuff) {
		this.undeadAggressionBuff = undeadAggressionBuff;
		this.syncAbility();
	}

	private int getResistanceBuff() {
		return resistanceBuff;
	}

	private void setResistanceBuff(int resistanceBuff) {
		this.resistanceBuff = resistanceBuff;
		this.syncAbility();
	}

	private int getDeathsTouchBuff() {
		return deathsTouchBuff;
	}

	private void setDeathsTouchBuff(int deathsTouchBuff) {
		this.deathsTouchBuff = deathsTouchBuff;
		this.syncAbility();
	}

	private int getStatusEffectConversionBuff() {
		return statusEffectConversionBuff;
	}

	private void setStatusEffectConversionBuff(int statusEffectConversionBuff) {
		this.statusEffectConversionBuff = statusEffectConversionBuff;
		this.syncAbility();
	}

	private int getNecroAuraBuff() {
		return necroAuraBuff;
	}

	private void setNecroAuraBuff(int necroAuraBuff) {
		this.necroAuraBuff = necroAuraBuff;
		this.syncAbility();
	}

	public int getKakuzu() {
		return kakuzu;
	}

	private void setKakuzu(int kakuzu) {
		this.kakuzu = kakuzu;
		this.syncAbility();
	}

	public int getDispatchedKakuzuMinions() {
		return dispatchedKakuzuMinions;
	}

	private void setDispatchedKakuzuMinions(int dispatchedKakuzuMinions) {
		this.dispatchedKakuzuMinions = dispatchedKakuzuMinions;
		this.syncAbility();
	}

	public boolean getPhantomImmunity() {
		return phantomImmunity;
	}

	private void setPhantomImmunity(boolean phantomImmunity) {
		this.phantomImmunity = phantomImmunity;
		this.syncAbility();
	}

	public boolean getLich() {
		return isLich;
	}


	public void setLich(boolean isLich) {
		this.isLich = isLich;
		this.syncAbility();
	}

	public boolean getEntangled() {
		return entangledUuid != null;
	}

	@Nullable
	public UUID getEntangledUuid() {
		return entangledUuid;
	}

	private void setEntangled(UUID uuid) {
		this.entangledUuid = uuid;
	}

	private void syncAbility() {
		BotDComponents.PLAYER_COMPONENT.sync(this.player);
	}


}
