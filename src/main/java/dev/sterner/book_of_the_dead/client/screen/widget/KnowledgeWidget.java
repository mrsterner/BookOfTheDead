package dev.sterner.book_of_the_dead.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.client.screen.book.Knowledge;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.ChatNarratorManager;
import net.minecraft.client.util.math.MatrixStack;

public class KnowledgeWidget extends ClickableWidget {
	public Knowledge knowledge;
	public BookOfTheDeadScreen screen;
	public int x;
	public int y;

	public KnowledgeWidget(int x, int y, BookOfTheDeadScreen screen, Knowledge knowledge) {
		super(x, y, 17, 17, ChatNarratorManager.NO_TITLE);
		this.screen = screen;
		this.knowledge = knowledge;
		this.x = x;
		this.y = y;
	}

	@Override
	public void drawWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		RenderSystem.setShaderTexture(0, knowledge.icon);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, isHovered() && isValidClickButton(0) ? 0.75F : !isValidClickButton(0) ? 0.25F : this.alpha);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();

		drawTexture(matrices, this.x, this.y, 60 , 182, this.width, this.height, 512, 256);

	}

	@Override
	protected boolean isValidClickButton(int button) {
		return super.isValidClickButton(button);
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		if (isValidClickButton(0)) {

		}
	}

	@Override
	protected void updateNarration(NarrationMessageBuilder builder) {

	}
}
