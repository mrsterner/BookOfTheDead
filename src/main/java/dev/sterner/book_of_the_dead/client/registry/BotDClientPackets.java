package dev.sterner.book_of_the_dead.client.registry;

import dev.sterner.book_of_the_dead.client.network.BloodSplashParticlePacket;
import dev.sterner.book_of_the_dead.client.network.OpenBookPacket;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public interface BotDClientPackets {

	static void init() {
		ClientPlayNetworking.registerGlobalReceiver(BloodSplashParticlePacket.ID, BloodSplashParticlePacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(OpenBookPacket.ID, OpenBookPacket::handle);
	}
}
