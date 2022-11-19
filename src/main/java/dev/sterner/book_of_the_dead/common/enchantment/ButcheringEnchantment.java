package dev.sterner.book_of_the_dead.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class ButcheringEnchantment extends Enchantment {
	public ButcheringEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
		super(weight, target, slotTypes);
	}

	@Override
	public int getMinPower(int level) {
		return level * 25;
	}

	@Override
	public int getMaxPower(int level) {
		return this.getMinPower(level) + 50;
	}

	@Override
	public boolean isTreasure() {
		return false;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}
}
