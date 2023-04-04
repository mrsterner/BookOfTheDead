package dev.sterner.book_of_the_dead.client.screen.widget;

import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.client.screen.tab.BookOfTheDeadTab;

public class BackPageWidget extends PageWidget {

	public BackPageWidget(int x, int y, BookOfTheDeadTab tab, BookOfTheDeadScreen screen) {
		super(x, y, 272, 9, 18, 12, tab, screen);
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		if (isValidClickButton(0)) {
			screen.clearChildren();
			screen.knowledgeTab = null;
			screen.initialize();
		}
	}
}
