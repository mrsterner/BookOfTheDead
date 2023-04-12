package dev.sterner.book_of_the_dead.client.event;

import dev.sterner.book_of_the_dead.client.EyeManager;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.util.math.MatrixStack;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;

public interface BotDClientEvents {

	static void init() {
		ClientTickEvents.END.register(ClientTickHandler::clientTickEnd);
		ClientTickEvents.END.register(EyeManager::tickEye);
		HudRenderCallback.EVENT.register(BotDClientEvents::renderEye);
	}


	static void renderEye(MatrixStack matrixStack, float tickDelta) {

	}
}
