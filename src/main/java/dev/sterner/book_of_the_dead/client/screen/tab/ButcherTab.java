package dev.sterner.book_of_the_dead.client.screen.tab;

import dev.sterner.book_of_the_dead.client.screen.BookEntry;
import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.client.screen.page.HeadlineBookPage;

public class ButcherTab extends BookTab {
	public ButcherTab(BookOfTheDeadScreen screen) {
		super(screen, null);
	}

	@Override
	public void init() {
		ENTRIES.add(BookEntry.of()
			.addPage(HeadlineBookPage.of("butcher", "butcher.1"))
			.addPage(HeadlineBookPage.of("butcher.2"))
			.addPage(HeadlineBookPage.of("butcher.3"))
		);
	}
}
