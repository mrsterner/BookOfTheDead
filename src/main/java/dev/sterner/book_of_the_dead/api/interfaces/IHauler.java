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

	default boolean getHeadVisible() {
		return true;
	}

	default boolean getRArmVisible() {
		return true;
	}

	default boolean getLArmVisible() {
		return true;
	}

	default boolean getRLegVisible() {
		return true;
	}

	default boolean getLLegVisible() {
		return true;
	}

	void setHeadVisible(boolean visible);

	void setRArmVisible(boolean visible);

	void setLArmVisible(boolean visible);

	void setRLegVisible(boolean visible);

	void setLLegVisible(boolean visible);

	default void setAllVisible() {
		setHeadVisible(true);
		setLArmVisible(true);
		setLLegVisible(true);
		setRArmVisible(true);
		setRLegVisible(true);
	}
}
