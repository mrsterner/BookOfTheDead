package dev.sterner.book_of_the_dead.common.util;

import dev.sterner.book_of_the_dead.client.particle.ItemStackBeamParticleEffect;
import dev.sterner.book_of_the_dead.common.registry.BotDParticleTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ParticleUtils {
	/**
	 * generates the beam of particles from the pedestals to the ritual center
	 *
	 * @param from 		   blockpos from
	 * @param to  		   blockpos to
	 * @param world        world
	 * @param itemStack    itemstack for particle effect
	 */
	public static void generatePedestalParticleBeam(Vec3d from, Vec3d to, World world, ItemStack itemStack) {
		Vec3d directionVector = to.subtract(from);

		double x = from.getX() + (world.random.nextDouble() * 0.2D) + 0.4D;
		double y = from.getY() + (world.random.nextDouble() * 0.2D) + 1.2D;
		double z = from.getZ() + (world.random.nextDouble() * 0.2D) + 0.4D;
		if (world instanceof ServerWorld serverWorld && !itemStack.isEmpty()) {
			serverWorld.spawnParticles(
					new ItemStackBeamParticleEffect(BotDParticleTypes.ITEM_BEAM_PARTICLE, itemStack, 10),
					x,
					y,
					z,
					0,
					directionVector.x,
					directionVector.y,
					directionVector.z,
					0.10D);
		}
	}

	/**
	 * Handles the output items sound and particle effect
	 *
	 * @param serverWorld serverWorld
	 * @param x           coordinate for sound and particle
	 * @param y           coordinate for sound and particle
	 * @param z           coordinate for sound and particle
	 */
	public static void generateItemParticle(ServerWorld serverWorld, double x, double y, double z, List<ItemStack> items) {
		if (items != null) {
			for (ItemStack output : items) {
				for (int i = 0; i < items.size() * 2; i++) {
					serverWorld.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, output),
							x + ((serverWorld.random.nextDouble() / 2) - 0.25),
							y + ((serverWorld.random.nextDouble() / 2) - 0.25),
							z + ((serverWorld.random.nextDouble() / 2) - 0.25),
							0,
							1 * ((serverWorld.random.nextDouble() / 2) - 0.25),
							1 * ((serverWorld.random.nextDouble() / 2) - 0.25),
							1 * ((serverWorld.random.nextDouble() / 2) - 0.25),
							0);
				}
			}
		}
	}
}
