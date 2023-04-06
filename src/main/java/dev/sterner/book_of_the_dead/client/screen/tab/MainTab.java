package dev.sterner.book_of_the_dead.client.screen.tab;

import dev.sterner.book_of_the_dead.client.screen.BookEntry;
import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.client.screen.page.HeadlineBookPage;
import dev.sterner.book_of_the_dead.client.screen.widget.NavigationWidget;
import dev.sterner.book_of_the_dead.common.util.Constants;

import java.util.List;


public class MainTab extends BookTab {

	public MainTab(BookOfTheDeadScreen screen) {
		super(screen, List.of(Constants.id("textures/gui/background/main_tab.png")));
	}

	@Override
	public void init() {
		ENTRIES.add(BookEntry.of()
			.addPage(HeadlineBookPage.of("main", "main.1"))
			.addPage(HeadlineBookPage.of("glossary"))
			.addPage(HeadlineBookPage.of("glossary"))
		);

		widgets.add(new NavigationWidget(new ToolsTab(screen), 18 * 7 + 10, 70, 273, 23, this, screen, 0));
		widgets.add(new NavigationWidget(new KnowledgeTab(screen), 18 * 12 + 1, 70 + 25, 273, 23 + 25, this, screen, 0));
		widgets.add(new NavigationWidget(new ButcherTab(screen), 18 * 7 + 10, 70 + 25 * 2, 273, 23 + 25 + 25, this, screen, 0));
		widgets.add(new NavigationWidget(new NecroTab(screen), 18 * 12 + 1, 70 + 25 * 3, 273, 23 + 25 + 25 + 25, this, screen, 0));
	}
}
