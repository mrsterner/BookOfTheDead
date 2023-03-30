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

public class OrbitParticleEffect implements ParticleEffect {

	public static final ParticleEffect.Factory<OrbitParticleEffect> PARAMETERS_FACTORY = new ParticleEffect.Factory<>() {
		public OrbitParticleEffect read(ParticleType<OrbitParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
			stringReader.expect(' ');
			float r = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float g = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float b = (float) stringReader.readDouble();

			stringReader.expect(' ');
			float tx = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float ty = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float tz = (float) stringReader.readDouble();
			stringReader.expect(' ');
			float radius = (float) stringReader.readDouble();
			return new OrbitParticleEffect(r, g, b, tx, ty, tz, radius);
		}

		public OrbitParticleEffect read(ParticleType<OrbitParticleEffect> particleType, PacketByteBuf packetByteBuf) {
			return new OrbitParticleEffect(packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat());
		}
	};
	private final float tx;
	private final float ty;
	private final float tz;
	private final float radius;

	private final float red;
	private final float green;
	private final float blue;

	public OrbitParticleEffect(float red, float green, float blue, float tx, float ty, float tz, float radius) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.tx = tx;
		this.ty = ty;
		this.tz = tz;
		this.radius = radius;
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeFloat(this.tx);
		buf.writeFloat(this.ty);
		buf.writeFloat(this.tz);
	}

	@Override
	public ParticleType<?> getType() {
		return BotDParticleTypes.SOUL_ORBIT;
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

	@ClientOnly
	public float getRadius() {
		return this.radius;
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
