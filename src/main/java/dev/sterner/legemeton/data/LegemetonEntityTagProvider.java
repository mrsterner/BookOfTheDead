package dev.sterner.legemeton.data;

import dev.sterner.legemeton.common.util.Constants;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;

public class LegemetonEntityTagProvider extends FabricTagProvider.EntityTypeTagProvider {
	public LegemetonEntityTagProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	protected void generateTags() {
		getOrCreateTagBuilder(Constants.Tags.BUTCHERABLE)
				.add(EntityType.COW)
				.add(EntityType.VILLAGER)
				.add(EntityType.PILLAGER)
				.add(EntityType.SHEEP)
				.add(EntityType.PIG)
				.add(EntityType.PIGLIN)
				.add(EntityType.PIGLIN_BRUTE)
				.add(EntityType.CHICKEN);
	}

}
