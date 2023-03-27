package dev.sterner.book_of_the_dead.common.util;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class TextUtils {
	/**
	 * stylized two strings with different colors on the same line of Text
	 *
	 * @param first      string to be stylized
	 * @param second     string to be stylized
	 * @param nameColor  color of first string
	 * @param valueColor color fo second string
	 * @return Text styled to "first: second"
	 */
	public static Text formattedFromTwoStrings(String first, String second, int nameColor, int valueColor) {
		final MutableText name = styledText(first, nameColor);
		final MutableText value = styledText(second, valueColor);

		return name.append(Text.of(": ")).append(value);
	}

	public static MutableText styledText(Object string, int color) {
		return Text.literal(string.toString()).setStyle(Style.EMPTY.withColor(color));
	}

	/**
	 * turns "a_fun, string.minecraft:stick" to "A Fun, String Minecraft:stick"
	 *
	 * @param string string to be formatted
	 * @return formatted string
	 */
	public static String capitalizeString(String string) {
		if (string == null || string.trim().isEmpty()) {
			return "";
		}

		StringBuilder sb = new StringBuilder(string.toLowerCase());
		boolean capitalizeNext = true;

		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			if (Character.isWhitespace(c) || c == '.' || c == '\'' || c == '_') {
				capitalizeNext = true;
			} else if (capitalizeNext && Character.isLetter(c)) {
				sb.setCharAt(i, Character.toUpperCase(c));
				capitalizeNext = false;
			}
		}

		return sb.toString();
	}
}
