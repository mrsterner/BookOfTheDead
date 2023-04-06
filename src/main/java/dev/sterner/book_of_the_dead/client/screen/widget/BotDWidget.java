package dev.sterner.book_of_the_dead.client.screen.widget;

import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public abstract class BotDWidget extends ClickableWidget {
	public BotDWidget(int x, int y, int width, int height, Text message) {
		super(x, y, width, height, message);
	}

	public void tick() {
	}

	@Override
	protected void updateNarration(NarrationMessageBuilder builder) {

	}
}
