package dev.sterner.book_of_the_dead.common.util;

import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.item.group.api.QuiltItemGroup;


public class Constants {

	public static final String MOD_ID = "book_of_the_dead";
	public static final QuiltItemGroup BOTD_GROUP = QuiltItemGroup.builder(Constants.id("items")).icon(() -> new ItemStack(BotDObjects.BUTCHER_KNIFE)).build();

	public static Identifier id(String string){
		return new Identifier(MOD_ID, string);
	}

	public static class Values{

		public static final int BLEEDING = 120;
		public static final double JAR_COLLECTION_RANGE = 10;
	}

	public static class DataTrackers{
		public static final TrackedData<NbtCompound> PLAYER_CORPSE_ENTITY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.TAG_COMPOUND);
	 }

	public static class Tags {
		public static final TagKey<EntityType<?>> BUTCHERABLE = TagKey.of(Registry.ENTITY_TYPE_KEY, id("butcherable"));
	}

	public static class Nbt {
		public static final String CORPSE_ENTITY = "CorpseEntity";
        public static final String HOOKED_AGE = "HookedAge";
        public static final String BLOOD_LEVEL = "BloodLevel";
        public static final String ALL_BLACK = "AllBlack";
        public static final String HAS_LEGEMETON = "HasBotD";
		public static final String HAS_EMERALD_TABLET = "HasEmeraldTablet";
		public static final String NECRO_RITUAL = "NecroRitual";
		public static final String TIMER = "Timer";
		public static final String AGE = "Age";
        public static final String PLAYER_UUID = "PlayerUuid";
        public static final String RITUAL_POS = "RitualPos";
		public static final String PEDESTAL_ITEM = "PedestalItem";
		public static final String CRAFTING_FINISHED = "CraftingFinished";
		public static final String CRAFTING = "Crafting";
        public static final String RITUAL_RECIPE = "RitualRecipe";
        public static final String START = "Start";
		public static final String SHOULD_RUN = "ShouldRun";
        public static final String CLIENT_TIMER = "ClientTimer";
        public static final String DURATION = "Duration";
		public static final String TARGET_Y = "TargetY";

        public static final String BUTCHERING_LEVEL = "ButcheringLevel";
		public static final String IS_CORPSE = "IsCorpse";
        public static final String HEAD_VISIBLE = "HeadVisible";
		public static final String RIGHT_ARM_VISIBLE = "RightArmVisible";
		public static final String LEFT_ARM_VISIBLE = "LeftArmVisible";
		public static final String RIGHT_LEG_VISIBLE = "RightLegVisible";
		public static final String LEFT_LEG_VISIBLE = "LeftLegVisible";
    }
}
