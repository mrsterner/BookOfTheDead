package dev.sterner.book_of_the_dead.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class BotDDataGen implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
		FabricDataGenerator.Pack pack = dataGenerator.createPack();
		pack.addProvider(BotDBlockTagProvider::new);
		pack.addProvider(BotDRecipeProvider::new);
		pack.addProvider(BotDBlockLootTableProvider::new);

		pack.addProvider(BotDModelProvider::new);
		pack.addProvider(BotDEntityTagProvider::new);
		pack.addProvider(BotDLanguageProvider::new);

	}
}
