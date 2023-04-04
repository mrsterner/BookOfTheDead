package dev.sterner.book_of_the_dead.client.screen.widget;

import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.client.screen.tab.BookOfTheDeadTab;

public class NextPageWidget extends PageWidget {

	public NextPageWidget(int x, int y, BookOfTheDeadTab tab, BookOfTheDeadScreen screen) {
		super(x, y, 272, 0, 18, 9, tab, screen);
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		if (isValidClickButton(0)) {
			if(tab != null && tab.getNextTab() != null){
				screen.clearChildren();
				screen.knowledgeTab = tab.getNextTab();
				screen.initialize();
			}
		}
	}
}
