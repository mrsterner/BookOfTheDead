package dev.sterner.legemeton.data;

import dev.sterner.legemeton.common.registry.LegemetonObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Blocks;

public class LegemetonBlockLootTableProvider extends FabricBlockLootTableProvider {
	protected LegemetonBlockLootTableProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	protected void generateBlockLootTables() {
		addDrop(LegemetonObjects.HOOK_BLOCK, () -> LegemetonObjects.HOOK);
		addDrop(LegemetonObjects.ROPE);
		addDrop(Blocks.REDSTONE_BLOCK, () -> LegemetonObjects.CINNABAR);

	}
}
