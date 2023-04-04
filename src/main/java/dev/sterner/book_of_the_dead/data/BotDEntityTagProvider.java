package dev.sterner.book_of_the_dead.data;

import dev.sterner.book_of_the_dead.common.registry.BotDEntityTypes;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.HolderLookup;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.entity.EntityType.*;

public class BotDEntityTagProvider extends FabricTagProvider.EntityTypeTagProvider {

	public BotDEntityTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
		super(output, completableFuture);
	}

	@Override
	protected void configure(HolderLookup.Provider arg) {
		getOrCreateTagBuilder(Constants.Tags.BUTCHERABLE)
			.add(COW)
			.add(VILLAGER)
			.add(PILLAGER)
			.add(SHEEP)
			.add(PIG)
			.add(PIGLIN)
			.add(PIGLIN_BRUTE)
			.add(CHICKEN)
			.add(PLAYER)
			.add(BotDEntityTypes.PLAYER_CORPSE_ENTITY);

		getOrCreateTagBuilder(Constants.Tags.CAGEABLE_BLACKLIST)
			.add(PLAYER)
			.add(ENDER_DRAGON)
			.add(ELDER_GUARDIAN)
			.add(WITHER);

		getOrCreateTagBuilder(Constants.Tags.SOUL_WEAK)
			.add(ALLAY)
			.add(AXOLOTL)
			.add(BAT)
			.add(CAT)
			.add(CHICKEN)
			.add(COD)
			.add(COW)
			.add(FOX)
			.add(FROG)
			.add(GLOW_SQUID)
			.add(MOOSHROOM)
			.add(OCELOT)
			.add(PARROT)
			.add(PIG)
			.add(PUFFERFISH)
			.add(RABBIT)
			.add(SALMON)
			.add(SHEEP)
			.add(SQUID)
			.add(TADPOLE)
			.add(TROPICAL_FISH)
			.add(BEE)
			.add(CAVE_SPIDER)
			.add(SPIDER)
			.add(WOLF)
			.add(CREEPER)
			.add(ENDERMITE)
			.add(SHULKER)
			.add(SILVERFISH)
			.add(SLIME)
			.add(MAGMA_CUBE)
			.add(PHANTOM)
			.add(VEX);

		getOrCreateTagBuilder(Constants.Tags.SOUL_REGULAR)
			.add(DONKEY)
			.add(HORSE)
			.add(MULE)
			.add(SKELETON_HORSE)
			.add(SNOW_GOLEM)
			.add(STRIDER)
			.add(TURTLE)
			.add(DOLPHIN)
			.add(GOAT)
			.add(IRON_GOLEM)
			.add(LLAMA)
			.add(PANDA)
			.add(POLAR_BEAR)
			.add(TRADER_LLAMA)
			.add(ZOMBIFIED_PIGLIN)
			.add(BLAZE)
			.add(DROWNED)
			.add(ELDER_GUARDIAN)
			.add(RAVAGER)
			.add(SKELETON)
			.add(HUSK)
			.add(GHAST)
			.add(GUARDIAN)
			.add(HOGLIN)
			.add(STRAY)
			.add(WITHER_SKELETON)
			.add(ZOGLIN)
			.add(ZOMBIE)
			.add(CAMEL);

		getOrCreateTagBuilder(Constants.Tags.SOUL_STRONG)
			.add(VILLAGER)
			.add(WANDERING_TRADER)
			.add(ENDERMAN)
			.add(PIGLIN)
			.add(EVOKER)
			.add(PIGLIN_BRUTE)
			.add(VINDICATOR)
			.add(WARDEN)
			.add(WITCH)
			.add(ZOMBIE_VILLAGER)
			.add(SNIFFER)
			.add(ENDER_DRAGON)
			.add(WITHER)

		;
	}
}
