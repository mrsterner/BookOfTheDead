package dev.sterner.book_of_the_dead.client.event;

import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;

public interface BotDClientEvents {

	static void init() {
		ClientTickEvents.END.register(ClientTickHandler::clientTickEnd);
	}
}
