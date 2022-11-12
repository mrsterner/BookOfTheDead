package dev.sterner.legemeton.common.registry;

import dev.sterner.legemeton.common.enchantment.ButcheringEnchantment;
import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.registry.Registry;

public class LegemetonEnchantments {
	public static final Enchantment BUTCHERING = new ButcheringEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND);

	public static void init() {
		Registry.register(Registry.ENCHANTMENT, Constants.id("butchering"), BUTCHERING);
	}
}
