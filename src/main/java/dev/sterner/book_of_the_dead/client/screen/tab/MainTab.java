package dev.sterner.book_of_the_dead.client.screen.tab;

import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.client.screen.widget.NavigationWidget;
import dev.sterner.book_of_the_dead.common.util.Constants;

import java.util.ArrayList;
import java.util.List;


public class MainTab extends BotDTab {

	public List<NavigationWidget> widgets = new ArrayList<>();

	public MainTab(BookOfTheDeadScreen screen) {
		super(screen, Constants.id("textures/gui/background/main_tab.png"));
	}

	@Override
	public void init() {
		super.init();

		widgets.clear();
		widgets.add(new NavigationWidget(new ButcherTab(screen), 18 * 7 + 10, 70, 273, 23, this, screen));
		widgets.add(new NavigationWidget(new KnowledgeTab(screen), 18 * 12 + 1, 70 + 24, 273, 23 + 25, this, screen));

		for (NavigationWidget navigationWidget : widgets) {
			screen.addDrawableChild(navigationWidget);
		}
	}

	@Override
	public void tick() {
		for (NavigationWidget navigationWidget : widgets) {
			navigationWidget.tick();
		}
	}
}
