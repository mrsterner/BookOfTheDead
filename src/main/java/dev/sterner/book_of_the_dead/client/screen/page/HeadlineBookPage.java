package dev.sterner.book_of_the_dead.client.screen.page;

import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class HeadlineBookPage extends BookPage{
	private final String headlineTranslationKey;
	private final String translationKey;

	public HeadlineBookPage(String headlineTranslationKey, String translationKey) {
		super(Constants.id("textures/gui/book/pages/headline_page.png"));
		this.headlineTranslationKey = headlineTranslationKey;
		this.translationKey = translationKey;
	}

	public String headlineTranslationKey() {
		return "book_of_the_dead.gui.book.page.headline." + headlineTranslationKey;
	}

	public String translationKey() {
		return "book_of_the_dead.gui.book.page.text." + translationKey;
	}

	@Override
	public void renderLeft(MinecraftClient minecraft, MatrixStack matrixStack, float xOffset, float yOffset, int mouseX, int mouseY, float partialTicks) {
		int guiLeft = guiLeft();
		int guiTop = guiTop();
		Text component = Text.translatable(headlineTranslationKey());
		BookOfTheDeadScreen.renderText(matrixStack, component, guiLeft + 75 - 18 - minecraft.textRenderer.getWidth(component.getString()) / 2, guiTop + 10);
		BookOfTheDeadScreen.renderWrappingText(matrixStack, translationKey(), guiLeft, guiTop + 31, 109);
	}

	@Override
	public void renderRight(MinecraftClient minecraft, MatrixStack matrixStack, float xOffset, float yOffset, int mouseX, int mouseY, float partialTicks) {
		int guiLeft = guiLeft();
		int guiTop = guiTop();
		Text component = Text.translatable(headlineTranslationKey());
		BookOfTheDeadScreen.renderText(matrixStack, component, guiLeft + 218 - minecraft.textRenderer.getWidth(component.getString()) / 2, guiTop + 10);
		BookOfTheDeadScreen.renderWrappingText(matrixStack, translationKey(), guiLeft + 140, guiTop + 31, 109);
	}
}
