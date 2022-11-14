package dev.sterner.legemeton.common.block.entity;

import dev.sterner.legemeton.api.interfaces.Hauler;
import dev.sterner.legemeton.common.registry.LegemetonBlockEntityTypes;
import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class HookBlockEntity extends BlockEntity {
	public LivingEntity storedCorpseEntity;
	public HookBlockEntity(BlockPos pos, BlockState state) {
		super(LegemetonBlockEntityTypes.HOOK, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState tickerState, HookBlockEntity blockEntity) {
		if (world != null && !world.isClient) {
			if (world.getTime() % 20 == 0) {
				blockEntity.markDirty();
			}
		}

	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.of(this);
	}

	public void sync() {
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

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		NbtCompound nbtCompound = new NbtCompound();
		if(storedCorpseEntity != null){
			nbt.put(Constants.Nbt.CORPSE_ENTITY, storedCorpseEntity.writeNbt(nbtCompound));
		}
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		EntityType.getEntityFromNbt(nbt.getCompound(Constants.Nbt.CORPSE_ENTITY), this.world).ifPresent(type -> {
			if(type instanceof LivingEntity livingEntity){
				setCorpseEntity(livingEntity);
			}
		});
	}

	public void setCorpseEntity(LivingEntity entity) {
		NbtCompound nbtCompound = new NbtCompound();
		nbtCompound.putString("id", entity.getSavedEntityId());
		entity.writeNbt(nbtCompound);
		this.storedCorpseEntity = entity;
	}
}
