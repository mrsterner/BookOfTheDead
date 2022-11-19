package dev.sterner.legemeton.common.util;

import dev.sterner.legemeton.common.entity.CorpseEntity;
import dev.sterner.legemeton.common.registry.LegemetonObjects;
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

	public static final String MOD_ID = "legemeton";
	public static final QuiltItemGroup LEGEMETON_GROUP = QuiltItemGroup.builder(Constants.id("items")).icon(() -> new ItemStack(LegemetonObjects.BUTCHER_KNIFE)).build();

	public static Identifier id(String string){
		return new Identifier(MOD_ID, string);
	}

	public static class Values{

		public static final int BLEEDING = 120;
		public static final double JAR_COLLECTION_RANGE = 10;
	}

	public static class DataTrackers{
		public static final TrackedData<NbtCompound> STORED_CORPSE_ENTITY = DataTracker.registerData(CorpseEntity.class, TrackedDataHandlerRegistry.TAG_COMPOUND);
		public static final TrackedData<NbtCompound>  PLAYER_CORPSE_ENTITY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.TAG_COMPOUND);
    }

	public static class Tags {
		public static final TagKey<EntityType<?>> BUTCHERABLE = TagKey.of(Registry.ENTITY_TYPE_KEY, id("butcherable"));
	}

	public static class Nbt {
		public static final String CORPSE_ENTITY = "CorpseEntity";
        public static final String HOOKED_AGE = "HookedAge";
        public static final String BLOOD_LEVEL = "BloodLevel";
        public static final String ALL_BLACK = "AllBlack";
        public static final String HAS_LEGEMETON = "HasLegemeton";
		public static final String HAS_EMERALD_TABLET = "HasEmeraldTablet";
		public static final String NECRO_RITUAL = "NecroRitual";
		public static final String TIMER = "Timer";
		public static final String AGE = "Age";
        public static final String PLAYER_UUID = "PlayerUuid";
    }
}
