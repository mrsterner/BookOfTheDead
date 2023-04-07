package dev.sterner.book_of_the_dead.common.network;

import dev.sterner.book_of_the_dead.api.Knowledge;
import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.PlayerKnowledgeComponent;
import dev.sterner.book_of_the_dead.common.registry.BotDRegistries;
import dev.sterner.book_of_the_dead.common.util.Constants;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;


public class KnowledgeC2SPacket {
	public static final Identifier ID = Constants.id("knowledge");

	public static void send(Knowledge knowledge) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

		buf.writeString(knowledge.identifier);

		ClientPlayNetworking.send(ID, buf);
	}

	public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		Identifier id = Constants.id(buf.readString());
		if (BotDRegistries.KNOWLEDGE.getIds().contains(id)) {
			Knowledge knowledge = BotDRegistries.KNOWLEDGE.get(id);

			server.execute(() -> {
				PlayerKnowledgeComponent component = BotDComponents.KNOWLEDGE_COMPONENT.get(player);
				if (knowledge != null) {
					boolean bl = component.addKnowledge(knowledge);
					if (bl) {
						player.world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1, 1);
					}
				}
			});
		}
	}
}
