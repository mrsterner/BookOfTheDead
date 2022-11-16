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
		builder.add(LegemetonObjects.SLICED_FLESH, "The Legemeton");
		builder.add(LegemetonObjects.COOKED_SLICED_FLESH, "Cooked Sliced Flesh");
		builder.add(LegemetonObjects.FLESH, "Flesh");
		builder.add(LegemetonObjects.CAGE, "Cage");
		builder.add(LegemetonObjects.HOOK, "Hook");

		builder.add(LegemetonEnchantments.BUTCHERING, "Butchering");

		builder.add(LegemetonEntityTypes.CORPSE_ENTITY, "Corpse");
		builder.add(LegemetonObjects.JAR, "Jar");
		builder.add(LegemetonObjects.ROPE, "Rope");
	}
}
