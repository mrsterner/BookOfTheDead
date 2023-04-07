package dev.sterner.book_of_the_dead.client.screen.tab;

import dev.sterner.book_of_the_dead.client.screen.BookEntry;
import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.client.screen.page.HeadlineBookPage;

public class NecroTab extends BookTab {
	public NecroTab(BookOfTheDeadScreen screen) {
		super(screen, null);
	}

	@Override
	public void init() {
		ENTRIES.add(BookEntry.of()
			.addPage(HeadlineBookPage.of("necro", "necro.1"))
			.addPage(HeadlineBookPage.of("necro.2"))
			.addPage(HeadlineBookPage.of("necro.3"))
		);
	}
}
