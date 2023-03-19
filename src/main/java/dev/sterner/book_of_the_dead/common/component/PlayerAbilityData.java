package dev.sterner.book_of_the_dead.common.component;


import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PlayerAbilityData {

	private static final EntityAttributeModifier RESISTANCE_1, RESISTANCE_2, RESISTANCE_3, HEALTH_DECREASE_1, HEALTH_DECREASE_2, HEALTH_DECREASE_3;
	private final Set<EntityAttributeModifier> RESISTANCE_SET = new HashSet<>(List.of(RESISTANCE_1, RESISTANCE_2, RESISTANCE_3));
	private final Set<EntityAttributeModifier> HEALTH_DECREASE_SET = new HashSet<>(List.of(HEALTH_DECREASE_1, HEALTH_DECREASE_2, HEALTH_DECREASE_3));

	//index 0 is the default value, which should be either 0 or 1.

	public static final float[] BUTCHERING_MODIFIERS = {0.0f, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f};
	public static final float[] NECROMANCER_MODIFIERS = {0.0f, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f};

	//Debuffs
	public static final float[] HEALTH_DECREASES = {0, 2.0f, 4.0f, 6.0f};
	public static final float[] SATURATION_DECREASES = {1, 0.95f, 0.75f, 0.5f};
	public static final float[] EXPERIENCE_DECREASES = {1, 0.95f, 0.85f, 0.75f};

	public static final float[] AGGRESSION = {1, 1.15f, 1.25f, 1.5f};
	public static final float[] REPUTATION = {1, 0.85f, 0.75f, 0.5f};
	public static final float[] MOB_SPAWN = {1, 1.1f, 1.25f, 1.5f};
	public static final float[] INSANITY = {0.0f, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f};

	//Buffs
	public static final float[] UNDEAD_AGGRESSION_DECREASE = {1, 0.85f, 0.5f, 0.1f};
	public static final float[] INCREASE_RESISTANCE = {0, 1.0f, 2.0f, 3.0f};
	public static final float[] DEATHS_TOUCH = {0, 0.05f, 0.15f, 0.25f};
	public static final float[] STATUS_EFFECT_CONVERSION = {0, 0.25f, 0.5f, 0.75f};
	public static final float[] NECRO_AURA = {0, 0.25f, 0.5f, 1.0f};

	public void addOrReplaceAttribute(PlayerEntity player, EntityAttributeModifier attributeMod, boolean remove) {
		EntityAttributeInstance armor = player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR);
		if (armor != null) {
			for (EntityAttributeModifier mod : RESISTANCE_SET) {
				if (armor.hasModifier(mod)) {
					armor.removeModifier(mod);
				}
			}
			if(!remove && RESISTANCE_SET.contains(attributeMod)){
				armor.addPersistentModifier(attributeMod);
			}
		}

		EntityAttributeInstance health = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
		if (health != null) {
			for (EntityAttributeModifier mod : HEALTH_DECREASE_SET) {
				if (health.hasModifier(mod)) {
					health.removeModifier(mod);
				}
			}
			if(!remove && HEALTH_DECREASE_SET.contains(attributeMod)) {
				health.addPersistentModifier(attributeMod);
			}
		}
	}

	static {
		RESISTANCE_1 = new EntityAttributeModifier(UUID.fromString("6e138e72-7d06-449e-b09a-e3babed33120"), "Resistance Lvl 1", INCREASE_RESISTANCE[1], EntityAttributeModifier.Operation.ADDITION);
		RESISTANCE_2 = new EntityAttributeModifier(UUID.fromString("0d278102-1e00-4296-ae46-7406a4dd9ceb"), "Resistance Lvl 2", INCREASE_RESISTANCE[2], EntityAttributeModifier.Operation.ADDITION);
		RESISTANCE_3 = new EntityAttributeModifier(UUID.fromString("56b73540-12f7-4e72-854d-c49a53d7df6f"), "Resistance Lvl 3", INCREASE_RESISTANCE[3], EntityAttributeModifier.Operation.ADDITION);

		HEALTH_DECREASE_1 = new EntityAttributeModifier(UUID.fromString("853df0ee-6867-4952-957a-963a1cf4eba7"), "Health Decrease Lvl 1", - HEALTH_DECREASES[1], EntityAttributeModifier.Operation.ADDITION);
		HEALTH_DECREASE_2 = new EntityAttributeModifier(UUID.fromString("2c298f92-6f8a-4cae-9a16-d577d20cc946"), "Health Decrease Lvl 2", - HEALTH_DECREASES[2], EntityAttributeModifier.Operation.ADDITION);
		HEALTH_DECREASE_3 = new EntityAttributeModifier(UUID.fromString("5fcfc969-e2d8-4719-b5af-1d2ef9291617"), "Health Decrease Lvl 3", - HEALTH_DECREASES[3], EntityAttributeModifier.Operation.ADDITION);
	}
}
