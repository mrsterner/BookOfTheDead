package dev.sterner.book_of_the_dead.common.block.entity;

import dev.sterner.book_of_the_dead.api.block.entity.BaseBlockEntity;
import dev.sterner.book_of_the_dead.client.particle.ItemStackBeamParticleEffect;
import dev.sterner.book_of_the_dead.common.registry.BotDBlockEntityTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDParticleTypes;
import dev.sterner.book_of_the_dead.common.util.BotDUtils;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PedestalBlockEntity extends BaseBlockEntity {
	private ItemStack stack;
	private boolean crafting;
	public Vec3d ritualCenter;
	public int duration = 0;
	public double targetY = 0;

	public PedestalBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.PEDESTAL, pos, state);
		stack = ItemStack.EMPTY;
	}

	public void tick(World world, BlockPos pos, BlockState state) {
		if (world != null && isCrafting()) {
			Vec3d b = ritualCenter.subtract(new Vec3d(pos.getX(), pos.getY(), pos.getZ()).add(0.5, 1.5 - targetY, 0.5));
			Vec3d directionVector = new Vec3d(b.getX(), b.getY(), b.getZ());

			double x = pos.getX() + (world.random.nextDouble() * 0.2D) + 0.4D;
			double y = pos.getY() + (world.random.nextDouble() * 0.2D) + 1.2D;
			double z = pos.getZ() + (world.random.nextDouble() * 0.2D) + 0.4D;
			if (world instanceof ServerWorld serverWorld && !getStack().isEmpty()) {
				serverWorld.spawnParticles(
						new ItemStackBeamParticleEffect(
								BotDParticleTypes.ITEM_BEAM_PARTICLE,
								getStack(),
								10),
						x, y, z, 0, directionVector.x, directionVector.y, directionVector.z, 0.10D);
			}
		}
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.setStack(ItemStack.fromNbt(nbt.getCompound(Constants.Nbt.PEDESTAL_ITEM)));
		this.setCrafting(nbt.getBoolean(Constants.Nbt.CRAFTING));
		if (nbt.contains(Constants.Nbt.RITUAL_POS)) {
			this.ritualCenter = BotDUtils.toVec3d(nbt.getCompound(Constants.Nbt.RITUAL_POS));
		}
		this.duration = nbt.getInt(Constants.Nbt.DURATION);
		this.targetY = nbt.getDouble(Constants.Nbt.TARGET_Y);
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.put(Constants.Nbt.PEDESTAL_ITEM, this.stack.writeNbt(new NbtCompound()));
		nbt.putBoolean(Constants.Nbt.CRAFTING, this.isCrafting());
		if (hasRitualPos()) {
			nbt.put(Constants.Nbt.RITUAL_POS, BotDUtils.fromVec3d(this.ritualCenter));
		}
		nbt.putInt(Constants.Nbt.DURATION, this.duration);
		nbt.putDouble(Constants.Nbt.TARGET_Y, this.targetY);
	}

	private boolean hasRitualPos() {
		return this.ritualCenter != null;
	}

	public ItemStack getStack() {
		return stack;
	}

	public void setStack(ItemStack stack) {
		this.stack = stack;
		if (this.world != null && !this.world.isClient) {
			this.sync(world, pos);
		}
	}

	public boolean isCrafting() {
		return crafting;
	}

	public void setCrafting(boolean crafting) {
		this.crafting = crafting;
	}
}
