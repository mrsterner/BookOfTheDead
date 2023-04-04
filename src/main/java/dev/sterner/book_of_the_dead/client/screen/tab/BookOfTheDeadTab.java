package dev.sterner.book_of_the_dead.client.screen.tab;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class BookOfTheDeadTab extends DrawableHelper {
	public int width;
	public boolean isDragging;
	private BookOfTheDeadTab nextTab;
	private BookOfTheDeadTab prevTab;

	public BookOfTheDeadTab(){

	}

	public BookOfTheDeadTab getNextTab() {
		return nextTab;
	}

	public void setNextTab(BookOfTheDeadTab nextTab) {
		this.nextTab = nextTab;
	}

	public BookOfTheDeadTab getPrevTab() {
		return prevTab;
	}

	public void setPrevTab(BookOfTheDeadTab prevTab) {
		this.prevTab = prevTab;
	}

	public void init() {
	}

	public void render(MatrixStack matrices, int width, int mouseX, int mouseY, float delta) {
	}

	public boolean move(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		return false;
	}
}
