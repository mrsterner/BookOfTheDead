package dev.sterner.book_of_the_dead.common.block.entity;

import com.mojang.datafixers.util.Pair;
import dev.sterner.book_of_the_dead.api.interfaces.IBlockEntityInventory;
import dev.sterner.book_of_the_dead.api.interfaces.IHauler;
import dev.sterner.book_of_the_dead.common.entity.CorpseEntity;
import dev.sterner.book_of_the_dead.common.recipe.ButcheringRecipe;
import dev.sterner.book_of_the_dead.common.registry.BotDBlockEntityTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDEntityTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.registry.BotDRecipeTypes;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ButcherTableBlockEntity extends BlockEntity implements IBlockEntityInventory, IHauler {
	public NbtCompound storedCorpseNbt = new NbtCompound();
	public DefaultedList<ItemStack> outputs = DefaultedList.ofSize(8, ItemStack.EMPTY);
	public DefaultedList<Float> chances = DefaultedList.ofSize(8, 1F);
	public ButcheringRecipe butcheringRecipe = null;
	public boolean resetRecipe = true;
	public ButcherTableBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.BUTCHER, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState tickerState, ButcherTableBlockEntity blockEntity) {
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
				if(this.outputs.size() > 0 && this.outputs.get(0).isOf(Items.AIR) && resetRecipe){
					Optional<Entity> entity = EntityType.getEntityFromNbt(getCorpseEntity().getCompound(Constants.Nbt.CORPSE_ENTITY), world);
					if(entity.isPresent() && !world.isClient()){
						butcheringRecipe = world.getRecipeManager().listAllOfType(BotDRecipeTypes.BUTCHERING_RECIPE_TYPE)
								.stream().filter(type -> type.entityType == entity.get().getType()).findFirst().orElse(null);
						if(butcheringRecipe != null){
							DefaultedList<Pair<ItemStack, Float>> outputsWithChance = DefaultedList.ofSize(butcheringRecipe.getOutputs().size(), Pair.of(Items.ACACIA_DOOR.getDefaultStack(), 1f));
							for(int i = 0; i < butcheringRecipe.getOutputs().size(); i++){
								outputsWithChance.set(i, Pair.of(butcheringRecipe.getOutputs().get(i).getFirst().copy(), butcheringRecipe.getOutputs().get(i).getSecond()));
							}
							craftRecipe(outputsWithChance);
							resetRecipe = false;
						}
					}
				}
				if(player.isSneaking() && player.getMainHandStack().isEmpty()){
					IHauler.of(player).ifPresent(hauler ->{
						Optional<Entity> entity = EntityType.getEntityFromNbt(getCorpseEntity().getCompound(Constants.Nbt.CORPSE_ENTITY), world);

						if(hauler.getCorpseEntity().isEmpty() && entity.isPresent() && entity.get() instanceof LivingEntity livingEntity){
							CorpseEntity corpse = new CorpseEntity(BotDEntityTypes.CORPSE_ENTITY, world);
							corpse.setCorpseEntity(livingEntity);
							hauler.setCorpseEntity(corpse);
							clearCorpseData();
							this.outputs = DefaultedList.ofSize(8, ItemStack.EMPTY);
							this.chances = DefaultedList.ofSize(8, 1.5f);
						}
					});
				} else if(player.getMainHandStack().isOf(BotDObjects.BUTCHER_KNIFE)){
					player.swingHand(Hand.MAIN_HAND);
					var nonEmptyOutput = this.outputs.stream().filter(item -> !item.isEmpty() || !item.isOf(Items.AIR) || item.getCount() != 0).toList();
					var nonEmptyChance = this.chances.stream().filter(chance -> chance != 0).toList();
					if(nonEmptyOutput.size() > 0){
						if(nonEmptyChance.size() > 0){
							if(world.getRandom().nextFloat() < nonEmptyChance.get(0)){
								ItemScatterer.spawn(world, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, nonEmptyOutput.get(0));
								this.chances.set(0, 0F);
							}
						}else{
							ItemScatterer.spawn(world, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, nonEmptyOutput.get(0));
						}
						this.outputs.set(0, ItemStack.EMPTY);
						nonEmptyOutput = this.outputs.stream().filter(item -> !item.isEmpty() || !item.isOf(Items.AIR) || item.getCount() != 0).toList();
						if(nonEmptyOutput.isEmpty()){
							clearCorpseData();
							clear();
							this.setCorpse(new NbtCompound());
							butcheringRecipe = null;
							resetRecipe = true;
							markDirty();
						}
					}else {
						clearCorpseData();
						this.setCorpse(new NbtCompound());
						butcheringRecipe = null;
						resetRecipe = true;
						clear();
						markDirty();
					}
				}
			}
		}
		markDirty();
		return ActionResult.PASS;
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
		Inventories.writeNbt(nbt, outputs);
		if(!storedCorpseNbt.isEmpty()){
			nbt.put(Constants.Nbt.CORPSE_ENTITY, getCorpseEntity());
		}
		nbt.putBoolean("Refresh", this.resetRecipe);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		Inventories.readNbt(nbt, outputs);
		setCorpse(nbt.getCompound(Constants.Nbt.CORPSE_ENTITY));
		this.resetRecipe = nbt.getBoolean("Refresh");
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
