package dev.sterner.book_of_the_dead.common.component;


public class PlayerAbilityData {

	public PlayerAbilityData(){

	}
	//index 0 is the default value, which should be either 0 or 1.

	public static final float[] BUTCHERING_MODIFIERS = {0.0f, 0.1f, 0.25f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f};
	public static final float[] NECROMANCER_MODIFIERS = {0.0f, 0.1f, 0.25f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f};

	//Debuffs
	public static final float[] HEALTH_DECREASES = {0, 2.0f, 4.0f, 6.0f};
	public static final float[] SATURATION_DECREASES = {0, 0.95f, 0.75f, 0.5f};
	public static final float[] EXPERIENCE_DECREASES = {0, 0.95f, 0.85f, 0.75f};

	public static final float[] AGGRESSION = {1, 1.15f, 1.25f, 1.5f};
	public static final float[] REPUTATION = {1, 0.85f, 0.75f, 0.5f};
	public static final float[] MOB_SPAWN = {1, 1.1f, 1.25f, 1.5f};
	public static final float[] INSANITY = {0, 0.25f, 0.5f, 0.75f};

	//Buffs
	public static final float[] UNDEAD_AGGRESSION_DECREASE = {1, 0.85f, 0.5f, 0.1f};
	public static final float[] INCREASE_RESISTANCE = {0, 1.0f, 2.0f, 3.0f};
	public static final float[] DEATHS_TOUCH = {0, 0.05f, 0.15f, 0.25f};
	public static final float[] STATUS_EFFECT_CONVERSION = {0, 0.25f, 0.5f, 0.75f};
	public static final float[] NECRO_AURA = {0, 0.25f, 0.5f, 1.0f};
}
