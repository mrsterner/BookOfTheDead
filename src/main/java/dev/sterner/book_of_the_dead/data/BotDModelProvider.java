package dev.sterner.book_of_the_dead.data;

import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.model.BlockStateModelGenerator;
import net.minecraft.data.client.model.Models;
import net.minecraft.state.property.Properties;

public class BotDModelProvider extends FabricModelProvider {

	public BotDModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		blockStateModelGenerator.registerDoor(BotDObjects.REINFORCED_DOOR);
		blockStateModelGenerator.registerCrop(BotDObjects.POPPY_CROP, Properties.AGE_7, 0, 1, 2, 2, 3, 3, 3, 5);
		blockStateModelGenerator.registerNorthDefaultHorizontalRotation(BotDObjects.RETORT_FLASK_BLOCK);
	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		itemModelGenerator.register(BotDObjects.DEBUG_WAND, Models.SINGLE_LAYER_ITEM);

		itemModelGenerator.register(BotDObjects.QUARTZ_PEARL, Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.PHILOSOPHERS_STONE, Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.EYE, Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.SULFURIC_ACID, Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.HEART, Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.BOOK_OF_THE_DEAD, Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.PACKET, Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.CAGE, Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.HOOK, Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.METAL_HOOK, Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.FLESH, Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.COOKED_FLESH, Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.EMERALD_TABLET, Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.FAT, Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.SKIN, Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.BOTTLE_OF_BLOOD, Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.ROPE.asItem(), Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.CELLAR_KEY, Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.OLD_LETTER, Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.RETORT_FLASK, Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.POPPY_POD, Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.PAPER_AND_QUILL, Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.CARPENTER_TOOLS, Models.SINGLE_LAYER_ITEM);
		itemModelGenerator.register(BotDObjects.SOAP, Models.SINGLE_LAYER_ITEM);
	}
}
