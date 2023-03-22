package dev.sterner.book_of_the_dead.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.Set;

public class LivingEntityDataComponent implements AutoSyncedComponent, ServerTickingComponent {
	public final float[] ENTANGLE_STRENGTH = {0.15f, 0.25f, 0.35f, 0.5f, 0.6f, 0.75f, 0.85f};
	public LivingEntity livingEntity;
	private float morphine$accumulatedDamage = 0;
	private float adrenaline$bonusDamage = 0;
	private int entangledEntityId = 0;

	public LivingEntityDataComponent(LivingEntity livingEntity){
		this.livingEntity = livingEntity;
	}

	@Override
	public void serverTick() {

	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		setMorphine$accumulatedDamage(nbt.getFloat(Constants.Nbt.MORPHINE));
		setAdrenaline$bonusDamage(nbt.getFloat(Constants.Nbt.ADRENALINE));
		setEntangledEntityId(nbt.getInt(Constants.Nbt.ENTANGLED));
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putFloat(Constants.Nbt.MORPHINE, getMorphine$accumulatedDamage());
		nbt.putFloat(Constants.Nbt.ADRENALINE, getAdrenaline$bonusDamage());
		nbt.putInt(Constants.Nbt.ENTANGLED, getEntangledEntityId());
	}

	public float getEntangleStrength(LivingEntity source, LivingEntity target, boolean isSource){
		int i = 0;
		if(source instanceof PlayerEntity && target instanceof PlayerEntity){
			i = 6;
		} else if(source instanceof PlayerEntity playerSource){
			i++;
			if(target instanceof TameableEntity tameableEntity && tameableEntity.isOwner(playerSource)){
				i += 4;
			}else if(target instanceof MerchantEntity){
				i++;
			}
			if(target instanceof IllagerEntity){
				i += 2;
			}
		} else if(target instanceof PlayerEntity playerSource){
			i++;
			if(source instanceof TameableEntity tameableEntity && tameableEntity.isOwner(playerSource)){
				i += 4;
			}else if(source instanceof MerchantEntity){
				i++;
			}
			if(source instanceof IllagerEntity){
				i += 2;
			}
		}

		float strength = ENTANGLE_STRENGTH[MathHelper.clamp(i, 0, ENTANGLE_STRENGTH.length)];

		return isSource ? 1 - strength : strength;
	}

	public int getEntangledEntityId() {
		return entangledEntityId;
	}

	public void setEntangledEntityId(int entangledEntityId) {
		this.entangledEntityId = entangledEntityId;
		syncAbility();
	}

	public float getMorphine$accumulatedDamage() {
		return morphine$accumulatedDamage;
	}

	public void setMorphine$accumulatedDamage(float morphine$accumulatedDamage) {
		this.morphine$accumulatedDamage = morphine$accumulatedDamage;
		syncAbility();
	}

	public void increaseMorphine$accumulatedDamage(float damage){
		this.setMorphine$accumulatedDamage(getMorphine$accumulatedDamage() + damage);
	}

	public float getAdrenaline$bonusDamage() {
		return adrenaline$bonusDamage;
	}

	public void setAdrenaline$bonusDamage(float adrenaline$bonusDamage) {
		this.adrenaline$bonusDamage = adrenaline$bonusDamage;
		syncAbility();
	}

	public void increaseAdrenaline$bonusDamage(float damage){
		setAdrenaline$bonusDamage(getAdrenaline$bonusDamage() + damage);
	}

	private void syncAbility(){
		BotDComponents.LIVING_COMPONENT.sync(this.livingEntity);
	}


}
