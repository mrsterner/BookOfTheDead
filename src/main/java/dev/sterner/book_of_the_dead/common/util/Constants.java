package dev.sterner.book_of_the_dead.common.util;

import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;


public class Constants {

	public static final String MODID = "book_of_the_dead";

	public static final ItemGroup BOTD_GROUP = FabricItemGroup.builder(Constants.id("items")).icon(() -> new ItemStack(BotDObjects.MEAT_CLEAVER)).build();

	public static Identifier id(String string) {
		return new Identifier(MODID, string);
	}

	public interface Values {
		int BLEEDING = 120;
		double JAR_COLLECTION_RANGE = 10;
	}

	public interface DataTrackers {
		TrackedData<NbtCompound> PLAYER_CORPSE_ENTITY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.TAG_COMPOUND);
	}

	public interface Tags {
		TagKey<EntityType<?>> BUTCHERABLE = TagKey.of(RegistryKeys.ENTITY_TYPE, id("butcherable"));
		TagKey<EntityType<?>> CAGEABLE_BLACKLIST = TagKey.of(RegistryKeys.ENTITY_TYPE, id("cageable_blacklist"));

		TagKey<EntityType<?>> SOUL_WEAK = TagKey.of(RegistryKeys.ENTITY_TYPE, id("soul_weak"));
		TagKey<EntityType<?>> SOUL_REGULAR = TagKey.of(RegistryKeys.ENTITY_TYPE, id("soul_regular"));
		TagKey<EntityType<?>> SOUL_STRONG = TagKey.of(RegistryKeys.ENTITY_TYPE, id("soul_strong"));
	}

	public interface Nbt {
		String CORPSE_ENTITY = "CorpseEntity";
		String HOOKED_AGE = "HookedAge";
		String ALL_BLACK = "AllBlack";
		String HAS_LEGEMETON = "HasBotD";
		String HAS_EMERALD_TABLET = "HasEmeraldTablet";
		String NECRO_RITUAL = "NecroRitual";
		String TIMER = "Timer";
		String AGE = "Age";
		String PLAYER_UUID = "PlayerUuid";
		String RITUAL_POS = "RitualPos";
		String PEDESTAL_ITEM = "PedestalItem";
		String CRAFTING_FINISHED = "CraftingFinished";
		String CRAFTING = "Crafting";
		String RITUAL_RECIPE = "RitualRecipe";
		String START = "Start";
		String SHOULD_RUN = "ShouldRun";
		String CLIENT_TIMER = "ClientTimer";
		String DURATION = "Duration";
		String TARGET_Y = "TargetY";

		String BUTCHERING_LEVEL = "ButcheringLevel";
		String IS_CORPSE = "IsCorpse";
		String HEAD_VISIBLE = "HeadVisible";
		String RIGHT_ARM_VISIBLE = "RightArmVisible";
		String LEFT_ARM_VISIBLE = "LeftArmVisible";
		String RIGHT_LEG_VISIBLE = "RightLegVisible";
		String LEFT_LEG_VISIBLE = "LeftLegVisible";
		String CONTRACT = "Contract";
		String NAME = "Name";
		String UUID = "Uuid";
		String STATUS_EFFECT_INSTANCE = "StatusEffectInstance";
		String STATUS_EFFECT = "StatusEffect";
		String AMPLIFIER = "Amplifier";
		String MORPHINE = "Morphine";
		String ADRENALINE = "Adrenaline";
		String FILTHY = "Filthy";
		String CLEANING = "Cleaning";
		String LATTER = "Latter";
		String NECROMANCER_LEVEL = "NecromancerLevel";
		String HEALTH_DEBUFF = "HealthDebuff";
		String SATURATION_DEBUFF = "SaturationDebuff";
		String EXPERIENCE_DEBUFF = "ExperienceDebuff";
		String AGGRESSION_DEBUFF = "AggressionDebuff";
		String REPUTATION_DEBUFF = "ReputationDebuff";
		String MOB_SPAWN_RATE_DEBUFF = "MobSpawnRateDebuff";
		String INSANITY_DEBUFF = "InsanityDebuff";
		String UNDEAD_AGGRESSION_BUFF = "UndeadAggressionBuff";
		String RESISTANCE_BUFF = "ResistanceBuff";
		String NECRO_AURA_BUFF = "NecroAuraBuff";
		String EXTRA_LIVES = "ExtraLives";
		String DISPATCHED_MINIONS = "DispatchedMinions";
		String PHANTOM_IMMUNITY = "PhantomImmunity";
		String DEATHS_TOUCH_BUFF = "DeathsTouch";
		String VARIANT = "Variant";
		String OWNER = "Owner";
		String MODE = "Mode";
		String BLOOD = "Blood";
		String STORED_ENTITY = "StoredEntity";
		String IS_LICH = "IsLich";
		String ENTANGLED = "Entangled";
		String ID = "Id";
		String BRAIN = "Brain";
		String LIQUID_TYPE = "LiquidType";
		String LIQUID_LEVEL = "LiquidLevel";
		String BLOOD_LEVEL = "BloodLevel";
		String SKIN_PROFILE = "SkinProfile";
		String COLOR = "Color";
		String HEAT_TIMER = "HeatTimer";
		String PROGRESS = "Progress";
		String HAS_LIQUID = "HasLiquid";
        String IS_NECRO = "IsNecro";
    }
}
