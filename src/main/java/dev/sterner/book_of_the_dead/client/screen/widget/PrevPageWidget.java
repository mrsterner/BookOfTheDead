package dev.sterner.book_of_the_dead.client.screen.widget;

import dev.sterner.book_of_the_dead.api.interfaces.InactiveButton;
import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.client.screen.tab.BookTab;
import dev.sterner.book_of_the_dead.client.screen.tab.BotDTab;

public class PrevPageWidget extends PulseWidget implements InactiveButton {

	public PrevPageWidget(int x, int y, BotDTab tab, BookOfTheDeadScreen screen, int page) {
		super(x, y, 273 + 19, 0, 18, 9, tab, screen, page);
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		if (isValidClickButton(0)) {
			if (tab instanceof BookTab bookTab) {
				screen.clearChildren();
				bookTab.previousPage();
				screen.initialize();
			}
		}
	}

	@Override
	public boolean isInactive() {
		return !(tab.grouping > 0);
	}
}
