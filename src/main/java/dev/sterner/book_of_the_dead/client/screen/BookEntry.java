package dev.sterner.book_of_the_dead.client.screen;

import dev.sterner.book_of_the_dead.client.screen.page.BookPage;

import java.util.ArrayList;
import java.util.List;

public class BookEntry {
	public List<BookPage> pages = new ArrayList<>();

	protected BookEntry() {
	}

	public static BookEntry of(){
		return new BookEntry();
	}

	public BookEntry addPage(BookPage page) {
		if (page.isValid()) {
			pages.add(page);
		}
		return this;
	}
}
