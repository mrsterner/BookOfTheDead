package dev.sterner.book_of_the_dead.client.screen.tab;

import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.client.screen.widget.BotDWidget;
import dev.sterner.book_of_the_dead.client.screen.widget.NavigationWidget;
import dev.sterner.book_of_the_dead.common.util.RenderUtils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class BotDTab extends DrawableHelper {
	public BookOfTheDeadScreen screen;
	public int width;
	public boolean isDragging;
	public List<Identifier> background;
	public List<BotDWidget> widgets = new ArrayList<>();
	public int grouping;

	public BotDTab(BookOfTheDeadScreen screen, List<Identifier> background) {
		this.screen = screen;
		this.background = background;
	}

	public void tick() {
		for (BotDWidget botDWidget : widgets) {
			botDWidget.tick();
		}
	}

	public void preInit(){
	}

	public void init() {
	}

	public void postInit(){
	}

	public void render(MatrixStack matrices, int width, int mouseX, int mouseY, float delta) {

	}

	public boolean move(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		return false;
	}
}
