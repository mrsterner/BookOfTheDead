package dev.sterner.legemeton.common.block.entity;

import dev.sterner.legemeton.common.block.JarBlock;
import dev.sterner.legemeton.common.registry.LegemetonBlockEntityTypes;
import dev.sterner.legemeton.common.registry.LegemetonObjects;
import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class JarBlockEntity extends BlockEntity implements Inventory {
	private DefaultedList<ItemStack> inventory;
	public int bloodAmount = 0;

	public JarBlockEntity(BlockPos pos, BlockState state) {
		super(LegemetonBlockEntityTypes.JAR, pos, state);
		this.inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
	}

	public static void tick(World world, BlockPos pos, BlockState tickerState, JarBlockEntity blockEntity) {
		boolean mark = false;
		if (world != null && !world.isClient) {
			if (world.getTime() % 10 == 0 && blockEntity.getBloodyCorpseAbove(world, pos) && blockEntity.getInventory().isEmpty() && tickerState.get(JarBlock.OPEN)) {
				if(blockEntity.bloodAmount < 100){
					blockEntity.bloodAmount++;
					blockEntity.markDirty();
					mark = true;
				}
			}
		}
		if(mark){
			markDirty(world, pos, tickerState);
		}
	}

	private boolean getBloodyCorpseAbove(World world, BlockPos pos){
		for (double y = 0; y <= Constants.Values.JAR_COLLECTION_RANGE; ++y) {
			BlockPos potentialCorpse = new BlockPos(pos.getX(), pos.getY() + y, pos.getZ());
			if(world.getBlockEntity(potentialCorpse) instanceof HookBlockEntity hookBlockEntity){
				if(hookBlockEntity.hookedAge < Constants.Values.BLEEDING){
					return true;
				}
			}
		}
		return false;
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
		return BlockEntityUpdateS2CPacket.of(this, (BlockEntity b) -> this.toInvetoryNbt());
	}


	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		Inventories.readNbt(nbt, inventory);
		this.bloodAmount = nbt.getInt(Constants.Nbt.BLOOD_LEVEL);
		markDirty();
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, inventory);
		nbt.putInt(Constants.Nbt.BLOOD_LEVEL, this.bloodAmount);
	}

	public NbtCompound toInvetoryNbt(){
		NbtCompound rtn = new NbtCompound();
		Inventories.writeNbt(rtn, inventory);
		rtn.putInt(Constants.Nbt.BLOOD_LEVEL, this.bloodAmount);
		return rtn;
	}


	@Override
	public void markDirty() {
		super.markDirty();
		if (world != null && !world.isClient) {
			world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
			toUpdatePacket();
		}
	}

	@Override
	public int size() {
		return inventory.size();
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < size(); i++) {
			if (getStack(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStack(int slot) {
		return inventory.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return Inventories.splitStack(inventory, slot, amount);
	}

	@Override
	public ItemStack removeStack(int slot) {
		return Inventories.removeStack(inventory, slot);
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		inventory.set(slot, stack);
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	public void clear() {
		inventory.clear();
	}

	public DefaultedList<ItemStack> getInventory(){
		return inventory;
	}
}
