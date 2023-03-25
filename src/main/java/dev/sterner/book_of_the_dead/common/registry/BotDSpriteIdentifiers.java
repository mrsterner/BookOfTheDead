package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import static net.minecraft.screen.PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;

@ClientOnly
public interface BotDSpriteIdentifiers {

	SpriteIdentifier BLOOD = new SpriteIdentifier(BLOCK_ATLAS_TEXTURE, Constants.id("block/blood_fluid"));
	SpriteIdentifier WATER = new SpriteIdentifier(BLOCK_ATLAS_TEXTURE, new Identifier("block/water_still"));
	SpriteIdentifier WATER_ALPHA = new SpriteIdentifier(BLOCK_ATLAS_TEXTURE, Constants.id("block/water_still"));
}
