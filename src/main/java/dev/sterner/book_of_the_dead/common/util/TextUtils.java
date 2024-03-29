package dev.sterner.book_of_the_dead.common.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.ColorUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

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

	public static void renderWrappingText(MatrixStack mStack, String text, int x, int y, int w) {
		TextRenderer font = MinecraftClient.getInstance().textRenderer;
		text = Text.translatable(text).getString() + "\n";
		List<String> lines = new ArrayList<>();

		boolean italic = false;
		boolean bold = false;
		boolean strikethrough = false;
		boolean underline = false;
		boolean obfuscated = false;

		StringBuilder line = new StringBuilder();
		StringBuilder word = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			char chr = text.charAt(i);
			if (chr == ' ' || chr == '\n') {
				if (word.length() > 0) {
					if (font.getWidth(line.toString()) + font.getWidth(word.toString()) > w) {
						line = newLine(lines, italic, bold, strikethrough, underline, obfuscated, line);
					}
					line.append(word).append(' ');
					word = new StringBuilder();
				}

				String noFormatting = Formatting.strip(line.toString());

				if (chr == '\n' && !(noFormatting == null || noFormatting.isEmpty())) {
					line = newLine(lines, italic, bold, strikethrough, underline, obfuscated, line);
				}
			} else if (chr == '$') {
				if (i != text.length() - 1) {
					char peek = text.charAt(i + 1);
					switch (peek) {
						case 'i' -> {
							word.append(Formatting.ITALIC);
							italic = true;
							i++;
						}
						case 'b' -> {
							word.append(Formatting.BOLD);
							bold = true;
							i++;
						}
						case 's' -> {
							word.append(Formatting.STRIKETHROUGH);
							strikethrough = true;
							i++;
						}
						case 'u' -> {
							word.append(Formatting.UNDERLINE);
							underline = true;
							i++;
						}
						case 'k' -> {
							word.append(Formatting.OBFUSCATED);
							obfuscated = true;
							i++;
						}
						default -> word.append(chr);
					}
				} else {
					word.append(chr);
				}
			} else if (chr == '/') {
				if (i != text.length() - 1) {
					char peek = text.charAt(i + 1);
					if (peek == '$') {
						italic = bold = strikethrough = underline = obfuscated = false;
						word.append(Formatting.RESET);
						i++;
					} else
						word.append(chr);
				} else
					word.append(chr);
			} else {
				word.append(chr);
			}
		}

		for (int i = 0; i < lines.size(); i++) {
			String currentLine = lines.get(i);
			renderRawText(mStack, currentLine, x, y + i * (font.fontHeight + 1));
		}
	}

	private static StringBuilder newLine(List<String> lines, boolean italic, boolean bold, boolean strikethrough, boolean underline, boolean obfuscated, StringBuilder line) {
		lines.add(line.toString());
		line = new StringBuilder();
		if (italic) line.append(Formatting.ITALIC);
		if (bold) line.append(Formatting.BOLD);
		if (strikethrough) line.append(Formatting.STRIKETHROUGH);
		if (underline) line.append(Formatting.UNDERLINE);
		if (obfuscated) line.append(Formatting.OBFUSCATED);
		return line;
	}

	public static void renderText(MatrixStack stack, Text component, int x, int y) {
		String text = component.getString();
		renderRawText(stack, text, x, y);
	}

	private static void renderRawText(MatrixStack stack, String text, int x, int y) {
		var font = MinecraftClient.getInstance().textRenderer;

		font.draw(stack, text, x - 1, y, ColorUtil.ARGB32.getArgb(96, 236, 227, 214));
		font.draw(stack, text, x + 1, y, ColorUtil.ARGB32.getArgb(128, 165, 149, 142));
		font.draw(stack, text, x, y - 1, ColorUtil.ARGB32.getArgb(128, 208, 197, 183));
		font.draw(stack, text, x, y + 1, ColorUtil.ARGB32.getArgb(96, 105, 109, 102));

		font.draw(stack, text, x, y, ColorUtil.ARGB32.getArgb(255, 50, 50, 50));
	}
}
