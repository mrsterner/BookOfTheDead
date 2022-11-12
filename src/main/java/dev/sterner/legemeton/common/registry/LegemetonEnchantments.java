package dev.sterner.legemeton.common.registry;

import com.chocohead.mm.api.ClassTinkerers;
import dev.sterner.legemeton.common.enchantment.ButcheringEnchantment;
import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public interface LegemetonEnchantments {
	Map<Identifier, Enchantment> ENCHANTMENTS = new LinkedHashMap<>();

	EnchantmentTarget AXE = ClassTinkerers.getEnum(EnchantmentTarget.class, "AXE");


	Enchantment BUTCHERING = register("butchering", new ButcheringEnchantment(Enchantment.Rarity.RARE, AXE, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND));

	static <T extends Enchantment> T register(String id, T enchantment) {
		ENCHANTMENTS.put(Constants.id(id), enchantment);
		return enchantment;
	}

	static void init() {
		ENCHANTMENTS.forEach((id, enchantment) -> Registry.register(Registry.ENCHANTMENT, id, enchantment));
	}
}
