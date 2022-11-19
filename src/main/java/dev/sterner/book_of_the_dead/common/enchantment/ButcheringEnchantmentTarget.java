package dev.sterner.book_of_the_dead.common.enchantment;

import dev.sterner.book_of_the_dead.mixin.EnchantmentTargetMixin;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;

public class ButcheringEnchantmentTarget extends EnchantmentTargetMixin {
	@Override
	public boolean isAcceptableItem(Item item) {
		return item instanceof AxeItem;
	}
}
