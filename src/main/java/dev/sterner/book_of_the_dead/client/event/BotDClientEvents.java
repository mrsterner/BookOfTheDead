package dev.sterner.book_of_the_dead.client.event;

import com.mojang.blaze3d.glfw.Window;
import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.PlayerAdviceComponent;
import dev.sterner.book_of_the_dead.common.component.PlayerSanityComponent;
import dev.sterner.book_of_the_dead.common.util.TextUtils;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
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
		PlayerSanityComponent component = BotDComponents.SANITY_COMPONENT.get(player);
		Window window = mc.getWindow();
		if(component.manager == null) return;


		float k = (float) window.getScaledWidth() / 2 - 7 - 0.5f;
		float l = (window.getScaledHeight()) - 30 - 15;

		matrixStack.push();
		component.manager.drawTexture(matrixStack, k, l);
		matrixStack.pop();

		PlayerAdviceComponent advice = BotDComponents.ADVICE_COMPONENT.get(player);
		if(advice.isActive() || true){
			matrixStack.push();
			advice.drawTexture(matrixStack, k + 18 - 4, l - 18 - 9 - 9);
			TextUtils.renderWrappingText(matrixStack, advice.getActiveAdvice(), (int)k + 18 + 9 + 4, (int)l - 18 - 9 - 9 + 3, 80);
			matrixStack.pop();

		}

	}
}
