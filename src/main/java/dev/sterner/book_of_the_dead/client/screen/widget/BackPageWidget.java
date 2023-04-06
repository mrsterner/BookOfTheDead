package dev.sterner.book_of_the_dead.client.screen.widget;

import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.client.screen.tab.BotDTab;
import dev.sterner.book_of_the_dead.client.screen.tab.MainTab;

public class BackPageWidget extends PulseWidget {

	public BackPageWidget(int x, int y, BotDTab tab, BookOfTheDeadScreen screen) {
		super(x, y, 273, 10, 18, 12, tab, screen);
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		if (isValidClickButton(0)) {
			screen.clearChildren();
			screen.tab = new MainTab(screen);
			screen.initialize();
		}
	}
}
