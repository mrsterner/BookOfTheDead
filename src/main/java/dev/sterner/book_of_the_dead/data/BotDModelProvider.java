package dev.sterner.book_of_the_dead.data;

import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.model.BlockStateModelGenerator;
import net.minecraft.data.client.model.Models;
import net.minecraft.state.property.Properties;

public class BotDModelProvider extends FabricModelProvider {

	public BotDModelProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		blockStateModelGenerator.registerDoor(BotDObjects.REINFORCED_DOOR);
		blockStateModelGenerator.registerCrop(BotDObjects.POPPY_CROP, Properties.AGE_7, 0, 1, 2, 2, 3, 3, 3, 5);
		blockStateModelGenerator.registerNorthDefaultHorizontalRotation(BotDObjects.RETORT_FLASK_BLOCK);
	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		itemModelGenerator.register(BotDObjects.BOOK_OF_THE_DEAD, Models.GENERATED);
		itemModelGenerator.register(BotDObjects.SIGNED_CONTRACT, Models.GENERATED);
		itemModelGenerator.register(BotDObjects.CONTRACT, Models.GENERATED);
		itemModelGenerator.register(BotDObjects.PACKET, Models.GENERATED);
		itemModelGenerator.register(BotDObjects.BUTCHER_KNIFE, Models.HANDHELD);
		itemModelGenerator.register(BotDObjects.BLOODY_BUTCHER_KNIFE, Models.HANDHELD);
		itemModelGenerator.register(BotDObjects.CAGE, Models.GENERATED);
		itemModelGenerator.register(BotDObjects.HOOK, Models.GENERATED);
		itemModelGenerator.register(BotDObjects.METAL_HOOK, Models.GENERATED);
		itemModelGenerator.register(BotDObjects.FLESH, Models.GENERATED);
		itemModelGenerator.register(BotDObjects.COOKED_FLESH, Models.GENERATED);
		itemModelGenerator.register(BotDObjects.EMERALD_TABLET, Models.GENERATED);
		itemModelGenerator.register(BotDObjects.FAT, Models.GENERATED);
		itemModelGenerator.register(BotDObjects.SKIN, Models.GENERATED);
		itemModelGenerator.register(BotDObjects.BOTTLE_OF_BLOOD, Models.GENERATED);
		itemModelGenerator.register(BotDObjects.ROPE.asItem(), Models.GENERATED);
		itemModelGenerator.register(BotDObjects.QUICKSILVER, Models.GENERATED);
		itemModelGenerator.register(BotDObjects.SOUL_GEM, Models.GENERATED);
		itemModelGenerator.register(BotDObjects.CINNABAR, Models.GENERATED);
		itemModelGenerator.register(BotDObjects.CELLAR_KEY, Models.GENERATED);
		itemModelGenerator.register(BotDObjects.OLD_LETTER, Models.GENERATED);
		itemModelGenerator.register(BotDObjects.RETORT_FLASK, Models.GENERATED);
		itemModelGenerator.register(BotDObjects.POPPY_POD, Models.GENERATED);
		itemModelGenerator.register(BotDObjects.PAPER_AND_QUILL, Models.GENERATED);
		itemModelGenerator.register(BotDObjects.CARPENTER_TOOLS, Models.GENERATED);
	}
}
