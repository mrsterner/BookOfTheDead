package dev.sterner.book_of_the_dead.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.client.screen.tab.BookOfTheDeadTab;
import dev.sterner.book_of_the_dead.common.util.RenderUtils;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.ChatNarratorManager;
import net.minecraft.client.util.math.MatrixStack;

public class PrevPageWidget extends ClickableWidget {
	public float x;
	public float y;
	public BookOfTheDeadTab tab;
	public BookOfTheDeadScreen screen;

	public PrevPageWidget(int x, int y, BookOfTheDeadTab tab, BookOfTheDeadScreen screen) {
		super(x, y, 18, 9, ChatNarratorManager.NO_TITLE);
		this.x = x;
		this.y = y;
		this.tab = tab;
		this.screen = screen;
	}

	@Override
	public void drawWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		RenderSystem.setShaderTexture(0, BookOfTheDeadScreen.BOOK_TEXTURE);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, isHovered() ? this.alpha : 0.75F);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		// Render the screen texture

		matrices.push();
		RenderUtils.drawTexture(matrices, this.x, this.y, 272 + 18, 0, 18, 9, 512, 256);
		matrices.pop();
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		if (isValidClickButton(0)) {
			if(tab != null && tab.getPrevTab() != null){
				screen.knowledgeTab = tab.getPrevTab();
			}
		}
	}

	@Override
	protected void updateNarration(NarrationMessageBuilder builder) {

	}
}
