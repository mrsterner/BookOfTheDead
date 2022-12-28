package dev.sterner.book_of_the_dead.common.block.entity;

import dev.sterner.book_of_the_dead.client.particle.ItemStackBeamParticle;
import dev.sterner.book_of_the_dead.client.particle.ItemStackBeamParticleEffect;
import dev.sterner.book_of_the_dead.common.registry.BotDBlockEntityTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDParticleTypes;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PedestalBlockEntity extends BlockEntity {
	private ItemStack stack;
	private boolean loaded = false;
	private boolean crafting;
	private boolean craftingFinished;
	public BlockPos ritualCenter;

	public PedestalBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.PEDESTAL, pos, state);
		stack = ItemStack.EMPTY;
		craftingFinished = false;
	}

	public static void tick(World world, BlockPos blockPos, BlockState blockState, PedestalBlockEntity blockEntity) {
		if (world != null ) {//&& blockEntity.isCrafting()) {
			if(!blockEntity.getStack().isEmpty() && blockEntity.hasRitualPos()){
				double yOffset = MathHelper.sin((world.getTime()) / 10F);
				BlockPos b = blockEntity.ritualCenter.subtract(blockPos.add(0.5,0.5,0.5));
				Vec3d directionVector = new Vec3d(b.getX(), b.getY(), b.getZ());

				double x = blockPos.getX() + (world.random.nextDouble() * 0.2D) + 0.4D;
				double y = blockPos.getY() + (world.random.nextDouble() * 0.2D) + 1.2D;
				double z = blockPos.getZ() + (world.random.nextDouble() * 0.2D) + 0.4D;
				if(world instanceof ServerWorld serverWorld){
					serverWorld.spawnParticles(
							new ItemStackBeamParticleEffect(
									BotDParticleTypes.ITEM_BEAM_PARTICLE,
									blockEntity.getStack(),
									10),
							x, y, z, 0, directionVector.x, directionVector.y, directionVector.z, 0.10D);
				}
				if(blockEntity.craftingFinished){
					blockEntity.getStack().decrement(1);
					blockEntity.setCrafting(false);
					blockEntity.setCraftingFinished(false);
				}
			}
		}
	}


	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.setStack(ItemStack.fromNbt(nbt.getCompound(Constants.Nbt.PEDESTAL_ITEM)));
		this.setCrafting(nbt.getBoolean(Constants.Nbt.CRAFTING));
		this.setCraftingFinished(nbt.getBoolean(Constants.Nbt.CRAFTING_FINISHED));
		if (nbt.contains(Constants.Nbt.RITUAL_POS)) {
			this.ritualCenter = NbtHelper.toBlockPos(nbt.getCompound(Constants.Nbt.RITUAL_POS));
		}
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.put(Constants.Nbt.PEDESTAL_ITEM, this.stack.writeNbt(new NbtCompound()));
		nbt.putBoolean(Constants.Nbt.CRAFTING, this.isCrafting());
		nbt.putBoolean(Constants.Nbt.CRAFTING_FINISHED, craftingFinished);
		if(hasRitualPos()){
			nbt.put(Constants.Nbt.RITUAL_POS, NbtHelper.fromBlockPos(this.ritualCenter));
		}
	}

	private boolean hasRitualPos() {
		return this.ritualCenter != null;
	}

	public ItemStack getStack() {
		return stack;
	}

	public void setStack(ItemStack stack) {
		this.stack = stack;
		if(this.world != null && !this.world.isClient) {
			this.sync(world, pos);
		}
	}

	public boolean isCrafting() {
		return crafting;
	}

	public void setCrafting(boolean crafting) {
		this.crafting = crafting;
	}

	public void setCraftingFinished(boolean finished) {
		this.craftingFinished = finished;
	}

	public void sync(World world, BlockPos pos) {
		if (world != null && !world.isClient) {
			world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
		}
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbt = super.toInitialChunkDataNbt();
		writeNbt(nbt);
		return nbt;
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.of(this);
	}


}
