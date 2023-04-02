package dev.sterner.book_of_the_dead.client.screen.book;

import net.minecraft.util.Identifier;

public class Knowledge {
	public final Identifier icon;
	public final String identifier;

	public Knowledge(String identifier, Identifier icon) {
		this.icon = icon;
		this.identifier = identifier;
	}
}
