package dev.sterner.book_of_the_dead.common.block.entity;

import dev.sterner.book_of_the_dead.api.block.entity.BaseBlockEntity;
import dev.sterner.book_of_the_dead.api.interfaces.IBlockEntityInventory;
import dev.sterner.book_of_the_dead.common.block.JarBlock;
import dev.sterner.book_of_the_dead.common.registry.BotDBlockEntityTypes;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class JarBlockEntity extends BaseBlockEntity {
	public static final int MAX_LIQUID = 100;
	public int bloodAmount = 0;
	public int waterAmount = 0;
	public boolean hasBrain = false;

	public JarBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.JAR, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState tickerState, JarBlockEntity blockEntity) {
		boolean mark = false;
		if (world != null && !world.isClient) {
			if (world.getTime() % 10 == 0 && tickerState.get(JarBlock.OPEN) && blockEntity.getBloodyCorpseAbove(world, pos)) {
				if(blockEntity.bloodAmount < MAX_LIQUID){
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
			BlockPos potentialCorpse = new BlockPos(pos.getX(), (int)(pos.getY() + y), pos.getZ());
			if(world.getBlockEntity(potentialCorpse) instanceof HookBlockEntity hookBlockEntity){
				if(hookBlockEntity.hookedAge < Constants.Values.BLEEDING){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.bloodAmount = nbt.getInt(Constants.Nbt.BLOOD_LEVEL);
		this.waterAmount = nbt.getInt(Constants.Nbt.WATER_LEVEL);
		this.hasBrain = nbt.getBoolean(Constants.Nbt.BRAIN);
		markDirty();
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putInt(Constants.Nbt.BLOOD_LEVEL, this.bloodAmount);
		nbt.putInt(Constants.Nbt.WATER_LEVEL, this.waterAmount);
		nbt.putBoolean(Constants.Nbt.BRAIN, this.hasBrain);
	}
}
