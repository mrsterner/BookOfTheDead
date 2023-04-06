package dev.sterner.book_of_the_dead.client.screen;

import dev.sterner.book_of_the_dead.client.screen.tab.BotDTab;
import dev.sterner.book_of_the_dead.client.screen.tab.MainTab;
import dev.sterner.book_of_the_dead.client.screen.widget.BackPageWidget;
import dev.sterner.book_of_the_dead.client.screen.widget.NextPageWidget;
import dev.sterner.book_of_the_dead.client.screen.widget.PrevPageWidget;
import dev.sterner.book_of_the_dead.common.util.Constants;
import dev.sterner.book_of_the_dead.common.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ChatNarratorManager;
import net.minecraft.client.util.ColorUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;


public class BookOfTheDeadScreen extends Screen {
	public static final Identifier BOOK_TEXTURE = Constants.id("textures/gui/background.png");
	public PlayerEntity player;
	public static BookOfTheDeadScreen screen;
	public BotDTab tab;

	public BookOfTheDeadScreen(PlayerEntity player) {
		super(ChatNarratorManager.NO_TITLE);
		this.player = player;
		this.tab = new MainTab(this);
	}

	public static void openScreen(PlayerEntity player) {
		MinecraftClient.getInstance().setScreen(getInstance(player));
	}

	@Override
	protected void init() {
		initialize();
	}

	public void initialize() {
		if (tab != null) {
			tab.width = this.width;
			tab.preInit();
			tab.init();
			tab.postInit();
		}

		int x = (width - 192) / 4 + 9 * 12 + 3;
		int y = 32 * 6 + 1;

		if (!(tab instanceof MainTab)) {
			addDrawableChild(new NextPageWidget(x + 18 * 6 + 7, y, tab, this));
			addDrawableChild(new PrevPageWidget(x - (18 * 6 + 7), y, tab, this));
			addDrawableChild(new BackPageWidget(x, y - 5, tab, this));
		}
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		matrices.push();
		this.renderBackground(matrices);
		this.setFocused(false);
		RenderUtils.renderTexture(matrices, BOOK_TEXTURE, (this.width - 192) / 4 - 16, 32, 0, 0, 272, 182, 512, 256);
		matrices.pop();
		if (tab != null) {
			if (tab.background != null) {
				RenderUtils.renderTexture(matrices, tab.background, (this.width - 192) / 4 - 16, 32, 0, 0, 272, 182, 512, 256);
			}
			tab.render(matrices, this.width, mouseX, mouseY, delta);
		}


		super.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (button == 0) {
			if (tab != null) {
				return tab.move(mouseX, mouseY, button, deltaX, deltaY);
			}
		}
		return false;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (tab != null) {
			tab.isDragging = false;
		}
		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}


	@Override
	public void tick() {
		if (tab != null) {
			tab.tick();
		}
		super.tick();
	}


	public static BookOfTheDeadScreen getInstance(PlayerEntity player) {
		if (screen == null) {
			screen = new BookOfTheDeadScreen(player);
		}
		return screen;
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
