package dev.sterner.book_of_the_dead.common.block.entity;

import com.mojang.datafixers.util.Pair;
import dev.sterner.book_of_the_dead.api.interfaces.IHauler;
import dev.sterner.book_of_the_dead.common.recipe.ButcheringRecipe;
import dev.sterner.book_of_the_dead.common.registry.BotDBlockEntityTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDRecipeTypes;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class HookBlockEntity extends BlockEntity implements IHauler {
	public NbtCompound storedCorpseNbt = new NbtCompound();
	public DefaultedList<ItemStack> outputs = DefaultedList.ofSize(8, ItemStack.EMPTY);
	public DefaultedList<Float> chances  = DefaultedList.ofSize(8, 1F);
	public int hookedAge = 0;
	public HookBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.HOOK, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState tickerState, HookBlockEntity blockEntity) {
		boolean mark = false;

		if (world != null && !world.isClient) {
			if (world.getTime() % 20 == 0 && !blockEntity.storedCorpseNbt.isEmpty()) {
				blockEntity.markDirty();
				mark = true;
				if(blockEntity.hookedAge < Constants.Values.BLEEDING){
					blockEntity.hookedAge++;
				}else{
					blockEntity.hookedAge = Constants.Values.BLEEDING;
				}
			}
			if(blockEntity.storedCorpseNbt.isEmpty()){
				blockEntity.hookedAge = 0;
			}
		}
		if(mark){
			markDirty(world, pos, tickerState);
		}
	}

	public ActionResult onUse(World world, BlockState state, BlockPos pos, PlayerEntity player, Hand hand) {
		if(hand == Hand.MAIN_HAND){
			IHauler.of(player).ifPresent(hauler -> {
				if(hauler.getCorpseEntity() != null){
					NbtCompound nbtCompound = hauler.getCorpseEntity();
					if(!nbtCompound.isEmpty()){
						setCorpse(nbtCompound);
						hauler.clearCorpseData();
					}
				}
			});
			if(getCorpseEntity() != null && getCorpseEntity().contains(Constants.Nbt.CORPSE_ENTITY)){
				Optional<Entity> entity = EntityType.getEntityFromNbt(getCorpseEntity().getCompound(Constants.Nbt.CORPSE_ENTITY), world);
				if(entity.isPresent()){
					Optional<ButcheringRecipe> optionalButcheringRecipe = world.getRecipeManager().listAllOfType(BotDRecipeTypes.BUTCHERING_RECIPE_RECIPE_TYPE)
							.stream().filter(type -> type.entityType == entity.get().getType()).findFirst();
					if(optionalButcheringRecipe.isPresent()){
						ButcheringRecipe butcheringRecipe = optionalButcheringRecipe.get();
						DefaultedList<Pair<ItemStack, Float>> outputsWithChance = butcheringRecipe.getOutputs();
						if(craftRecipe(outputsWithChance)){
							return ActionResult.CONSUME;
						}
					}
				}
			}
		}
		return ActionResult.PASS;
	}

	public boolean craftRecipe(DefaultedList<Pair<ItemStack, Float>> outputs) {
		if (this.world == null) {
			return false;
		} else if (outputs == null) {
			return false;
		} else if (outputs.isEmpty()) {
			return false;
		} else {
			List<ItemStack> itemStackList = outputs.stream().map(Pair::getFirst).toList();
			List<Float> chanceList = outputs.stream().map(Pair::getSecond).toList();
			DefaultedList<ItemStack> defaultedList = DefaultedList.copyOf(ItemStack.EMPTY, itemStackList.toArray(new ItemStack[0]));
			DefaultedList<Float> defaultedList2 = DefaultedList.copyOf(1F, chanceList.toArray(new Float[0]));

			this.outputs = defaultedList;
			this.chances = defaultedList2;
			return true;
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
		NbtList nbtList = new NbtList();
		if(outputs != null){
			for(int i = 0; i < outputs.size(); ++i) {
				ItemStack itemStack = outputs.get(i);
				if (!itemStack.isEmpty()) {
					NbtCompound nbtCompound = new NbtCompound();
					nbtCompound.putByte("Slot", (byte)i);
					itemStack.writeNbt(nbtCompound);
					nbtCompound.putFloat("Chance", i);
					nbtList.add(nbtCompound);
				}
			}
			nbt.put("Items", nbtList);
		}

		nbt.putInt(Constants.Nbt.HOOKED_AGE, this.hookedAge);
		if(!storedCorpseNbt.isEmpty()){
			nbt.put(Constants.Nbt.CORPSE_ENTITY, getCorpseEntity());
		}
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		NbtList nbtList = nbt.getList("Items", NbtElement.COMPOUND_TYPE);
		for(int i = 0; i < nbtList.size(); ++i) {
			NbtCompound nbtCompound = nbtList.getCompound(i);
			int j = nbtCompound.getByte("Slot") & 255;
			if (j < outputs.size()) {
				outputs.set(j, ItemStack.fromNbt(nbtCompound));
			}
			chances.set(j, nbtCompound.getFloat("Chance"));
		}

		this.hookedAge = nbt.getInt(Constants.Nbt.HOOKED_AGE);
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
