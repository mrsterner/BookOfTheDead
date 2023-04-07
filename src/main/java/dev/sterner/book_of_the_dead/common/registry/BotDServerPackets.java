package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.common.network.KnowledgeC2SPacket;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public interface BotDServerPackets {

	static void init(){
		ServerPlayNetworking.registerGlobalReceiver(KnowledgeC2SPacket.ID, KnowledgeC2SPacket::handle);
	}
}
