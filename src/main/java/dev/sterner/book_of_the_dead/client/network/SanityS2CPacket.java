package dev.sterner.book_of_the_dead.client.network;

import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.PlayerSanityComponent;
import dev.sterner.book_of_the_dead.common.registry.BotDParticleTypes;
import dev.sterner.book_of_the_dead.common.util.Constants;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public class SanityS2CPacket {
	public static final Identifier ID = Constants.id("sanity");

	public static void send(PlayerEntity player) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

		ServerPlayNetworking.send((ServerPlayerEntity) player, ID, buf);
	}

	public static void handle(MinecraftClient client, ClientPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {

		if(client.player == null)return;

		client.execute(() -> {
			PlayerSanityComponent component = BotDComponents.EYE_COMPONENT.get(client.player);
			if(client.player.isSneaking()){
				component.increaseSanity(10);
			}else{
				component.decreaseSanity(10);
			}
			System.out.println(component.getSanity() + " : " + component.blinkTopCoord);
		});
	}
}
