package dev.sterner.legemeton.data;

import dev.sterner.legemeton.common.registry.LegemetonEnchantments;
import dev.sterner.legemeton.common.registry.LegemetonEntityTypes;
import dev.sterner.legemeton.common.registry.LegemetonObjects;
import dev.sterner.legemeton.common.util.Constants;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class LegemetonLanguageProvider extends FabricLanguageProvider {

	protected LegemetonLanguageProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	public void generateTranslations(TranslationBuilder builder) {
		builder.add(Constants.LEGEMETON_GROUP, "The Legemeton");

		builder.add(LegemetonObjects.LEGEMETON, "The Legemeton");
		builder.add(LegemetonObjects.BUTCHER_KNIFE, "Butcher Knife");
		builder.add(LegemetonObjects.CONTRACT, "Contract");
		builder.add(LegemetonObjects.SIGNED_CONTRACT, "Signed Contract");
		builder.add(LegemetonObjects.PACKET, "Packet");
		builder.add(LegemetonObjects.FAT, "Fat");
		builder.add(LegemetonObjects.SKIN, "Skin");
		builder.add(LegemetonObjects.BOTTLE_OF_BLOOD, "Bottle of Blood");
		builder.add(LegemetonObjects.COOKED_FLESH, "Cooked Flesh");
		builder.add(LegemetonObjects.FLESH, "Flesh");
		builder.add(LegemetonObjects.CAGE, "Cage");
		builder.add(LegemetonObjects.HOOK, "Hook");
		builder.add(LegemetonObjects.METAL_HOOK, "Metal Hook");
		builder.add(LegemetonObjects.ALL_BLACK, "All-Black");
		builder.add(LegemetonObjects.QUICKSILVER, "Quicksilver");
		builder.add(LegemetonObjects.SOUL_STONE, "Soul Stone");
		builder.add(LegemetonObjects.CINNABAR, "Cinnabar");
		builder.add(LegemetonObjects.CELLAR_KEY, "Cellar Key");
		builder.add(LegemetonObjects.REINFORCED_DOOR, "Reinforced Door");
		builder.add(LegemetonObjects.REINFORCED_BLOCK, "Reinforced Block");
		builder.add(LegemetonObjects.OLD_LETTER, "Old Letter");
		builder.add(LegemetonObjects.EMERALD_TABLET, "Emerald Tablet");

		builder.add(LegemetonEnchantments.BUTCHERING, "Butchering");

		builder.add(LegemetonEntityTypes.CORPSE_ENTITY, "Corpse");
		builder.add(LegemetonEntityTypes.OLD_MAN_ENTITY, "Old Man");
		builder.add("entity.legemeton.old_man.nitwit", "Old Man");

		builder.add(LegemetonObjects.JAR, "Jar");
		builder.add(LegemetonObjects.ROPE, "Rope");
		builder.add("rei.legemeton.butchering_drops", "Butchering Drops");
		builder.add("emi.category.legemeton.butchering", "Butchering Drops");
		builder.add("tooltip.legemeton.old_friend", "Letter from an old friend");
		builder.add("tooltip.legemeton.from_archive", "From Library Archive");
	}
}
