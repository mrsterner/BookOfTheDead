package dev.sterner.book_of_the_dead.client.event;

import net.minecraft.client.MinecraftClient;

public final class ClientTickHandler {
	private ClientTickHandler() {
	}

	public static int ticksInGame = 0;
	public static float partialTicks = 0;
	public static float delta = 0;
	public static float total = 0;

	public static void calcDelta() {
		float oldTotal = total;
		total = ticksInGame + partialTicks;
		delta = total - oldTotal;
	}

	public static void renderTick(float renderTickTime) {
		partialTicks = renderTickTime;
	}

	public static void clientTickEnd(MinecraftClient mc) {
		if (!mc.isPaused()) {
			ticksInGame++;
			partialTicks = 0;
		}
		calcDelta();
	}
}
