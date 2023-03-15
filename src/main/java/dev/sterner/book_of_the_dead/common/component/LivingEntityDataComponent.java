package dev.sterner.book_of_the_dead.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

public class LivingEntityDataComponent implements AutoSyncedComponent, ServerTickingComponent {
	public LivingEntity livingEntity;
	private float morphine$accumulatedDamage = 0;
	private float adrenaline$bonusDamage = 0;

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
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putFloat(Constants.Nbt.MORPHINE, getMorphine$accumulatedDamage());
		nbt.putFloat(Constants.Nbt.ADRENALINE, getAdrenaline$bonusDamage());
	}

	public float getMorphine$accumulatedDamage() {
		return morphine$accumulatedDamage;
	}

	public void setMorphine$accumulatedDamage(float morphine$accumulatedDamage) {
		this.morphine$accumulatedDamage = morphine$accumulatedDamage;
		BotDComponents.LIVING_COMPONENT.sync(livingEntity);
	}

	public void increaseMorphine$accumulatedDamage(float damage){
		this.setMorphine$accumulatedDamage(getMorphine$accumulatedDamage() + damage);
	}

	public float getAdrenaline$bonusDamage() {
		return adrenaline$bonusDamage;
	}

	public void setAdrenaline$bonusDamage(float adrenaline$bonusDamage) {
		this.adrenaline$bonusDamage = adrenaline$bonusDamage;
		BotDComponents.LIVING_COMPONENT.sync(livingEntity);
	}

	public void increaseAdrenaline$bonusDamage(float damage){
		setAdrenaline$bonusDamage(getAdrenaline$bonusDamage() + damage);
	}
}
