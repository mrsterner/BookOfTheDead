package dev.sterner.book_of_the_dead.client.network;

import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.common.util.Constants;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public class OpenBookPacket {
	public static final Identifier ID = Constants.id("open_book");

	public static void send(PlayerEntity player) {
		ServerPlayNetworking.send((ServerPlayerEntity) player, ID, new PacketByteBuf(Unpooled.buffer()));
	}

	public static void handle(MinecraftClient client, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
		client.execute(() -> {
			client.setScreen(new BookOfTheDeadScreen(client.player));
		});
	}
}
