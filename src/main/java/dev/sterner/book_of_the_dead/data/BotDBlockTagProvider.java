package dev.sterner.book_of_the_dead.data;

import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.HolderLookup;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class BotDBlockTagProvider extends FabricTagProvider.BlockTagProvider{
	public BotDBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(HolderLookup.Provider arg) {
		getOrCreateTagBuilder(BlockTags.CLIMBABLE).add(BotDObjects.ROPE);
		getOrCreateTagBuilder(Constants.Tags.EMITS_HEAT)
				.add(Blocks.FIRE)
				.add(Blocks.MAGMA_BLOCK)
				.add(Blocks.LAVA)
				.add(Blocks.LAVA_CAULDRON)
				.add(Blocks.TORCH)
				.add(Blocks.CAMPFIRE);

		getOrCreateTagBuilder(BlockTags.INFINIBURN_OVERWORLD)
				.add(BotDObjects.SULFUR);

		getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(BotDObjects.SULFUR);
		getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE).add(BotDObjects.SULFUR);
	}
}
