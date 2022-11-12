package dev.sterner.legemeton.common.enchantment;

import dev.sterner.legemeton.mixin.EnchantmentTargetMixin;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;

public class ButcheringEnchantmentTarget extends EnchantmentTargetMixin {
	@Override
	public boolean isAcceptableItem(Item item) {
		return item instanceof AxeItem;
	}
}
