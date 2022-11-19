package dev.sterner.book_of_the_dead.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class BotDDataGen implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		fabricDataGenerator.addProvider(BotDBlockTagProvider::new);
		fabricDataGenerator.addProvider(BotDRecipeProvider::new);
		fabricDataGenerator.addProvider(BotDBlockLootTableProvider::new);
		fabricDataGenerator.addProvider(BotDModelProvider::new);
		fabricDataGenerator.addProvider(BotDEntityTagProvider::new);
		fabricDataGenerator.addProvider(BotDLanguageProvider::new);
	}
}
