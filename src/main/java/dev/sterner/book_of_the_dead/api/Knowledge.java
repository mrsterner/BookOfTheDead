package dev.sterner.book_of_the_dead.api;

import net.minecraft.util.Identifier;

import java.util.List;

public class Knowledge {
	public final Identifier icon;
	public final String identifier;
	public final List<Knowledge> children;

	public Knowledge(String identifier, Identifier icon, List<Knowledge> children) {
		this.icon = icon;
		this.identifier = identifier;
		this.children = children;
	}
}
