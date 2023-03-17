package dev.sterner.book_of_the_dead.common.statuseffect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class EuthanasiaStatusEffect extends StatusEffect {
	public EuthanasiaStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}

	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		entity.damage(entity.getDamageSources().outOfWorld(), Integer.MAX_VALUE);
		super.onRemoved(entity, attributes, amplifier);
	}
}
