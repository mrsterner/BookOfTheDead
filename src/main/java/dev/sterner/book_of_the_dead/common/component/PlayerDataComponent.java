package dev.sterner.book_of_the_dead.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class PlayerDataComponent implements AutoSyncedComponent {
	private final PlayerEntity player;
	private int butcheringLevel = 0;
	private final int MAX_BUTCHERING_LEVEL = 10;
	private int necromancerLevel = 0;
	private final int MAX_NECROMANCER_LEVEL = 10;
	private final int MAX_ABILITY_LEVEL = 3;

	//Negatives
	private int healthDebuff = 0;
	private int saturationDebuff = 0;
	private int experienceDebuff = 0;
	private int aggressionLevel = 0;
	private int reputationDecrease = 0;
	private int mobSpawnRateDebuff = 0;
	private int insanityLevel = 0;

	//Positives
	private int undeadAggressionDecrease = 0;
	private int increasedResistance = 0;

	private int deathsTouchLevel = 0;
	private int statusEffectConversionLevel = 0;
	private int necroAuraLevel = 0;

	private int extraLives = 0;
	private int dispatchedExtraLivesMinions = 0;
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
		nbt.putInt(Constants.Nbt.AGGRESSION_DEBUFF, getAggressionLevel());
		nbt.putInt(Constants.Nbt.REPUTATION_DEBUFF, getReputationDecrease());
		nbt.putInt(Constants.Nbt.MOB_SPAWN_RATE_DEBUFF, getMobSpawnRateDebuff());
		nbt.putInt(Constants.Nbt.INSANITY_DEBUFF, getInsanityLevel());
		nbt.putInt(Constants.Nbt.UNDEAD_AGGRESSION_BUFF, getUndeadAggressionDecrease());
		nbt.putInt(Constants.Nbt.RESISTANCE_BUFF, getIncreasedResistance());
		nbt.putInt(Constants.Nbt.DEATHS_TOUCH_BUFF, getDeathsTouchLevel());
		nbt.putInt(Constants.Nbt.NECRO_AURA_BUFF, getNecroAuraLevel());
		nbt.putInt(Constants.Nbt.EXTRA_LIVES, getExtraLives());
		nbt.putInt(Constants.Nbt.DISPATCHED_MINIONS, getDispatchedExtraLivesMinions());
		nbt.putBoolean(Constants.Nbt.PHANTOM_IMMUNITY, getPhantomImmunity());
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		setButcheringLevel(nbt.getInt(Constants.Nbt.BUTCHERING_LEVEL));
		setNecromancerLevel(nbt.getInt(Constants.Nbt.NECROMANCER_LEVEL));
		setHealthDebuff(nbt.getInt(Constants.Nbt.HEALTH_DEBUFF));
		setSaturationDebuff(nbt.getInt(Constants.Nbt.SATURATION_DEBUFF));
		setExperienceDebuff(nbt.getInt(Constants.Nbt.EXPERIENCE_DEBUFF));
		setAggressionLevel(nbt.getInt(Constants.Nbt.AGGRESSION_DEBUFF));
		setReputationDecrease(nbt.getInt(Constants.Nbt.REPUTATION_DEBUFF));
		setMobSpawnRateModifier(nbt.getInt(Constants.Nbt.MOB_SPAWN_RATE_DEBUFF));
		setInsanityLevel(nbt.getInt(Constants.Nbt.INSANITY_DEBUFF));
		setUndeadAggressionDecrease(nbt.getInt(Constants.Nbt.UNDEAD_AGGRESSION_BUFF));
		setIncreasedResistance(nbt.getInt(Constants.Nbt.RESISTANCE_BUFF));
		setDeathsTouchLevel(nbt.getInt(Constants.Nbt.DEATHS_TOUCH_BUFF));
		setNecroAuraLevel(nbt.getInt(Constants.Nbt.NECRO_AURA_BUFF));
		setExtraLives(nbt.getInt(Constants.Nbt.EXTRA_LIVES));
		setDispatchedExtraLivesMinions(nbt.getInt(Constants.Nbt.DISPATCHED_MINIONS));
		setPhantomImmunity(nbt.getBoolean(Constants.Nbt.PHANTOM_IMMUNITY));

	}

	public float getNecroAuraBuffModifier(){
		return PlayerAbilityData.NECRO_AURA[getNecroAuraLevel()];
	}

	public float getStatusEffectConversionBuffModifier(){
		return PlayerAbilityData.STATUS_EFFECT_CONVERSION[getStatusEffectConversionLevel()];
	}

	public float getDeathsTouchBuffModifier(){
		return PlayerAbilityData.DEATHS_TOUCH[getDeathsTouchLevel()];
	}

	public float getResistanceBuffModifier(){
		return PlayerAbilityData.INCREASE_RESISTANCE[getIncreasedResistance()];
	}

	public float getUndeadAggressionBuffModifier(){
		return PlayerAbilityData.UNDEAD_AGGRESSION_DECREASE[getUndeadAggressionDecrease()];
	}

	public float getInsanityDebuffModifier(){
		return PlayerAbilityData.INSANITY[getInsanityLevel()];
	}

	public float getMobSpawnDebuffModifier(){
		return PlayerAbilityData.MOB_SPAWN[getMobSpawnRateDebuff()];
	}

	public float getReputationDebuffModifier(){
		return PlayerAbilityData.REPUTATION[getReputationDecrease()];
	}

	public float getAggressionDebuffModifier(){
		return PlayerAbilityData.AGGRESSION[getAggressionLevel()];
	}

	public float getExperienceDebuffModifier(){
		return PlayerAbilityData.EXPERIENCE_DECREASES[getExperienceDebuff()];
	}

	public float getSaturationDebuffModifier(){
		return PlayerAbilityData.SATURATION_DECREASES[getSaturationDebuff()];
	}

	public float getHealthDebuffModifier(){
		return PlayerAbilityData.HEALTH_DECREASES[getHealthDebuff()];
	}

	public float getButcheringModifier(){
		return PlayerAbilityData.BUTCHERING_MODIFIERS[getButcheringLevel()];
	}

	public float getNecromancerModifier(){
		return PlayerAbilityData.NECROMANCER_MODIFIERS[getNecromancerLevel()];
	}

	public void increaseButcheringLevel(int amount){
		if(getButcheringLevel() + amount <= MAX_BUTCHERING_LEVEL){
			setButcheringLevel(getButcheringLevel() + amount);
		}
	}

	public void decreaseButcheringLevel(int amount){
		if(getButcheringLevel() - amount >= 0){
			setButcheringLevel(getButcheringLevel() - amount);
		}
	}

	public void increaseNecromancerLevel(int amount){
		if(getNecromancerLevel() + amount <= MAX_NECROMANCER_LEVEL){
			setNecromancerLevel(getNecromancerLevel() + amount);
		}
	}

	public void decreaseNecromancerLevel(int amount){
		if(getNecromancerLevel() - amount >= 0){
			setNecromancerLevel(getNecromancerLevel() - amount);
		}
	}

	public void increaseHealthDebuffLevel(){
		if(getHealthDebuff() + 1 <= MAX_ABILITY_LEVEL){
			setHealthDebuff(getHealthDebuff() + 1);
		}
	}

	public void decreaseHealthDebuffLevel(){
		if(getHealthDebuff() - 1 >= 0){
			setHealthDebuff(getHealthDebuff() - 1);
		}
	}

	public void increaseSaturationDebuffLevel(){
		if(getSaturationDebuff() + 1 <= MAX_ABILITY_LEVEL){
			setSaturationDebuff(getSaturationDebuff() + 1);
		}
	}

	public void decreaseSaturationDebuffLevel(){
		if(getSaturationDebuff() - 1 >= 0){
			setSaturationDebuff(getSaturationDebuff() - 1);
		}
	}

	public void increaseExperienceDebuffLevel(){
		if(getExperienceDebuff() + 1 <= MAX_ABILITY_LEVEL){
			setExperienceDebuff(getExperienceDebuff() + 1);
		}
	}

	public void decreaseExperienceDebuffLevel(){
		if(getExperienceDebuff() - 1 >= 0){
			setExperienceDebuff(getExperienceDebuff() - 1);
		}
	}

	public void increaseAggressionDebuffLevel(){
		if(getAggressionLevel() + 1 <= MAX_ABILITY_LEVEL){
			setAggressionLevel(getAggressionLevel() + 1);
		}
	}

	public void decreaseAggressionDebuffLevel(){
		if(getAggressionLevel() - 1 >= 0){
			setAggressionLevel(getAggressionLevel() - 1);
		}
	}

	public void increaseReputationDebuffLevel(){
		if(getReputationDecrease() + 1 <= MAX_ABILITY_LEVEL){
			setReputationDecrease(getReputationDecrease() + 1);
		}
	}

	public void decreaseReputationDebuffLevel(){
		if(getReputationDecrease() - 1 >= 0){
			setReputationDecrease(getReputationDecrease() - 1);
		}
	}

	public void increaseMobSpawnDebuffLevel(){
		if(getMobSpawnRateDebuff() + 1 <= MAX_ABILITY_LEVEL){
			setMobSpawnRateModifier(getMobSpawnRateDebuff() + 1);
		}
	}

	public void decreaseMobSpawnDebuffLevel(){
		if(getMobSpawnRateDebuff() - 1 >= 0){
			setMobSpawnRateModifier(getMobSpawnRateDebuff() - 1);
		}
	}

	public void increaseInsanityDebuffLevel(){
		if(getInsanityLevel() + 1 <= MAX_ABILITY_LEVEL){
			setInsanityLevel(getInsanityLevel() + 1);
		}
	}

	public void decreaseInsanityDebuffLevel(){
		if(getInsanityLevel() - 1 >= 0){
			setInsanityLevel(getInsanityLevel() - 1);
		}
	}

	public void increaseUndeadAggressionBuffLevel(){
		if(getUndeadAggressionDecrease() + 1 <= MAX_ABILITY_LEVEL){
			setUndeadAggressionDecrease(getUndeadAggressionDecrease() + 1);
		}
	}

	public void decreaseUndeadAggressionBuffLevel(){
		if(getUndeadAggressionDecrease() - 1 >= 0){
			setUndeadAggressionDecrease(getUndeadAggressionDecrease() - 1);
		}
	}

	public void increaseResistanceBuffLevel(){
		if(getIncreasedResistance() + 1 <= MAX_ABILITY_LEVEL){
			setIncreasedResistance(getIncreasedResistance() + 1);
		}
	}

	public void decreaseResistanceBuffLevel(){
		if(getIncreasedResistance() - 1 >= 0){
			setIncreasedResistance(getIncreasedResistance() - 1);
		}
	}

	public void increaseDeathsTouchBuffLevel(){
		if(getDeathsTouchLevel() + 1 <= MAX_ABILITY_LEVEL){
			setDeathsTouchLevel(getDeathsTouchLevel() + 1);
		}
	}

	public void decreaseDeathsTouchBuffLevel(){
		if(getDeathsTouchLevel() - 1 >= 0){
			setDeathsTouchLevel(getDeathsTouchLevel() - 1);
		}
	}

	public void increaseStatusEffectConversionBuffLevel(){
		if(getStatusEffectConversionLevel() + 1 <= MAX_ABILITY_LEVEL){
			setStatusEffectConversionLevel(getStatusEffectConversionLevel() + 1);
		}
	}

	public void decreaseStatusEffectConversionBuffLevel(){
		if(getStatusEffectConversionLevel() - 1 >= 0){
			setStatusEffectConversionLevel(getStatusEffectConversionLevel() - 1);
		}
	}

	public void increaseNecroAuraBuffLevel(){
		if(getNecroAuraLevel() + 1 <= MAX_ABILITY_LEVEL){
			setNecroAuraLevel(getNecroAuraLevel() + 1);
		}
	}

	public void decreaseNecroAuraBuffLevel(){
		if(getNecroAuraLevel() - 1 >= 0){
			setNecroAuraLevel(getNecroAuraLevel() - 1);
		}
	}

	public void increaseExtraLivesBuffLevel(){
		if(getExtraLives() + 1 <= MAX_ABILITY_LEVEL){
			setExtraLives(getExtraLives() + 1);
		}
	}

	public void decreaseExtraLivesBuffLevel(){
		if(getExtraLives() - 1 >= 0){
			setExtraLives(getExtraLives() - 1);
		}
	}

	public void increaseDispatchedMinionBuffLevel(){
		if(getDispatchedExtraLivesMinions() + 1 <= MAX_ABILITY_LEVEL){
			setDispatchedExtraLivesMinions(getDispatchedExtraLivesMinions() + 1);
		}
	}

	public void decreaseDispatchedMinionBuffLevel(){
		if(getDispatchedExtraLivesMinions() - 1 >= 0){
			setDispatchedExtraLivesMinions(getDispatchedExtraLivesMinions() - 1);
		}
	}


	private int getButcheringLevel(){
		return butcheringLevel;
	}

	private void setButcheringLevel(int butcheringLevel){
		this.butcheringLevel = butcheringLevel;
		this.syncAbility();
	}

	private int getNecromancerLevel() {
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

	private int getAggressionLevel() {
		return aggressionLevel;
	}

	private void setAggressionLevel(int aggressionLevel) {
		this.aggressionLevel = aggressionLevel;
		this.syncAbility();
	}

	private int getReputationDecrease() {
		return reputationDecrease;
	}

	private void setReputationDecrease(int reputationDecrease) {
		this.reputationDecrease = reputationDecrease;
		this.syncAbility();
	}

	private int getMobSpawnRateDebuff() {
		return mobSpawnRateDebuff;
	}

	private void setMobSpawnRateModifier(int mobSpawnRateDebuff) {
		this.mobSpawnRateDebuff = mobSpawnRateDebuff;
		this.syncAbility();
	}

	private int getInsanityLevel() {
		return insanityLevel;
	}

	private void setInsanityLevel(int insanityLevel) {
		this.insanityLevel = insanityLevel;
		this.syncAbility();
	}

	private int getUndeadAggressionDecrease() {
		return undeadAggressionDecrease;
	}

	private void setUndeadAggressionDecrease(int undeadAggressionDecrease) {
		this.undeadAggressionDecrease = undeadAggressionDecrease;
		this.syncAbility();
	}

	private int getIncreasedResistance() {
		return increasedResistance;
	}

	private void setIncreasedResistance(int increasedResistance) {
		this.increasedResistance = increasedResistance;
		this.syncAbility();
	}

	private int getDeathsTouchLevel() {
		return deathsTouchLevel;
	}

	private void setDeathsTouchLevel(int deathsTouchLevel) {
		this.deathsTouchLevel = deathsTouchLevel;
		this.syncAbility();
	}

	private int getStatusEffectConversionLevel() {
		return statusEffectConversionLevel;
	}

	private void setStatusEffectConversionLevel(int statusEffectConversionLevel) {
		this.statusEffectConversionLevel = statusEffectConversionLevel;
		this.syncAbility();
	}

	private int getNecroAuraLevel() {
		return necroAuraLevel;
	}

	private void setNecroAuraLevel(int necroAuraLevel) {
		this.necroAuraLevel = necroAuraLevel;
		this.syncAbility();
	}

	private int getExtraLives() {
		return extraLives;
	}

	private void setExtraLives(int extraLives) {
		this.extraLives = extraLives;
		this.syncAbility();
	}

	private int getDispatchedExtraLivesMinions() {
		return dispatchedExtraLivesMinions;
	}

	private void setDispatchedExtraLivesMinions(int dispatchedExtraLivesMinions) {
		this.dispatchedExtraLivesMinions = dispatchedExtraLivesMinions;
		this.syncAbility();
	}

	public boolean getPhantomImmunity() {
		return phantomImmunity;
	}

	private void setPhantomImmunity(boolean phantomImmunity) {
		this.phantomImmunity = phantomImmunity;
		this.syncAbility();
	}

	private void syncAbility(){
		BotDComponents.PLAYER_COMPONENT.sync(this.player);
	}
}
