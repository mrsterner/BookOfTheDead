package dev.sterner.book_of_the_dead.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.sterner.book_of_the_dead.common.registry.BotDParticleTypes;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.Locale;

public class SoulParticleEffect implements ParticleEffect {

	public static final ParticleEffect.Factory<SoulParticleEffect> PARAMETERS_FACTORY = new ParticleEffect.Factory<>() {
		public SoulParticleEffect read(ParticleType<SoulParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
			stringReader.expect(' ');
			float r = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float g = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float b = (float) stringReader.readDouble();
			return new SoulParticleEffect(r, g, b);
		}

		public SoulParticleEffect read(ParticleType<SoulParticleEffect> particleType, PacketByteBuf packetByteBuf) {
			return new SoulParticleEffect(packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat());
		}
	};
	private final float red;
	private final float green;
	private final float blue;

	public SoulParticleEffect(float red, float green, float blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeFloat(this.red);
		buf.writeFloat(this.green);
		buf.writeFloat(this.blue);
	}

	@Override
	public String asString() {
		return String.format(Locale.ROOT, "%s %.2f %.2f %.2f", Registries.PARTICLE_TYPE.getId(this.getType()), this.red, this.green, this.blue);
	}

	@Override
	public ParticleType<SoulParticleEffect> getType() {
		return BotDParticleTypes.SOUL;
	}

	@ClientOnly
	public float getRed() {
		return this.red;
	}

	@ClientOnly
	public float getGreen() {
		return this.green;
	}

	@ClientOnly
	public float getBlue() {
		return this.blue;
	}
}
