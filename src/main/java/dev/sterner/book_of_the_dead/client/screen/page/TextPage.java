package dev.sterner.book_of_the_dead.client.screen.page;

import dev.sterner.book_of_the_dead.common.util.Constants;
import dev.sterner.book_of_the_dead.common.util.TextUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class TextPage extends BookPage {
	public final String translationKey;

	protected TextPage(String translationKey) {
		super(Constants.id("textures/gui/book/pages/blank_page.png"));
		this.translationKey = translationKey;
	}

	public static TextPage of(String translationKey) {
		return new TextPage(translationKey);
	}

	public static TextPage of() {
		return new TextPage("empty");
	}

	public String translationKey() {
		return "book_of_the_dead.gui.book.page.text." + translationKey;
	}

	@Override
	public void renderLeft(MinecraftClient minecraft, MatrixStack poseStack, float xOffset, float yOffset, int mouseX, int mouseY, float partialTicks) {
		int guiLeft = guiLeft();
		int guiTop = guiTop();
		TextUtils.renderWrappingText(poseStack, translationKey(), guiLeft, guiTop + 10, 109);
	}

	@Override
	public void renderRight(MinecraftClient minecraft, MatrixStack poseStack, float xOffset, float yOffset, int mouseX, int mouseY, float partialTicks) {
		int guiLeft = guiLeft();
		int guiTop = guiTop();
		TextUtils.renderWrappingText(poseStack, translationKey(), guiLeft + 140, guiTop + 10, 109);
	}
}
