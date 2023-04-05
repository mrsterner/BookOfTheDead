package dev.sterner.book_of_the_dead.client.screen.widget;

import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.client.screen.tab.BotDTab;

public class NavigationWidget extends PageWidget {

	public BotDTab targetTab;

	public NavigationWidget(BotDTab targetTab, int x, int y, int u, int v, BotDTab tab, BookOfTheDeadScreen screen) {
		super((tab.width - 192) / 4 + x, y, u, v, 24, 24, tab, screen);
		this.targetTab = targetTab;
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		if (isValidClickButton(0)) {
			screen.clearChildren();
			screen.tab = targetTab;
			screen.initialize();
		}
	}
}
