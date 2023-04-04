package dev.sterner.book_of_the_dead.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.client.screen.tab.BookOfTheDeadTab;
import dev.sterner.book_of_the_dead.common.util.RenderUtils;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.ChatNarratorManager;
import net.minecraft.client.util.math.MatrixStack;

public class PageWidget extends ClickableWidget {
	public int h;
	public int w;
	public int u;
	public int v;
	public float x;
	public float y;
	public BookOfTheDeadScreen screen;
	public BookOfTheDeadTab tab;

	public PageWidget(int x, int y, int u, int v, int w, int h, BookOfTheDeadTab tab, BookOfTheDeadScreen screen) {
		super(x, y, w, h, ChatNarratorManager.NO_TITLE);
		this.x = x;
		this.y = y;
		this.h = h;
		this.w = w;
		this.u = u;
		this.v = v;
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
		RenderUtils.drawTexture(matrices, x, y, u, v, w, h, 512, 256);
		matrices.pop();
	}

	@Override
	protected void updateNarration(NarrationMessageBuilder builder) {

	}
}
