package dev.sterner.book_of_the_dead.client.screen.tab;

import dev.sterner.book_of_the_dead.client.screen.BookEntry;
import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.client.screen.page.HeadlineBookPage;
import dev.sterner.book_of_the_dead.client.screen.page.TextPage;

public class ToolsTab extends BookTab {
	public ToolsTab(BookOfTheDeadScreen screen) {
		super(screen, null);
	}

	@Override
	public void init() {
		ENTRIES.add(BookEntry.of()
			.addPage(HeadlineBookPage.of("tools", "tools.1"))
			.addPage(TextPage.of("tools.2"))
			.addPage(TextPage.of("tools.3"))
		);
	}
}
