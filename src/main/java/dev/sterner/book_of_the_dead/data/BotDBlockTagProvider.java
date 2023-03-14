package dev.sterner.book_of_the_dead.data;

import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;

public class BotDBlockTagProvider extends FabricTagProvider.BlockTagProvider{
	public BotDBlockTagProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	protected void generateTags() {
		getOrCreateTagBuilder(BlockTags.CLIMBABLE).add(BotDObjects.ROPE);
		getOrCreateTagBuilder(Constants.Tags.EMITS_HEAT)
				.add(Blocks.FIRE)
				.add(Blocks.MAGMA_BLOCK)
				.add(Blocks.LAVA)
				.add(Blocks.LAVA_CAULDRON)
				.add(Blocks.TORCH)
				.add(Blocks.CAMPFIRE);
	}
}
