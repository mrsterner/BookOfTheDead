package dev.sterner.legemeton.common.util;

import dev.sterner.legemeton.common.entity.CorpseEntity;
import dev.sterner.legemeton.common.registry.LegemetonObjects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.EulerAngle;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerData;
import org.quiltmc.qsl.item.group.api.QuiltItemGroup;


public class Constants {

	public static final String MOD_ID = "legemeton";
	public static final QuiltItemGroup LEGEMETON_GROUP = QuiltItemGroup.builder(Constants.id("items")).icon(() -> new ItemStack(LegemetonObjects.BUTCHER_KNIFE)).build();

	public static Identifier id(String string){
		return new Identifier(MOD_ID, string);
	}

	public static class DataTrackers{
		public static final TrackedData<NbtCompound> HAULED_CORPSE = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.TAG_COMPOUND);

		public static final TrackedData<Boolean> IS_BABY = DataTracker.registerData(CorpseEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
		public static final TrackedData<VillagerData> VILLAGER_DATA = DataTracker.registerData(CorpseEntity.class, TrackedDataHandlerRegistry.VILLAGER_DATA);
		public static final TrackedData<NbtCompound> STORED_CORPSE_ENTITY = DataTracker.registerData(CorpseEntity.class, TrackedDataHandlerRegistry.TAG_COMPOUND);
		public static final TrackedData<EulerAngle> TRACKER_BODY_ROTATION = DataTracker.registerData(CorpseEntity.class, TrackedDataHandlerRegistry.ROTATION);
		public static final TrackedData<Boolean> IS_DYING = DataTracker.registerData(CorpseEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


		public static final TrackedData<Boolean> PLAYER_IS_BABY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
		public static final TrackedData<VillagerData>  PLAYER_VILLAGER_DATA = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.VILLAGER_DATA);
		public static final TrackedData<NbtCompound>  PLAYER_CORPSE_ENTITY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.TAG_COMPOUND);

	}

	public static class Tags {
		public static final TagKey<EntityType<?>> BUTCHERABLE = TagKey.of(Registry.ENTITY_TYPE_KEY, id("butcherable"));
	}

	public static class Nbt {

        public static final String TARGET_ROT = "Rotation";
        public static final String VILLAGER_DATA = "VillagerData";
		public static final String IS_BABY = "IsBaby";
		public static final String CORPSE_ENTITY = "CorpseEntity";
		public static final String IS_DYING = "IsDying";
	}
}
