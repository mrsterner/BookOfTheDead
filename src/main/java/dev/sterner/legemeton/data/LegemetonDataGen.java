package dev.sterner.legemeton.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class LegemetonDataGen implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		fabricDataGenerator.addProvider(LegemetonBlockTagProvider::new);
		fabricDataGenerator.addProvider(LegemetonRecipeProvider::new);
		fabricDataGenerator.addProvider(LegemetonBlockLootTableProvider::new);
		fabricDataGenerator.addProvider(LegemetonModelProvider::new);
		fabricDataGenerator.addProvider(LegemetonEntityTagProvider::new);
		fabricDataGenerator.addProvider(LegemetonLanguageProvider::new);
	}
}
