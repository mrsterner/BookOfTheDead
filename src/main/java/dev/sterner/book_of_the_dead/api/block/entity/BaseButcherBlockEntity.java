package dev.sterner.book_of_the_dead.api.block.entity;

import com.mojang.datafixers.util.Pair;
import dev.sterner.book_of_the_dead.api.interfaces.IBlockEntityInventory;
import dev.sterner.book_of_the_dead.api.interfaces.IHauler;
import dev.sterner.book_of_the_dead.client.network.BloodSplashParticlePacket;
import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.player.PlayerDataComponent;
import dev.sterner.book_of_the_dead.common.recipe.ButcheringRecipe;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.registry.BotDRecipeTypes;
import dev.sterner.book_of_the_dead.common.util.BotDUtils;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
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
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.networking.api.PlayerLookup;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static dev.sterner.book_of_the_dead.api.block.HorizontalDoubleBlock.FACING;

public class BaseButcherBlockEntity extends HaulerBlockEntity implements IBlockEntityInventory {
	public NbtCompound storedCorpseNbt = new NbtCompound();
	public LivingEntity storedLiving = null;
	public DefaultedList<ItemStack> outputs = DefaultedList.ofSize(8, ItemStack.EMPTY);
	public DefaultedList<Float> chances = DefaultedList.ofSize(8, 1F);
	public ButcheringRecipe butcheringRecipe = null;
	public boolean resetRecipe = true;

	public BaseButcherBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	/**
	 * Triggered at onUse from the Butcher block
	 *
	 * @param world          world
	 * @param state          state
	 * @param pos            pos
	 * @param player         player
	 * @param hand           hand
	 * @param probability    probability
	 * @param particleOffset particle Y offset
	 * @param isNeighbour    if the butcher block is a double block and player is interacting with the recessive one.
	 * @return ActionResult
	 */
	public ActionResult onUse(World world, BlockState state, BlockPos pos, PlayerEntity player, Hand hand, double probability, double particleOffset, boolean isNeighbour) {
		if (hand == Hand.MAIN_HAND) {
			if (placeCorpseOnButcherBlock(world, pos, state, player, isNeighbour)) {
				return ActionResult.CONSUME;
			}
			if (getCorpseEntity() != null && !getCorpseEntity().isEmpty()) {
				this.refreshButcheringRecipe(world);

				if (player.getMainHandStack().isOf(BotDObjects.MEAT_CLEAVER)) {

					List<ItemStack> nonEmptyOutput = this.outputs.stream().filter(item -> !item.isEmpty() || !item.isOf(Items.AIR) || item.getCount() != 0).toList();
					List<Float> nonEmptyChance = this.chances.stream().filter(chance -> chance != 0).toList();
					if (nonEmptyOutput.size() > 0) {
						double magicNumber = calculateMagicNumber(player, probability);

						nonEmptyOutput.get(0).setCount(world.getRandom().nextDouble() < magicNumber * nonEmptyChance.get(0) ? 1 : 0);
						handleButcheringResult(world, pos, magicNumber, isNeighbour, nonEmptyOutput);
						this.makeARuckus(world, player, hand, pos, particleOffset);
						markDirty();
						return ActionResult.CONSUME;
					} else {
						this.makeARuckus(world, player, hand, pos, particleOffset);
						this.reset();
					}
				}
			}
		}
		markDirty();
		return ActionResult.PASS;
	}

	/**
	 * Filters the outputs list out of empty and 0 chances.
	 *
	 * @param outputs filtered list
	 */
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

	/**
	 * Refreshes the butcher recipe if the corpse is empty
	 *
	 * @param world world
	 */
	public void refreshButcheringRecipe(World world) {
		if (getCorpseEntity() != null && !outputs.isEmpty() && outputs.get(0).isEmpty() && resetRecipe) {
			Optional<Entity> entity = EntityType.getEntityFromNbt(getCorpseEntity(), world);
			if (entity.isPresent() && !world.isClient()) {
				Optional<ButcheringRecipe> recipe = world.getRecipeManager().listAllOfType(BotDRecipeTypes.BUTCHERING_RECIPE_TYPE)
					.stream().filter(type -> type.entityType() == entity.get().getType()).findFirst();
				recipe.ifPresent(r -> {
					DefaultedList<Pair<ItemStack, Float>> outputsWithChance = DefaultedList.ofSize(r.getOutputs().size(), Pair.of(ItemStack.EMPTY, 1f));
					IntStream.range(0, butcheringRecipe.getOutputs().size()).forEach(i ->
						outputsWithChance.set(i, Pair.of(butcheringRecipe.getOutputs().get(i).getFirst().copy(), butcheringRecipe.getOutputs().get(i).getSecond())));
					craftRecipe(outputsWithChance);
					resetRecipe = false;
				});
			}
		}
	}

	/**
	 * Handles drop rate depending on magic number and item stack list input and removes the item from the next pool
	 *
	 * @param world          world
	 * @param pos            pos
	 * @param magicNumber    chance of successful butcher attempt
	 * @param isNeighbour    if the butcher block is a double block and player is interacting with the recessive one.
	 * @param nonEmptyOutput list of item drop possibilities from butcher recipe
	 */
	private void handleButcheringResult(World world, BlockPos pos, double magicNumber, boolean isNeighbour, List<ItemStack> nonEmptyOutput) {
		if (!decapitateHeadIfPresent(world, isNeighbour, magicNumber)) {
			this.dismemberAtRandom(world);
			ItemScatterer.spawn(world, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, nonEmptyOutput.get(0));
		}

		this.makeFilth(world);

		this.chances.set(0, 0F);
		this.outputs.set(0, ItemStack.EMPTY);
		List<ItemStack> nonEmptyOutputAfter = this.outputs.stream().filter(item -> !item.isEmpty() || !item.isOf(Items.AIR) || item.getCount() != 0).toList();
		if (nonEmptyOutputAfter.isEmpty()) {
			this.reset();
		}
	}

	/**
	 * If the butcher recipe has a head drop, drop the head first before trying to dismember other limbs
	 *
	 * @param world       world
	 * @param isNeighbour if the butcher block is a double block and player is interacting with the recessive one.
	 * @param magicNumber chance of successful butcher attempt
	 * @return if head was successfully decapitated and dropped
	 */
	private boolean decapitateHeadIfPresent(World world, boolean isNeighbour, double magicNumber) {
		if (getHeadVisible() && !isNeighbour && this.butcheringRecipe.headDrop().getFirst() != ItemStack.EMPTY) {
			this.setHeadVisible(false);
			ItemStack head = this.butcheringRecipe.headDrop().getFirst();
			head.setCount(world.getRandom().nextDouble() < magicNumber * this.butcheringRecipe.headDrop().getSecond() ? 1 : 0);
			ItemScatterer.spawn(world, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, head);
			return true;
		}
		return false;
	}

	/**
	 * Calculates a mix between player butcher level, butcher block type and sanitary level of the butcher block.
	 *
	 * @param player      player
	 * @param probability inherited probability of butcher block
	 * @return the chance of successful butchering attempt
	 */
	private double calculateMagicNumber(PlayerEntity player, double probability) {
		double sanitaryModifier = 1.5 - (getFilthLevel() + 1) / 6d;
		float butcherLevel = BotDComponents.PLAYER_COMPONENT.maybeGet(player).map(PlayerDataComponent::getButcheringModifier).orElse(0F);
		return probability + 0.5D * butcherLevel * sanitaryModifier;
	}

	/**
	 * Places a stored corpse from a IHauler on to the Butcher block.
	 *
	 * @param world       world
	 * @param pos         pos
	 * @param state       state
	 * @param player      player who is IHauler
	 * @param isNeighbour if the butcher block is a double block and player is interacting with the recessive one.
	 * @return if place is successful
	 */
	private boolean placeCorpseOnButcherBlock(World world, BlockPos pos, BlockState state, PlayerEntity player, boolean isNeighbour) {
		Optional<IHauler> optionalIHauler = IHauler.of(player);
		if (optionalIHauler.isPresent() && getCorpseEntity().isEmpty()) {
			if (optionalIHauler.get().getCorpseEntity() != null) {
				NbtCompound nbtCompound = optionalIHauler.get().getCorpseEntity();
				if (!nbtCompound.isEmpty()) {
					this.setCorpse(nbtCompound);
					this.setAllVisible();
					optionalIHauler.get().clearCorpseData();

					Direction targetDirection = state.get(FACING).rotateClockwise(Direction.Axis.Y);
					if (!isNeighbour) {
						targetDirection = targetDirection.getOpposite();
					}

					this.spawnMuckParticles((ServerWorld) world, pos);
					this.spawnMuckParticles((ServerWorld) world, pos.offset(targetDirection));

					world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1, 1);
					markDirty();
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Override to trigger when corpse is placed on butcher block
	 *
	 * @param world world
	 * @param pos   pos of muck
	 */
	public void spawnMuckParticles(ServerWorld world, BlockPos pos) {
	}

	/**
	 * To give the butchering an extra oomph, this method plays some sound and spawns some particles.
	 *
	 * @param world          world
	 * @param player         player causing oomph
	 * @param hand           hand used
	 * @param pos            position of ruckus
	 * @param particleOffset particle spawn Y offset.
	 */
	private void makeARuckus(World world, PlayerEntity player, Hand hand, BlockPos pos, double particleOffset) {
		world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_HONEY_BLOCK_BREAK, SoundCategory.PLAYERS, 2, 1);
		world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 0.75f, 1);
		PlayerLookup.tracking(player).forEach(track -> BloodSplashParticlePacket.send(track, pos.getX(), pos.getY() + particleOffset, pos.getZ()));
		BloodSplashParticlePacket.send(player, pos.getX(), pos.getY() + particleOffset, pos.getZ());
		player.swingHand(hand, true);
	}

	/**
	 * Picks a random number between 0 and 3, and set a body part invisible.
	 *
	 * @param world world
	 */
	private void dismemberAtRandom(World world) {
		int i = world.getRandom().nextInt(3);
		switch (i) {
			case 0 -> setLArmVisible(false);
			case 1 -> setRArmVisible(false);
			case 2 -> setLLegVisible(false);
			case 3 -> setRLegVisible(false);
		}
	}

	/**
	 * Resets the Butchering block after a recipe has ended
	 */
	public void reset() {
		this.clear();
		this.setCorpse(new NbtCompound());
		this.butcheringRecipe = null;
		this.resetRecipe = true;
		this.clearCorpseData();
		this.markDirty();
	}

	/**
	 * Override this to add a filth function
	 *
	 * @param world world
	 */
	public void makeFilth(@NotNull World world) {
	}

	/**
	 * Override this to add a filth function
	 *
	 * @return level of filth
	 */
	public int getFilthLevel() {
		return 0;
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, outputs);
		BotDUtils.writeChancesNbt(nbt, chances);
		if (!storedCorpseNbt.isEmpty()) {
			nbt.put(Constants.Nbt.CORPSE_ENTITY, getCorpseEntity());
		}
		nbt.putBoolean(Constants.Nbt.REFRESH, this.resetRecipe);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		Inventories.readNbt(nbt, outputs);
		BotDUtils.readChanceNbt(nbt, chances);
		setCorpse(nbt.getCompound(Constants.Nbt.CORPSE_ENTITY));
		this.resetRecipe = nbt.getBoolean(Constants.Nbt.REFRESH);
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

	public void setCorpse(NbtCompound nbtCompound) {
		this.storedCorpseNbt = nbtCompound;
		if (!nbtCompound.isEmpty() && world != null) {
			Optional<Entity> living = EntityType.getEntityFromNbt(nbtCompound, world);
			if (living.isPresent() && living.get() instanceof LivingEntity livingEntity) {
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
}
