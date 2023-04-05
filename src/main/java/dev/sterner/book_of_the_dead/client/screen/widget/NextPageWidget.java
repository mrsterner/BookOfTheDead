package dev.sterner.book_of_the_dead.client.screen.widget;

import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.client.screen.tab.BookTab;
import dev.sterner.book_of_the_dead.client.screen.tab.BotDTab;

public class NextPageWidget extends PageWidget {

	public NextPageWidget(int x, int y, BotDTab tab, BookOfTheDeadScreen screen) {
		super(x, y, 273, 0, 18, 9, tab, screen);
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		if (isValidClickButton(0)) {
			if (tab instanceof BookTab bookTab) {
				screen.clearChildren();
				bookTab.nextPage();
				screen.initialize();
			} else if (tab != null && tab.getNextTab() != null) {
				screen.clearChildren();
				screen.tab = tab.getNextTab();
				screen.initialize();
			}
		}
	}
}
