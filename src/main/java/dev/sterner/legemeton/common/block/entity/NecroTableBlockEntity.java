package dev.sterner.legemeton.common.block.entity;

import dev.sterner.legemeton.api.NecrotableRitual;
import dev.sterner.legemeton.api.enums.HorizontalDoubleBlockHalf;
import dev.sterner.legemeton.common.block.NecroTableBlock;
import dev.sterner.legemeton.common.registry.LegemetonBlockEntityTypes;
import dev.sterner.legemeton.common.registry.LegemetonObjects;
import dev.sterner.legemeton.common.registry.LegemetonRegistries;
import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class NecroTableBlockEntity extends BlockEntity {
	public boolean hasLegemeton = false;
	public boolean hasEmeraldTablet = false;
	public NecrotableRitual necrotableRitual = null;
	private boolean loaded = false;
	private int timer = 0;
	public NecroTableBlockEntity(BlockPos pos, BlockState state) {
		super(LegemetonBlockEntityTypes.NECRO, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState blockState, NecroTableBlockEntity blockEntity) {
		if (world != null && blockState.isOf(LegemetonObjects.NECRO_TABLE) && blockState.get(NecroTableBlock.HHALF) == HorizontalDoubleBlockHalf.RIGHT) {
			if (!blockEntity.loaded) {
				blockEntity.markDirty();
				blockEntity.loaded = true;
			}
			if (blockEntity.necrotableRitual != null) {
				blockEntity.timer++;
				if (blockEntity.timer >= 0) {
					blockEntity.necrotableRitual.tick(world, pos, blockEntity);
				}
				if(blockEntity.timer >= blockEntity.necrotableRitual.duration){
					blockEntity.necrotableRitual.onStopped(world, pos, blockEntity);
					blockEntity.necrotableRitual = null;
					blockEntity.timer = 0;
					blockEntity.markDirty();
				}
			}
		}

	}

	public ActionResult onUse(World world, BlockState state, BlockPos pos, PlayerEntity player, Hand hand) {
		return ActionResult.PASS;
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


	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		necrotableRitual = LegemetonRegistries.NECROTABLE_RITUALS.get(new Identifier(nbt.getString(Constants.Nbt.NECRO_RITUAL)));
		this.hasLegemeton = nbt.getBoolean(Constants.Nbt.HAS_LEGEMETON);
		this.hasEmeraldTablet = nbt.getBoolean(Constants.Nbt.HAS_EMERALD_TABLET);
		this.timer = nbt.getInt(Constants.Nbt.TIMER);
		markDirty();
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (necrotableRitual != null) {
			nbt.putString(Constants.Nbt.NECRO_RITUAL, LegemetonRegistries.NECROTABLE_RITUALS.getId(necrotableRitual).toString());
		}
		nbt.putBoolean(Constants.Nbt.HAS_LEGEMETON, this.hasLegemeton);
		nbt.putBoolean(Constants.Nbt.HAS_EMERALD_TABLET, this.hasEmeraldTablet);
		nbt.putInt(Constants.Nbt.TIMER, this.timer);
	}


	@Override
	public void markDirty() {
		super.markDirty();
		if (world != null && !world.isClient) {
			world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
			toUpdatePacket();
		}
	}


}
