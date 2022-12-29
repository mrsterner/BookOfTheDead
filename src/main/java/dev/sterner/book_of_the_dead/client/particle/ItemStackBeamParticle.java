package dev.sterner.book_of_the_dead.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import org.jetbrains.annotations.Nullable;

public class ItemStackBeamParticle extends SpriteBillboardParticle {
	private final float sampleU;
	private final float sampleV;

	ItemStackBeamParticle(ClientWorld world, double x, double y, double z, double d, double e, double f, ItemStack stack, int maxAge) {
		this(world, x, y, z, stack, maxAge);
		this.velocityX *= 0.1F;
		this.velocityY *= 0.1F;
		this.velocityZ *= 0.1F;
		this.velocityX += d;
		this.velocityY += e;
		this.velocityZ += f;
	}

	protected ItemStackBeamParticle(ClientWorld world, double x, double y, double z, ItemStack stack, int maxAge) {
		super(world, x, y, z, 0.0, 0.0, 0.0);
		this.maxAge = maxAge;
		this.setSprite(MinecraftClient.getInstance().getItemRenderer().getHeldItemModel(stack, world, null, 0).getParticleSprite());
		this.gravityStrength = 0.0F;
		this.scale /= 2.0F;
		this.sampleU = this.random.nextFloat() * 3.0F;
		this.sampleV = this.random.nextFloat() * 3.0F;
	}

	@Override
	protected float getMinU() {
		return this.sprite.getFrameU((double)((this.sampleU + 1.0F) / 4.0F * 16.0F));
	}

	@Override
	protected float getMaxU() {
		return this.sprite.getFrameU((double)(this.sampleU / 4.0F * 16.0F));
	}

	@Override
	protected float getMinV() {
		return this.sprite.getFrameV((double)(this.sampleV / 4.0F * 16.0F));
	}

	@Override
	protected float getMaxV() {
		return this.sprite.getFrameV((double)((this.sampleV + 1.0F) / 4.0F * 16.0F));
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.TERRAIN_SHEET;
	}

	@Environment(EnvType.CLIENT)
	public static class ItemFactory implements ParticleFactory<ItemStackBeamParticleEffect> {

		@Nullable
		@Override
		public Particle createParticle(ItemStackBeamParticleEffect parameters, ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new ItemStackBeamParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ, parameters.getItemStack(), parameters.getMaxAge());
		}
	}
}
