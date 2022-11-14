package dev.sterner.legemeton.api.interfaces;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.Optional;

public interface Hauler {

	static Optional<Hauler> of(Object context) {
		if (context instanceof Hauler) {
			return Optional.of(((Hauler) context));
		}
		return Optional.empty();
	}

	NbtCompound getCorpseEntity();

	void setCorpseEntity(LivingEntity livingEntity);

	void clearCorpseData();
}
