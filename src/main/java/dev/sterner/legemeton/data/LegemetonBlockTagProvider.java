package dev.sterner.legemeton.data;

import dev.sterner.legemeton.common.registry.LegemetonObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.tag.BlockTags;

public class LegemetonBlockTagProvider extends FabricTagProvider.BlockTagProvider{
	public LegemetonBlockTagProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	protected void generateTags() {
		getOrCreateTagBuilder(BlockTags.CLIMBABLE).add(LegemetonObjects.ROPE);
	}
}
