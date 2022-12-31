package dev.sterner.book_of_the_dead.data;

import dev.sterner.book_of_the_dead.common.util.Constants;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;

public class BotDEntityTagProvider extends FabricTagProvider.EntityTypeTagProvider {
	public BotDEntityTagProvider(FabricDataGenerator dataGenerator) {
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
				.add(EntityType.CHICKEN)
				.add(EntityType.PLAYER);
	}

}
