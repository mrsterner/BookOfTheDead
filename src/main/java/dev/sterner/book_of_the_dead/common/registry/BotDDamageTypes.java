package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import net.minecraft.world.gen.BootstrapContext;
import org.jetbrains.annotations.Nullable;

public class BotDDamageTypes {
	public static final RegistryKey<DamageType> SANGUINE = register("sanguine");
    public static final RegistryKey<DamageType> SACRIFICE = register("sacrifice");

    public static RegistryKey<DamageType> register(String name) {
		return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Constants.id(name));
	}

	public static DamageSource getDamageSource(World world, RegistryKey<DamageType> type) {
		return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).getHolderOrThrow(type));
	}

	public static void init() {
	}
}
