package dev.sterner.legemeton.api.interfaces;

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
}
