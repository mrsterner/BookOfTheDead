package dev.sterner.book_of_the_dead.client.screen.tab;

import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class BotDTab extends DrawableHelper {
	public BookOfTheDeadScreen screen;
	public int width;
	public boolean isDragging;
	public Identifier background;
	private BotDTab nextTab;
	private BotDTab prevTab;

	public BotDTab(BookOfTheDeadScreen screen, Identifier background){
		this.screen = screen;
		this.background = background;
	}

	public BotDTab getNextTab() {
		return nextTab;
	}

	public void setNextTab(BotDTab nextTab) {
		this.nextTab = nextTab;
	}

	public BotDTab getPrevTab() {
		return prevTab;
	}

	public void setPrevTab(BotDTab prevTab) {
		this.prevTab = prevTab;
	}

	public void tick(){
	}

	public void init() {
	}

	public void render(MatrixStack matrices, int width, int mouseX, int mouseY, float delta) {
	}

	public boolean move(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		return false;
	}
}
