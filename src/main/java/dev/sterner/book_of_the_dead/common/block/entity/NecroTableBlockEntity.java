package dev.sterner.book_of_the_dead.common.block.entity;

import dev.sterner.book_of_the_dead.api.NecrotableRitual;
import dev.sterner.book_of_the_dead.api.enums.HorizontalDoubleBlockHalf;
import dev.sterner.book_of_the_dead.api.interfaces.IBlockEntityInventory;
import dev.sterner.book_of_the_dead.api.block.HorizontalDoubleBlock;
import dev.sterner.book_of_the_dead.common.recipe.RitualRecipe;
import dev.sterner.book_of_the_dead.common.registry.BotDBlockEntityTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.registry.BotDRecipeTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDRegistries;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
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

import java.util.UUID;

public class NecroTableBlockEntity extends BlockEntity implements IBlockEntityInventory {
	private final DefaultedList<ItemStack> ITEMS = DefaultedList.ofSize(8, ItemStack.EMPTY);
	public boolean hasBotD = false;
	public boolean hasEmeraldTablet = false;
	public NecrotableRitual currentNecrotableRitual = null;
	private boolean loaded = false;
	private int timer = 0;
	public long age = 0;
	public UUID user = null;

	public Entity targetedEntity = null;
	public NecroTableBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.NECRO, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState blockState, NecroTableBlockEntity blockEntity) {
		if (world != null && blockState.isOf(BotDObjects.NECRO_TABLE) && blockState.get(HorizontalDoubleBlock.HHALF) == HorizontalDoubleBlockHalf.RIGHT) {
			if (!blockEntity.loaded) {
				blockEntity.markDirty();
				blockEntity.loaded = true;
			}
			blockEntity.age++;
			if (blockEntity.currentNecrotableRitual != null) {
				blockEntity.timer++;
				if (blockEntity.timer >= 0) {
					blockEntity.currentNecrotableRitual.tick(world, pos, blockEntity);
				}
				if(blockEntity.timer >= blockEntity.currentNecrotableRitual.duration){
					blockEntity.currentNecrotableRitual.onStopped(world, pos, blockEntity);
					blockEntity.currentNecrotableRitual = null;
					blockEntity.timer = 0;
					blockEntity.markDirty();
				}
			}
		}

	}

	public ActionResult onUse(World world, BlockState state, BlockPos pos, PlayerEntity player, Hand hand) {
		if (world.getBlockEntity(pos) instanceof NecroTableBlockEntity necroTableBlockEntity) {
			if (necroTableBlockEntity.currentNecrotableRitual != null) {
				return ActionResult.PASS;
			}
			if (!world.isClient) {
				necroTableBlockEntity.setUser(player);
				necroTableBlockEntity.sync(world, pos);
				RitualRecipe ritualRecipe = BotDRecipeTypes.getRiteRecipe(necroTableBlockEntity);
				if (ritualRecipe != null) {
					NecrotableRitual necrotableRitual = ritualRecipe.ritual;
					necroTableBlockEntity.currentNecrotableRitual = necrotableRitual;
					necrotableRitual.onStart(world, pos, necroTableBlockEntity);
					necroTableBlockEntity.markDirty();
					necroTableBlockEntity.sync(world, pos);


					return ActionResult.CONSUME;
				}
			}

		}
		return ActionResult.PASS;
	}

	public PlayerEntity getUser() {
		return world != null && user != null ? world.getPlayerByUuid(user) : null;
	}

	public void setUser(PlayerEntity player) {
		if (currentNecrotableRitual == null || user == null) {
			this.user = player.getUuid();
		}
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


	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		ITEMS.clear();
		Inventories.readNbt(nbt, ITEMS);
		currentNecrotableRitual = BotDRegistries.NECROTABLE_RITUALS.get(new Identifier(nbt.getString(Constants.Nbt.NECRO_RITUAL)));
		this.hasBotD = nbt.getBoolean(Constants.Nbt.HAS_LEGEMETON);
		this.hasEmeraldTablet = nbt.getBoolean(Constants.Nbt.HAS_EMERALD_TABLET);
		this.timer = nbt.getInt(Constants.Nbt.TIMER);
		this.age = nbt.getLong(Constants.Nbt.AGE);
		if (nbt.contains(Constants.Nbt.PLAYER_UUID)) {
			user = nbt.getUuid(Constants.Nbt.PLAYER_UUID);
		} else {
			user = null;
		}
		markDirty();
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, ITEMS);
		if (currentNecrotableRitual != null) {
			nbt.putString(Constants.Nbt.NECRO_RITUAL, BotDRegistries.NECROTABLE_RITUALS.getId(currentNecrotableRitual).toString());
		}
		nbt.putBoolean(Constants.Nbt.HAS_LEGEMETON, this.hasBotD);
		nbt.putBoolean(Constants.Nbt.HAS_EMERALD_TABLET, this.hasEmeraldTablet);
		nbt.putInt(Constants.Nbt.TIMER, this.timer);
		nbt.putLong(Constants.Nbt.AGE, this.age);
		if (user != null) {
			nbt.putUuid(Constants.Nbt.PLAYER_UUID, user);
		}
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
	public DefaultedList<ItemStack> getItems() {
		return ITEMS;
	}
}
