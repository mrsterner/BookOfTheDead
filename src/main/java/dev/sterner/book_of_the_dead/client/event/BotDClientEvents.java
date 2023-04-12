package dev.sterner.book_of_the_dead.client.event;

import com.mojang.blaze3d.glfw.Window;
import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.PlayerSanityComponent;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;

public interface BotDClientEvents {

	static void init() {
		ClientTickEvents.END.register(ClientTickHandler::clientTickEnd);
		HudRenderCallback.EVENT.register(BotDClientEvents::renderEye);
	}


	static void renderEye(MatrixStack matrixStack, float tickDelta) {
		MinecraftClient mc = MinecraftClient.getInstance();
		PlayerEntity player = mc.player;
		if (player == null) {
			return;
		}
		PlayerSanityComponent component = BotDComponents.EYE_COMPONENT.get(player);
		Window window = mc.getWindow();
		matrixStack.push();
		if(component.manager != null) {
			float k = (float) window.getScaledWidth() / 2 - 7 - 0.5f;
			float l = (window.getScaledHeight()) - 30 - 15;
			component.manager.drawTexture(matrixStack, k, l);
		}
		matrixStack.pop();
	}
}
