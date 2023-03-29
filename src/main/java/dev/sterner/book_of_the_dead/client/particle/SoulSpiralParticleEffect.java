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

public class SoulSpiralParticleEffect implements ParticleEffect {

	public static final ParticleEffect.Factory<SoulSpiralParticleEffect> PARAMETERS_FACTORY = new ParticleEffect.Factory<>() {
		public SoulSpiralParticleEffect read(ParticleType<SoulSpiralParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
			stringReader.expect(' ');
			float tx = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float ty = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float tz = (float) stringReader.readDouble();
			return new SoulSpiralParticleEffect(tx, ty, tz);
		}

		public SoulSpiralParticleEffect read(ParticleType<SoulSpiralParticleEffect> particleType, PacketByteBuf packetByteBuf) {
			return new SoulSpiralParticleEffect(packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat());
		}
	};
	private final float tx;
	private final float ty;
	private final float tz;

	public SoulSpiralParticleEffect(float tx, float ty, float tz) {
		this.tx = tx;
		this.ty = ty;
		this.tz = tz;
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeFloat(this.tx);
		buf.writeFloat(this.ty);
		buf.writeFloat(this.tz);
	}

	@Override
	public ParticleType<?> getType() {
		return BotDParticleTypes.SOUL_SPIRAL;
	}

	@Override
	public String asString() {
		return String.format(Locale.ROOT, "%s %.2f %.2f %.2f", Registries.PARTICLE_TYPE.getId(this.getType()), this.tx, this.ty, this.tz);
	}

	@ClientOnly
	public float getTargetX() {
		return this.tx;
	}

	@ClientOnly
	public float getTargetY() {
		return this.ty;
	}

	@ClientOnly
	public float getTargetZ() {
		return this.tz;
	}
}
