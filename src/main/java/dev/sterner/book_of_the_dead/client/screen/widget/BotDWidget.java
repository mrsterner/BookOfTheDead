package dev.sterner.book_of_the_dead.client.screen.widget;

import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public abstract class BotDWidget extends ClickableWidget {
	public int page;

	public BotDWidget(int x, int y, int width, int height, Text message, int page) {
		super(x, y, width, height, message);
		this.page = page;
	}

	public void tick() {
	}

	@Override
	protected void updateNarration(NarrationMessageBuilder builder) {

	}
}
