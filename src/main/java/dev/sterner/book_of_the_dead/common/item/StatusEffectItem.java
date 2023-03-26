package dev.sterner.book_of_the_dead.common.item;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;

public class StatusEffectItem extends Item {
	private final StatusEffect effect;

	public StatusEffectItem(Settings settings, StatusEffect effect) {
		super(settings);
		this.effect = effect;
	}

	public StatusEffect getStatusEffect() {
		return effect;
	}
}
