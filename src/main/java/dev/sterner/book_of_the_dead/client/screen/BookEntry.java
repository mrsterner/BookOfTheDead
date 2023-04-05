package dev.sterner.book_of_the_dead.client.screen;

import dev.sterner.book_of_the_dead.client.screen.page.BookPage;

import java.util.ArrayList;
import java.util.List;

public class BookEntry {
	public final String identifier;
	public final int xOffset;
	public final int yOffset;
	public List<BookPage> pages = new ArrayList<>();

	public BookEntry(String identifier, int xOffset, int yOffset) {
		this.identifier = identifier;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public String translationKey() {
		return "book_of_the_dead.gui.book.entry." + identifier;
	}

	public String descriptionTranslationKey() {
		return "book_of_the_dead.gui.book.entry." + identifier + ".description";
	}

	public BookEntry addPage(BookPage page) {
		if (page.isValid()) {
			pages.add(page);
		}
		return this;
	}
}
