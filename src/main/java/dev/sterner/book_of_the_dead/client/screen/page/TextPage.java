package dev.sterner.book_of_the_dead.client.screen.page;

import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class TextPage extends BookPage {
	public final String translationKey;

	public TextPage(String translationKey) {
		super(Constants.id("textures/gui/book/pages/blank_page.png"));
		this.translationKey = translationKey;
	}

	public String translationKey() {
		return "book_of_the_dead.gui.book.page.text." + translationKey;
	}

	@Override
	public void renderLeft(MinecraftClient minecraft, MatrixStack poseStack, float xOffset, float yOffset, int mouseX, int mouseY, float partialTicks) {
		int guiLeft = guiLeft();
		int guiTop = guiTop();
		BookOfTheDeadScreen.renderWrappingText(poseStack, translationKey(), guiLeft, guiTop + 10, 109);
	}

	@Override
	public void renderRight(MinecraftClient minecraft, MatrixStack poseStack, float xOffset, float yOffset, int mouseX, int mouseY, float partialTicks) {
		int guiLeft = guiLeft();
		int guiTop = guiTop();
		BookOfTheDeadScreen.renderWrappingText(poseStack, translationKey(), guiLeft + 140, guiTop + 10, 109);
	}
}
