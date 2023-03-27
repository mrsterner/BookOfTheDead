package dev.sterner.book_of_the_dead.common.block.entity;

import dev.sterner.book_of_the_dead.api.block.entity.BaseBlockEntity;
import dev.sterner.book_of_the_dead.api.interfaces.IBlockEntityInventory;
import dev.sterner.book_of_the_dead.common.item.StatusEffectItem;
import dev.sterner.book_of_the_dead.common.recipe.RetortRecipe;
import dev.sterner.book_of_the_dead.common.registry.BotDBlockEntityTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDRecipeTypes;
import dev.sterner.book_of_the_dead.common.util.BotDUtils;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class RetortFlaskBlockEntity extends BaseBlockEntity implements IBlockEntityInventory {
	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
	public RetortRecipe retortRecipe = null;
	public int color = 0x3f76e4;
	public int heatTimer = 0;
	public int progress = 0;
	public final int MAX_PROGRESS = 20 * 5;
	private boolean loaded = false;
	public boolean hasLiquid = false;

	public RetortFlaskBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.RETORT, pos, state);
	}

	public ActionResult onUse(World world, BlockState state, BlockPos pos, PlayerEntity player, Hand hand) {
		if (!state.get(Properties.WATERLOGGED)) {
			ItemStack stack = player.getStackInHand(hand);
			if (!stack.isEmpty()) {
				if ((stack.isOf(Items.POTION) && PotionUtil.getPotion(stack).equals(Potions.WATER))) {
					BotDUtils.addItemToInventoryAndConsume(player, hand, Items.GLASS_BOTTLE.getDefaultStack());
					this.hasLiquid = true;
					return ActionResult.CONSUME;
				} else if (stack.isOf(Items.WATER_BUCKET)) {
					BotDUtils.addItemToInventoryAndConsume(player, hand, Items.BUCKET.getDefaultStack());
					this.hasLiquid = true;
					return ActionResult.CONSUME;
				}
				markDirty();
				int firstEmpty = this.getFirstEmptySlot();
				if (firstEmpty != -1) {
					this.setStack(firstEmpty, stack.split(1));
					this.sync(world, pos);
					world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1, 0.5f);
					return ActionResult.CONSUME;
				}
			} else {
				if (progress >= MAX_PROGRESS) {
					for (ItemStack item : inventory) {
						if (item.getItem() instanceof StatusEffectItem) {
							inventory.clear();
						}
					}
					ItemScatterer.spawn(world, pos, inventory);
					world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1, 1);
					this.reset();
				} else {
					if (player.isSneaking()) {
						ItemScatterer.spawn(world, pos, inventory);
						world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1, 1);
					}
				}
			}
		}

		return ActionResult.PASS;
	}

	public void tick(World world, BlockPos pos, BlockState state) {
		if (world != null) {
			if (!loaded) {
				markDirty();
				retortRecipe = world.getRecipeManager().listAllOfType(BotDRecipeTypes.RETORT_RECIPE_TYPE).stream().filter(recipe -> recipe.matches(this, world)).findFirst().orElse(null);
				loaded = true;
			}
			heatTimer = MathHelper.clamp(heatTimer + (state.get(Properties.LIT) && hasLiquid ? 1 : -1), 0, 160);
			if (!world.isClient) {
				if (hasLiquid && heatTimer > 20) {
					if (retortRecipe != null) {
						if (progress < MAX_PROGRESS) {
							progress++;
						}
						if (progress >= MAX_PROGRESS) {
							craft(retortRecipe.output);
							setColor(retortRecipe.color);
						}
					}
					if (world.random.nextFloat() <= 0.075f) {
						world.playSound(null, pos, SoundEvents.BLOCK_BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundCategory.BLOCKS, 1 / 3f, 1);
					}
					markDirty();
				}
			}
		}
	}

	private void craft(ItemStack output) {
		inventory.clear();
		inventory.set(0, output);
		retortRecipe = null;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		Inventories.readNbt(nbt, inventory);
		if (nbt.contains(Constants.Nbt.COLOR)) {
			color = nbt.getInt(Constants.Nbt.COLOR);
		}
		heatTimer = nbt.getInt(Constants.Nbt.HEAT_TIMER);
		progress = nbt.getInt(Constants.Nbt.PROGRESS);
		hasLiquid = nbt.getBoolean(Constants.Nbt.HAS_LIQUID);
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, inventory);
		nbt.putInt(Constants.Nbt.COLOR, color);
		nbt.putInt(Constants.Nbt.HEAT_TIMER, heatTimer);
		nbt.putInt(Constants.Nbt.PROGRESS, progress);
		nbt.putBoolean(Constants.Nbt.HAS_LIQUID, hasLiquid);
	}

	public void setColor(int color) {
		if (world != null) {
			this.color = color;
		}
	}

	public void reset() {
		if (world != null) {
			this.setColor(0x3f76e4);
			this.clear();
			retortRecipe = null;
			progress = 0;
			this.markDirty();
		}
	}

	@Override
	public DefaultedList<ItemStack> getItems() {
		return inventory;
	}
}
