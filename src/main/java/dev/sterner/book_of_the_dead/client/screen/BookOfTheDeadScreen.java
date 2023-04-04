package dev.sterner.book_of_the_dead.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.sterner.book_of_the_dead.client.screen.tab.BookOfTheDeadTab;
import dev.sterner.book_of_the_dead.client.screen.tab.KnowledgeTab;
import dev.sterner.book_of_the_dead.client.screen.widget.BackPageWidget;
import dev.sterner.book_of_the_dead.client.screen.widget.NextPageWidget;
import dev.sterner.book_of_the_dead.client.screen.widget.PrevPageWidget;
import dev.sterner.book_of_the_dead.common.util.Constants;
import dev.sterner.book_of_the_dead.common.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ChatNarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;


public class BookOfTheDeadScreen extends Screen {
	public static final Identifier BOOK_TEXTURE = Constants.id("textures/gui/background.png");
	public PlayerEntity player;
	public static BookOfTheDeadScreen screen;
	public BookOfTheDeadTab knowledgeTab;


	public BookOfTheDeadScreen(PlayerEntity player) {
		super(ChatNarratorManager.NO_TITLE);
		this.player = player;
		this.knowledgeTab = new KnowledgeTab(this);
	}


	public static void openScreen(PlayerEntity player) {
		MinecraftClient.getInstance().setScreen(getInstance(player));
	}

	@Override
	protected void init() {
		initialize();
	}

	public void initialize(){
		if(knowledgeTab != null) {
			knowledgeTab.width = this.width;
			knowledgeTab.init();
		}

		int x = (width - 192) / 4 + 9 * 12 + 2;
		int y = 32 * 6 + 8;

		addDrawableChild(new NextPageWidget(x + 18 * 6 + 9, y, knowledgeTab, this));
		addDrawableChild(new PrevPageWidget(x - (18 * 6 + 9), y, knowledgeTab, this));
		addDrawableChild(new BackPageWidget(x, y, knowledgeTab, this));
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		matrices.push();
		this.renderBackground(matrices);
		this.setFocused(false);
		RenderUtils.renderTexture(matrices, BOOK_TEXTURE, (this.width - 192) / 4 - 16, 32, 0, 0, 272, 182, 512, 256);
		matrices.pop();
		if(knowledgeTab != null){
			knowledgeTab.render(matrices, this.width, mouseX, mouseY, delta);
		}


		super.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (button == 0) {
			if(knowledgeTab != null) {
				return knowledgeTab.move(mouseX, mouseY, button, deltaX, deltaY);
			}
		}
		return false;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if(knowledgeTab != null) {
			knowledgeTab.isDragging = false;
		}
		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	public static BookOfTheDeadScreen getInstance(PlayerEntity player) {
		if (screen == null) {
			screen = new BookOfTheDeadScreen(player);
		}
		return screen;
	}
}
