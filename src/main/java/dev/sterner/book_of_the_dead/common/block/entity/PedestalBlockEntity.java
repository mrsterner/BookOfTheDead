package dev.sterner.book_of_the_dead.common.block.entity;

import dev.sterner.book_of_the_dead.common.registry.BotDBlockEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PedestalBlockEntity extends BlockEntity {
	private ItemStack stack;
	private boolean crafting;
	private boolean craftingFinished;

	public PedestalBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.PEDESTAL, pos, state);
		stack = ItemStack.EMPTY;
		craftingFinished = false;
	}

	public static void tick(World tickerWorld, BlockPos pos, BlockState tickerState, PedestalBlockEntity blockEntity) {
	}


	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		this.setStack(ItemStack.fromNbt(tag.getCompound("pedestal_item")));
		this.crafting = tag.getBoolean("crafting");
		this.craftingFinished = tag.getBoolean("crafting_finished");

	}

	@Override
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		tag.put("pedestal_item", this.stack.writeNbt(new NbtCompound()));
		tag.putBoolean("crafting", crafting);
		tag.putBoolean("crafting_finished", craftingFinished);

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
