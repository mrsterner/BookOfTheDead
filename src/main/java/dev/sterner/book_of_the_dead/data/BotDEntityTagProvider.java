package dev.sterner.book_of_the_dead.data;

import dev.sterner.book_of_the_dead.common.registry.BotDEntityTypes;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class BotDEntityTagProvider extends FabricTagProvider.EntityTypeTagProvider {

	public BotDEntityTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
		super(output, completableFuture);
	}

	@Override
	protected void configure(HolderLookup.Provider arg) {
		getOrCreateTagBuilder(Constants.Tags.BUTCHERABLE)
				.add(EntityType.COW)
				.add(EntityType.VILLAGER)
				.add(EntityType.PILLAGER)
				.add(EntityType.SHEEP)
				.add(EntityType.PIG)
				.add(EntityType.PIGLIN)
				.add(EntityType.PIGLIN_BRUTE)
				.add(EntityType.CHICKEN)
				.add(EntityType.PLAYER)
				.add(BotDEntityTypes.PLAYER_CORPSE_ENTITY);

		getOrCreateTagBuilder(Constants.Tags.CAGEABLE_BLACKLIST)
				.add(EntityType.PLAYER)
				.add(EntityType.ENDER_DRAGON)
				.add(EntityType.ELDER_GUARDIAN)
				.add(EntityType.WITHER);
	}
}
