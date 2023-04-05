package dev.sterner.book_of_the_dead.client.screen.page;

import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class BookPage {
	public final Identifier BACKGROUND;

	public BookPage(Identifier background) {
		this.BACKGROUND = background;
	}

	public boolean isValid() {
		return true;
	}

	public void renderLeft(MinecraftClient minecraft, MatrixStack matrixStack, float xOffset, float yOffset, int mouseX, int mouseY, float partialTicks) {

	}

	public void renderRight(MinecraftClient minecraft, MatrixStack matrixStack, float xOffset, float yOffset, int mouseX, int mouseY, float partialTicks) {

	}

	public int guiLeft() {
		return (BookOfTheDeadScreen.screen.width - 192) / 4 - 3;
	}

	public int guiTop() {
		return 18 * 2;
	}
}
