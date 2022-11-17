package dev.sterner.legemeton.api.enums;

import net.minecraft.util.StringIdentifiable;

public enum HorizontalDoubleBlockHalf implements StringIdentifiable {
	LEFT,
	RIGHT;

	public String toString() {
		return this.asString();
	}

	@Override
	public String asString() {
		return this == LEFT ? "left" : "right";
	}
}
