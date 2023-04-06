package dev.sterner.book_of_the_dead.client.screen.tab;

import dev.sterner.book_of_the_dead.client.screen.BookEntry;
import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.client.screen.page.BookPage;
import dev.sterner.book_of_the_dead.client.screen.widget.BotDWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public abstract class BookTab extends BotDTab {
	public BookEntry openEntry;

	public static List<BookEntry> ENTRIES = new ArrayList<>();

	protected BookTab(BookOfTheDeadScreen screen, List<Identifier> background) {
		super(screen, background);
	}

	@Override
	public void preInit() {
		ENTRIES.clear();
		widgets.clear();
	}

	@Override
	public void postInit() {
		for (BotDWidget widget : widgets) {
			System.out.println(grouping + " : " + widget.page);
			if(grouping == widget.page){
				screen.addDrawableChild(widget);
			}

		}
		if(ENTRIES.size() > 0){
			openEntry = ENTRIES.get(0);
		}
	}

	@Override
	public void render(MatrixStack matrices, int width, int mouseX, int mouseY, float delta) {
		float x = (float) (width - 192) / 4 + 9 * 5 - 4;
		float y = 18 * 6 + 10;

		if (openEntry != null) {
			if (!openEntry.pages.isEmpty()) {
				int openPages = grouping * 2;
				for (int i = openPages; i < openPages + 2; i++) {
					if (i < openEntry.pages.size()) {
						BookPage page = openEntry.pages.get(i);
						if (i % 2 == 0) {
							page.renderLeft(screen.getClient(), matrices, x, y, mouseX, mouseY, delta);
						} else {
							page.renderRight(screen.getClient(), matrices, x + 90, y, mouseX, mouseY, delta);
						}
					}
				}
			}
		}

	}

	public void playSound() {
		PlayerEntity playerEntity = MinecraftClient.getInstance().player;
		if (playerEntity != null) {
			playerEntity.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.PLAYERS, 1.0f, 1.0f);
		}
	}

	public void nextPage() {
		if (grouping < openEntry.pages.size() / 2f - 1) {
			grouping += 1;
			playSound();
		}
	}

	public void previousPage() {
		if (grouping > 0) {
			grouping -= 1;
			playSound();
		}
	}
}
