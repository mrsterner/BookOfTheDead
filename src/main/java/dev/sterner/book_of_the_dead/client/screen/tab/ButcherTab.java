package dev.sterner.book_of_the_dead.client.screen.tab;

import dev.sterner.book_of_the_dead.client.screen.BookEntry;
import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.client.screen.page.HeadlineBookPage;
import dev.sterner.book_of_the_dead.client.screen.page.TextPage;

public class ButcherTab extends BookTab {
	public ButcherTab(BookOfTheDeadScreen screen) {
		super(screen, null);
	}

	@Override
	public void initPages() {
		ENTRIES.clear();

		ENTRIES.add(new BookEntry("butcher", 0, 0)
			.addPage(new HeadlineBookPage("butcher", "butcher.1"))
			.addPage(new TextPage("text"))
			.addPage(new TextPage("test2"))

		);

		openEntry = ENTRIES.get(0);
	}
}
