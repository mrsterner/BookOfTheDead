package dev.sterner.legemeton.api.interfaces;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.village.VillagerData;

import java.util.Optional;

public interface Hauler {

	static Optional<Hauler> of(Object context) {
		if (context instanceof Hauler) {
			return Optional.of(((Hauler) context));
		}
		return Optional.empty();
	}

	NbtCompound getCorpseEntity();

	void setCorpseEntity(NbtCompound entityNbt);

}
