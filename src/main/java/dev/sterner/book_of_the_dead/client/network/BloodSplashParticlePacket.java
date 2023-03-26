package dev.sterner.book_of_the_dead.client.network;

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

public class BloodSplashParticlePacket {
	public static final Identifier ID = Constants.id("blood_splash_particle");

	public static void send(PlayerEntity player, double posX, double posY, double posZ) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);

		ServerPlayNetworking.send((ServerPlayerEntity) player, ID, buf);
	}

	public static void handle(MinecraftClient client, ClientPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		double posX = buf.readDouble();
		double posY = buf.readDouble();
		double posZ = buf.readDouble();

		ClientWorld world = client.world;
		client.execute(() -> {
			var rand = world.random;
			for (int i = 0; i < 24; ++i) {
				double x = posX + (rand.nextDouble() * 2.0 - 1.0) * 1 * 0.5;
				double y = posY + 0.05 + rand.nextDouble();
				double z = posZ + (rand.nextDouble() * 2.0 - 1.0) * 1 * 0.5;

				double dx = (rand.nextFloat() / 2.0F);
				double dy = 5.0E-5;
				double dz = (rand.nextFloat() / 2.0F);
				world.addParticle(BotDParticleTypes.SPLASHING_BLOOD, x + 0.5, y + 1.0, z + 0.5, dx, dy, dz);
			}
		});
	}


}
