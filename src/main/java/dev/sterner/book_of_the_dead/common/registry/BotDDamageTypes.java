package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;

public interface BotDDamageTypes {
	RegistryKey<DamageType> SANGUINE = register("sanguine");
	RegistryKey<DamageType> SACRIFICE = register("sacrifice");

	static RegistryKey<DamageType> register(String name) {
		return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Constants.id(name));
	}

	static DamageSource getDamageSource(World world, RegistryKey<DamageType> type) {
		return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).getHolderOrThrow(type));
	}

	static void init() {
	}
}
