package dev.sterner.book_of_the_dead.client.screen.widget;

import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.client.screen.tab.BookOfTheDeadTab;

public class PrevPageWidget extends PageWidget {

	public PrevPageWidget(int x, int y, BookOfTheDeadTab tab, BookOfTheDeadScreen screen) {
		super(x, y, 272 + 18, 0, 18, 9, tab, screen);
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		if (isValidClickButton(0)) {
			if(tab != null && tab.getPrevTab() != null){
				screen.clearChildren();
				screen.knowledgeTab = tab.getPrevTab();
				screen.initialize();
			}
		}
	}
}
