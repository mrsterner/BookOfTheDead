package dev.sterner.book_of_the_dead.api.block.entity;

import com.mojang.datafixers.util.Pair;
import dev.sterner.book_of_the_dead.api.interfaces.IBlockEntityInventory;
import dev.sterner.book_of_the_dead.api.interfaces.IHauler;
import dev.sterner.book_of_the_dead.common.recipe.ButcheringRecipe;
import dev.sterner.book_of_the_dead.common.registry.BotDRecipeTypes;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class BaseButcherBlockEntity extends BlockEntity implements IHauler, IBlockEntityInventory {
	public NbtCompound storedCorpseNbt = new NbtCompound();
	public DefaultedList<ItemStack> outputs = DefaultedList.ofSize(8, ItemStack.EMPTY);
	public DefaultedList<Float> chances  = DefaultedList.ofSize(8, 1F);
	public ButcheringRecipe butcheringRecipe = null;
	public boolean resetRecipe = true;

	public BaseButcherBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public void craftRecipe(DefaultedList<Pair<ItemStack, Float>> outputs) {
		if (this.world != null) {
			List<ItemStack> itemStackList = outputs.stream().map(Pair::getFirst).filter(item -> !item.isOf(Items.AIR)).toList();
			List<Float> chanceList = outputs.stream().map(Pair::getSecond).toList();
			DefaultedList<ItemStack> defaultedList = DefaultedList.copyOf(ItemStack.EMPTY, itemStackList.toArray(new ItemStack[0]));
			DefaultedList<Float> defaultedList2 = DefaultedList.copyOf(1F, chanceList.toArray(new Float[0]));
			this.outputs = defaultedList;
			this.chances = defaultedList2;
		}
	}

	public void refreshButcheringRecipe(){
		if(getCorpseEntity() != null){
			if(this.outputs.size() > 0 && this.outputs.get(0).isOf(Items.AIR) && resetRecipe){
				Optional<Entity> entity = EntityType.getEntityFromNbt(getCorpseEntity(), world);
				if(entity.isPresent() && !world.isClient()){
					butcheringRecipe = world.getRecipeManager().listAllOfType(BotDRecipeTypes.BUTCHERING_RECIPE_TYPE)
							.stream().filter(type -> type.entityType == entity.get().getType()).findFirst().orElse(null);
					if(butcheringRecipe != null){
						DefaultedList<Pair<ItemStack, Float>> outputsWithChance = DefaultedList.ofSize(butcheringRecipe.getOutputs().size(), Pair.of(ItemStack.EMPTY, 1f));
						for(int i = 0; i < butcheringRecipe.getOutputs().size(); i++){
							outputsWithChance.set(i, Pair.of(butcheringRecipe.getOutputs().get(i).getFirst().copy(), butcheringRecipe.getOutputs().get(i).getSecond()));
						}
						craftRecipe(outputsWithChance);
						resetRecipe = false;
					}
				}
			}
		}
	}

	public void reset(){
		clearCorpseData();
		clear();
		this.setCorpse(new NbtCompound());
		butcheringRecipe = null;
		resetRecipe = true;
		markDirty();
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, outputs);
		writeChancesNbt(nbt, chances);
		if(!storedCorpseNbt.isEmpty()){
			nbt.put(Constants.Nbt.CORPSE_ENTITY, getCorpseEntity());
		}
		nbt.putBoolean("Refresh", this.resetRecipe);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		Inventories.readNbt(nbt, outputs);
		readChanceNbt(nbt, chances);
		setCorpse(nbt.getCompound(Constants.Nbt.CORPSE_ENTITY));
		this.resetRecipe = nbt.getBoolean("Refresh");
	}

	public static NbtCompound writeChancesNbt(NbtCompound nbt, DefaultedList<Float> floats) {
		NbtList nbtList = new NbtList();
		for (float aFloat : floats) {
			NbtCompound nbtCompound = new NbtCompound();
			nbtCompound.putFloat("Float", aFloat);
			nbtList.add(nbtCompound);
		}
		nbt.put("Floats", nbtList);
		return nbt;
	}

	public static void readChanceNbt(NbtCompound nbt, DefaultedList<Float> floats) {
		NbtList nbtList = nbt.getList("Floats", NbtElement.COMPOUND_TYPE);
		for(int i = 0; i < nbtList.size(); ++i) {
			NbtCompound nbtCompound = nbtList.getCompound(i);
			float j = nbtCompound.getFloat("Float");
			floats.set(i, j);
		}
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
	public DefaultedList<ItemStack> getItems() {
		return outputs;
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
		this.storedCorpseNbt = new NbtCompound();
	}
}
