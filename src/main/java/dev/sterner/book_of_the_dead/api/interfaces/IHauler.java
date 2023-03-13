package dev.sterner.book_of_the_dead.api.interfaces;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.Optional;

public interface IHauler {

	static Optional<IHauler> of(Object context) {
		if (context instanceof IHauler) {
			return Optional.of(((IHauler) context));
		}
		return Optional.empty();
	}

	LivingEntity getCorpseLiving();

	NbtCompound getCorpseEntity();

	void setCorpseEntity(LivingEntity livingEntity);

	void clearCorpseData();


	boolean getHeadVisible();

	boolean getRArmVisible();

	boolean getLArmVisible();

	boolean getRLegVisible();

	boolean getLLegVisible();

	void setHeadVisible(boolean visible);

	void setRArmVisible(boolean visible);

	void setLArmVisible(boolean visible);

	void setRLegVisible(boolean visible);

	void setLLegVisible(boolean visible);
}
