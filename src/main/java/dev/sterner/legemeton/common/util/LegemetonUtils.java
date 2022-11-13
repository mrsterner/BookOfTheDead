package dev.sterner.legemeton.common.util;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import dev.sterner.legemeton.Legemeton;
import dev.sterner.legemeton.api.interfaces.Hauler;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.village.VillagerData;

public class LegemetonUtils {

	public static NbtCompound writeHaulerNbt(NbtCompound nbt, Entity entity){
		Hauler.of(entity).ifPresent(hauler -> {
			if (!hauler.getCorpseEntity().isEmpty()) {
				nbt.put(Constants.Nbt.CORPSE_ENTITY, hauler.getCorpseEntity());
				nbt.putBoolean(Constants.Nbt.IS_BABY, hauler.getIsBaby());
				VillagerData.CODEC.encodeStart(NbtOps.INSTANCE, hauler.getVillagerData()).resultOrPartial(Legemeton.LOGGER::error).ifPresent(nbtElement -> nbt.put(Constants.Nbt.VILLAGER_DATA, nbtElement));
			}
		});
		return nbt;
	}

	public static void readHaulerNbt(NbtCompound nbt, Entity entity){
		Hauler.of(entity).ifPresent(hauler -> {
			if (nbt.contains(Constants.Nbt.CORPSE_ENTITY, NbtElement.COMPOUND_TYPE)) {
				hauler.setCorpseEntity(nbt.getCompound(Constants.Nbt.CORPSE_ENTITY));
				hauler.setIsBaby(nbt.getBoolean(Constants.Nbt.IS_BABY));

				if (nbt.contains(Constants.Nbt.VILLAGER_DATA, NbtElement.COMPOUND_TYPE)) {
					DataResult<VillagerData> dataResult = VillagerData.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, nbt.get(Constants.Nbt.VILLAGER_DATA)));
					dataResult.resultOrPartial(Legemeton.LOGGER::error).ifPresent(hauler::setVillagerData);
				}
			}
		});
	}
}
