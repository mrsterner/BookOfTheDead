package dev.sterner.book_of_the_dead.common.registry;

import com.mojang.blaze3d.platform.InputUtil;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBind;
import org.lwjgl.glfw.GLFW;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;

public interface BotDKeyBindings {
	KeyBind keyBinding = KeyBindingHelper.registerKeyBinding(
		new KeyBind("key.book_of_the_dead.open", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, "key.category.book_of_the_dead"));

	static void init() {
		ClientTickEvents.END.register(client -> {
			if (keyBinding.isPressed() && client.currentScreen == null) {

			}
		});
	}
}
