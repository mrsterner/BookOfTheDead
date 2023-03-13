package dev.sterner.book_of_the_dead.api.block.entity;

import com.mojang.datafixers.util.Pair;
import dev.sterner.book_of_the_dead.api.interfaces.IBlockEntityInventory;
import dev.sterner.book_of_the_dead.api.interfaces.IHauler;
import dev.sterner.book_of_the_dead.client.network.BloodSplashParticlePacket;
import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.CorpseDataComponent;
import dev.sterner.book_of_the_dead.common.component.PlayerDataComponent;
import dev.sterner.book_of_the_dead.common.recipe.ButcheringRecipe;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.registry.BotDRecipeTypes;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.networking.api.PlayerLookup;

import java.util.List;
import java.util.Optional;

import static dev.sterner.book_of_the_dead.api.block.HorizontalDoubleBlock.FACING;

public class BaseButcherBlockEntity extends BlockEntity implements IHauler, IBlockEntityInventory {
	public NbtCompound storedCorpseNbt = new NbtCompound();
	public LivingEntity storedLiving = null;
	public DefaultedList<ItemStack> outputs = DefaultedList.ofSize(8, ItemStack.EMPTY);
	public DefaultedList<Float> chances  = DefaultedList.ofSize(8, 1F);
	public ButcheringRecipe butcheringRecipe = null;
	public boolean resetRecipe = true;

	public boolean headVisible = true;
	public boolean rArmVisible = true;
	public boolean lArmVisible = true;
	public boolean rLegVisible = true;
	public boolean lLegVisible = true;

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

	public ActionResult onUse(World world, BlockState state, BlockPos pos, PlayerEntity player, Hand hand, double probability, double particleOffset, boolean isNeighbour) {
		if(hand == Hand.MAIN_HAND){
			Optional<IHauler> optionalIHauler = IHauler.of(player);
			if(optionalIHauler.isPresent()){
				if(optionalIHauler.get().getCorpseEntity() != null){
					NbtCompound nbtCompound = optionalIHauler.get().getCorpseEntity();
					if(!nbtCompound.isEmpty()){
						setCorpse(nbtCompound);
						setHeadVisible(true);
						setRArmVisible(true);
						setLArmVisible(true);
						setRLegVisible(true);
						setLLegVisible(true);
						optionalIHauler.get().clearCorpseData();
						markDirty();
						return ActionResult.CONSUME;
					}
				}
			}
			if(getCorpseEntity() != null && !getCorpseEntity().isEmpty()){

				refreshButcheringRecipe();

				if (player.getMainHandStack().isOf(BotDObjects.BUTCHER_KNIFE)){

					List<ItemStack> nonEmptyOutput = this.outputs.stream().filter(item -> !item.isEmpty() || !item.isOf(Items.AIR) || item.getCount() != 0).toList();
					List<Float> nonEmptyChance = this.chances.stream().filter(chance -> chance != 0).toList();
					if(nonEmptyOutput.size() > 0){
						double butcherLevel = BotDComponents.PLAYER_COMPONENT.maybeGet(player).map(PlayerDataComponent::getButcheringModifier).orElse(1D);
						double chance = probability + 0.5D * butcherLevel;

						nonEmptyOutput.get(0).setCount(world.getRandom().nextDouble() < chance * nonEmptyChance.get(0) ? 1 : 0);
						if(getHeadVisible() && !isNeighbour && this.butcheringRecipe.headDrop.getFirst() != ItemStack.EMPTY){
							setHeadVisible(false);
							ItemStack head = this.butcheringRecipe.headDrop.getFirst();
							head.setCount(world.getRandom().nextDouble() < chance * this.butcheringRecipe.headDrop.getSecond() ? 1 : 0);
							ItemScatterer.spawn(world, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, head);
						}else{
							dismemberAtRandom(world, isNeighbour);
							ItemScatterer.spawn(world, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, nonEmptyOutput.get(0));
						}

						PlayerLookup.tracking(player).forEach(track -> BloodSplashParticlePacket.send(track, pos.getX(), pos.getY() + particleOffset, pos.getZ()));
						BloodSplashParticlePacket.send(player, pos.getX(), pos.getY() + particleOffset, pos.getZ());

						world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_HONEY_BLOCK_BREAK, SoundCategory.PLAYERS, 2,1);
						world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 1,1);
						this.chances.set(0, 0F);
						this.outputs.set(0, ItemStack.EMPTY);
						nonEmptyOutput = this.outputs.stream().filter(item -> !item.isEmpty() || !item.isOf(Items.AIR) || item.getCount() != 0).toList();
						if(nonEmptyOutput.isEmpty()){
							reset();
						}
						player.swingHand(hand, true);
						return ActionResult.CONSUME;
					}else {
						reset();
					}
				}
			}
		}
		markDirty();
		return ActionResult.PASS;
	}

	private void dismemberAtRandom(World world, boolean isNeighbour) {
		boolean rand = world.getRandom().nextBoolean();
		if(isNeighbour){
			if(rand){
				if(getLArmVisible()){
					setLArmVisible(false);
				}
			}else{
				if(getRArmVisible()){
					setRArmVisible(false);
				}
			}
		}else{
			if(rand){
				if(getLLegVisible()){
					setLLegVisible(false);
				}
			}else{
				if(getRLegVisible()){
					setRLegVisible(false);
				}
			}
		}
	}

	public void reset(){
		clear();
		this.setCorpse(new NbtCompound());
		butcheringRecipe = null;
		resetRecipe = true;
		clearCorpseData();
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

		nbt.putBoolean(Constants.Nbt.HEAD_VISIBLE, getHeadVisible());
		nbt.putBoolean(Constants.Nbt.RIGHT_ARM_VISIBLE, getRLegVisible());
		nbt.putBoolean(Constants.Nbt.LEFT_ARM_VISIBLE, getLArmVisible());
		nbt.putBoolean(Constants.Nbt.RIGHT_LEG_VISIBLE, getRLegVisible());
		nbt.putBoolean(Constants.Nbt.LEFT_LEG_VISIBLE, getLLegVisible());
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		Inventories.readNbt(nbt, outputs);
		readChanceNbt(nbt, chances);
		setCorpse(nbt.getCompound(Constants.Nbt.CORPSE_ENTITY));
		this.resetRecipe = nbt.getBoolean("Refresh");

		setHeadVisible(nbt.getBoolean(Constants.Nbt.HEAD_VISIBLE));
		setRArmVisible(nbt.getBoolean(Constants.Nbt.RIGHT_ARM_VISIBLE));
		setLArmVisible(nbt.getBoolean(Constants.Nbt.LEFT_ARM_VISIBLE));
		setRLegVisible(nbt.getBoolean(Constants.Nbt.RIGHT_LEG_VISIBLE));
		setLLegVisible(nbt.getBoolean(Constants.Nbt.LEFT_LEG_VISIBLE));
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
		return storedLiving;
	}

	@Override
	public NbtCompound getCorpseEntity() {
		return storedCorpseNbt;
	}

	public void setCorpse(NbtCompound nbtCompound){
		this.storedCorpseNbt = nbtCompound;
		if(!nbtCompound.isEmpty() && world != null){
			Optional<Entity> living = EntityType.getEntityFromNbt(nbtCompound, world);
			if(living.isPresent() && living.get() instanceof LivingEntity livingEntity){
				this.storedLiving = livingEntity;
			}
		}
	}

	@Override
	public void setCorpseEntity(LivingEntity entity) {
		NbtCompound nbtCompound = new NbtCompound();
		nbtCompound.putString("id", entity.getSavedEntityId());
		entity.writeNbt(nbtCompound);
		this.storedCorpseNbt = nbtCompound;
		this.storedLiving = entity;
	}

	@Override
	public void clearCorpseData() {
		this.storedCorpseNbt = new NbtCompound();
		this.storedLiving = null;
	}

	@Override
	public boolean getHeadVisible() {
		return headVisible;
	}

	@Override
	public boolean getRArmVisible() {
		return rArmVisible;
	}

	@Override
	public boolean getLArmVisible() {
		return lArmVisible;
	}

	@Override
	public boolean getRLegVisible() {
		return rLegVisible;
	}

	@Override
	public boolean getLLegVisible() {
		return lLegVisible;
	}

	@Override
	public void setHeadVisible(boolean visible) {
		this.headVisible = visible;
	}

	@Override
	public void setRArmVisible(boolean visible) {
		this.rArmVisible = visible;
	}

	@Override
	public void setLArmVisible(boolean visible) {
		this.lArmVisible = visible;
	}

	@Override
	public void setRLegVisible(boolean visible) {
		this.rLegVisible = visible;
	}

	@Override
	public void setLLegVisible(boolean visible) {
		this.lLegVisible = visible;
	}
}
