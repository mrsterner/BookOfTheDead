package dev.sterner.book_of_the_dead.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStringReader;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.HolderLookup;
import net.minecraft.util.registry.Registry;

public class ItemStackBeamParticleEffect implements ParticleEffect {
	public static final ParticleEffect.Factory<ItemStackBeamParticleEffect> PARAMETERS_FACTORY = new ParticleEffect.Factory<ItemStackBeamParticleEffect>() {
		public ItemStackBeamParticleEffect read(ParticleType<ItemStackBeamParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
			stringReader.expect(' ');
			ItemStringReader.ItemResult itemResult = ItemStringReader.parseForItem(HolderLookup.forRegistry(Registry.ITEM), stringReader);
			ItemStack itemStack = new ItemStackArgument(itemResult.item(), itemResult.nbt()).createStack(1, false);
			stringReader.expect(' ');
			int maxAge = stringReader.readInt();
			return new ItemStackBeamParticleEffect(particleType, itemStack, maxAge);
		}

		public ItemStackBeamParticleEffect read(ParticleType<ItemStackBeamParticleEffect> particleType, PacketByteBuf packetByteBuf) {
			return new ItemStackBeamParticleEffect(particleType, packetByteBuf.readItemStack(), packetByteBuf.readInt());
		}
	};

	private final ParticleType<ItemStackBeamParticleEffect> type;
	private final ItemStack stack;
	private final int maxAge;


	public ItemStackBeamParticleEffect(ParticleType<ItemStackBeamParticleEffect> type, ItemStack stack, int maxAge) {
		this.type = type;
		this.stack = stack;
		this.maxAge = maxAge;
	}

	public ItemStack getItemStack() {
		return this.stack;
	}

	public int getMaxAge() {
		return this.maxAge;
	}

	@Override
	public String asString() {
		return Registry.PARTICLE_TYPE.getId(this.getType()) + " " + new ItemStackArgument(this.stack.getHolder(), this.stack.getNbt()).asString();
	}

	@Override
	public ParticleType<ItemStackBeamParticleEffect> getType() {
		return this.type;
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeItemStack(this.stack);
		buf.writeInt(this.maxAge);
	}

}
