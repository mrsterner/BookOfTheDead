package dev.sterner.legemeton.common.block.entity;

import dev.sterner.legemeton.api.interfaces.Hauler;
import dev.sterner.legemeton.common.entity.CorpseEntity;
import dev.sterner.legemeton.common.registry.LegemetonBlockEntityTypes;
import dev.sterner.legemeton.common.registry.LegemetonEntityTypes;
import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class HookBlockEntity extends BlockEntity implements Hauler {
	public NbtCompound storedCorpseNbt = new NbtCompound();
	public HookBlockEntity(BlockPos pos, BlockState state) {
		super(LegemetonBlockEntityTypes.HOOK, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState tickerState, HookBlockEntity blockEntity) {
		boolean mark = false;

		if (world != null && !world.isClient) {
			if (world.getTime() % 20 == 0 && !blockEntity.storedCorpseNbt.isEmpty()) {
				blockEntity.markDirty();
				mark = true;
			}
		}
		if(mark){
			markDirty(world, pos, tickerState);
		}
	}

	public void onUse(PlayerEntity player, Hand hand) {
		if(hand == Hand.MAIN_HAND){
			Hauler.of(player).ifPresent(hauler -> {
				if(hauler.getCorpseEntity() != null){
					NbtCompound nbtCompound = hauler.getCorpseEntity();
					if(!nbtCompound.isEmpty()){
						setCorpse(nbtCompound);
						hauler.clearCorpseData();
					}
				}
			});
		}
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.of(this, (BlockEntity b) -> toNbt2());
	}


	public NbtCompound toNbt2() {
		NbtCompound nbtCompound = new NbtCompound();
		this.writeNbt(nbtCompound);
		return nbtCompound;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		if (world != null && !world.isClient) {
			world.updateListeners(pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_LISTENERS);
			toUpdatePacket();
		}
		if(world instanceof ServerWorld serverWorld){
			serverWorld.getChunkManager().markForUpdate(pos);
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
		if(!storedCorpseNbt.isEmpty()){
			nbt.put(Constants.Nbt.CORPSE_ENTITY, getCorpseEntity());
		}
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		setCorpse(nbt.getCompound(Constants.Nbt.CORPSE_ENTITY));
	}

	@Override
	public LivingEntity getCorpseLiving() {
		return null;
	}

	@Override
	public NbtCompound getCorpseEntity() {
		return storedCorpseNbt;
	}

	public void setCorpse(NbtCompound nbtCompound){
		this.storedCorpseNbt = nbtCompound;
	}

	@Override
	public void setCorpseEntity(LivingEntity entity) {
		NbtCompound nbtCompound = new NbtCompound();
		nbtCompound.putString("id", entity.getSavedEntityId());
		entity.writeNbt(nbtCompound);
		this.storedCorpseNbt = nbtCompound;
	}

	@Override
	public void clearCorpseData() {
		this.storedCorpseNbt = null;
	}


}
