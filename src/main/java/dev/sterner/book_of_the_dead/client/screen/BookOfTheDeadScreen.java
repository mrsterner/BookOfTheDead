package dev.sterner.book_of_the_dead.client.screen;

import dev.sterner.book_of_the_dead.client.screen.tab.BotDTab;
import dev.sterner.book_of_the_dead.client.screen.tab.MainTab;
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
import net.minecraft.util.math.MathHelper;


public class BookOfTheDeadScreen extends Screen {
	public static final Identifier BOOK_TEXTURE = Constants.id("textures/gui/book_of_the_dead.png");
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

		addDrawableChild(new NextPageWidget(x + 18 * 6 + 7, y, tab, this, -1));
		addDrawableChild(new PrevPageWidget(x - (18 * 6 + 7), y, tab, this, -1));
		addDrawableChild(new BackPageWidget(x, y - 5, tab, this, -1));
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
				int bgIndex = MathHelper.ceil(tab.grouping / 2d);
				if(bgIndex >= 0 && tab.background.size() > bgIndex){
					RenderUtils.renderTexture(matrices, tab.background.get(bgIndex), (this.width - 192) / 4 - 16, 32, 0, 0, 272, 182, 512, 256);
				}
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
}
