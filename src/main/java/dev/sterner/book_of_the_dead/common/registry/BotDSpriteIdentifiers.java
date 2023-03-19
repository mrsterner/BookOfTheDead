package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.*;

import static net.minecraft.screen.PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;

@ClientOnly
public class BotDSpriteIdentifiers {

	public static final SpriteIdentifier BLOOD = new SpriteIdentifier(BLOCK_ATLAS_TEXTURE, Constants.id("block/blood_fluid"));
	public static final SpriteIdentifier WATER = new SpriteIdentifier(BLOCK_ATLAS_TEXTURE, new Identifier("block/water_still"));

	public static final BotDSpriteIdentifiers INSTANCE = new BotDSpriteIdentifiers();
	private final List<SpriteIdentifier> identifiers = new ArrayList<>();

	public SpriteIdentifier addIdentifier(SpriteIdentifier sprite) {
		this.identifiers.add(sprite);
		return sprite;
	}

	public Collection<SpriteIdentifier> getIdentifiers() {
		return Collections.unmodifiableList(identifiers);
	}
}
