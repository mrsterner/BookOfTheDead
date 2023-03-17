package dev.sterner.book_of_the_dead.common.statuseffect;

import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.LivingEntityDataComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class MorphineStatusEffect extends StatusEffect {
	public MorphineStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}

	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		LivingEntityDataComponent component = BotDComponents.LIVING_COMPONENT.get(entity);
		entity.damage(entity.getDamageSources().generic(), component.getMorphine$accumulatedDamage());
		component.setMorphine$accumulatedDamage(0.0F);

		super.onRemoved(entity, attributes, amplifier);
	}
}
