package dev.sterner.legemeton.common.util;

import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.item.group.api.QuiltItemGroup;


public class Constants {

	public static final String MOD_ID = "legemeton";
	public static final QuiltItemGroup LEGEMETON_GROUP = QuiltItemGroup.builder(Constants.id("items")).icon(() -> new ItemStack(Items.AIR)).build();

	public static Identifier id(String string){
		return new Identifier(MOD_ID, string);
	}

	public static class DataTrackers{

	}

	public static class Tags {
		public static final TagKey<EntityType<?>> BUTCHERABLE = TagKey.of(Registry.ENTITY_TYPE_KEY, id("butcherable"));
	}

	public static class Nbt {

	}
}
