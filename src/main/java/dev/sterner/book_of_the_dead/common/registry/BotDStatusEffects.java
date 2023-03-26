package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.common.statuseffect.*;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public interface BotDStatusEffects {
	Map<StatusEffect, Identifier> STATUS_EFFECTS = new LinkedHashMap<>();

	StatusEffect EUTHANASIA = register("euthanasia", new EuthanasiaStatusEffect(StatusEffectType.NEUTRAL, 0xffffff));
	StatusEffect ADRENALINE = register("adrenaline", new EmptyStatusEffect(StatusEffectType.NEUTRAL, 0xffffff));
	StatusEffect MORPHINE = register("morphine", new MorphineStatusEffect(StatusEffectType.NEUTRAL, 0xffffff));
	StatusEffect SANGUINE = register("sanguine_infection", new SanguineInfectionStatusEffect(StatusEffectType.HARMFUL));
	StatusEffect SOUL_SIPHON = register("soul_siphon", new SoulSiphonStatusEffect(StatusEffectType.HARMFUL));
	StatusEffect SOUL_SICKNESS = register("soul_sickness", new SoulSicknessStatusEffect(StatusEffectType.HARMFUL));

	static <T extends StatusEffect> T register(String name, T effect) {
		STATUS_EFFECTS.put(effect, Constants.id(name));
		return effect;
	}

	static void init() {
		STATUS_EFFECTS.keySet().forEach(effect -> Registry.register(Registries.STATUS_EFFECT, STATUS_EFFECTS.get(effect), effect));
	}
}
