package dev.sterner.book_of_the_dead.common.block.entity;

import dev.sterner.book_of_the_dead.api.block.entity.BaseBlockEntity;
import dev.sterner.book_of_the_dead.common.recipe.RetortRecipe;
import dev.sterner.book_of_the_dead.common.registry.BotDBlockEntityTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDRecipeTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class RetortFlaskBlockEntity extends BaseBlockEntity implements Inventory {
	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
	public RetortRecipe retortRecipe = null;
	public int color = 0x3f76e4;
	public int heatTimer = 0;
	private boolean loaded = false;
	public boolean hasLiquid = true;

	public RetortFlaskBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.RETORT, pos, state);
	}

	public ActionResult onUse(World world, BlockState state, BlockPos pos, PlayerEntity player, Hand hand) {

		return ActionResult.PASS;
	}

	public static void tick(World world, BlockPos pos, BlockState state, RetortFlaskBlockEntity blockEntity) {
		if (world != null) {
			if (!blockEntity.loaded) {
				blockEntity.markDirty();
				blockEntity.retortRecipe = world.getRecipeManager().listAllOfType(BotDRecipeTypes.RETORT_RECIPE_TYPE).stream().filter(recipe -> recipe.matches(blockEntity, world)).findFirst().orElse(null);
				blockEntity.loaded = true;
			}
			blockEntity.heatTimer = MathHelper.clamp(blockEntity.heatTimer + (state.get(Properties.LIT) && blockEntity.hasLiquid ? 1 : -1), 0, 160);
			if (!world.isClient) {
				if (blockEntity.retortRecipe != null && blockEntity.hasLiquid) {
					blockEntity.heatTimer++;
					if (world.random.nextFloat() <= 0.075f) {
						world.playSound(null, pos, SoundEvents.BLOCK_BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundCategory.BLOCKS, 1 / 3f, 1);
					}



				}
			}
		}
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		Inventories.readNbt(nbt, inventory);
		if (nbt.contains("Color")) {
			color = nbt.getInt("Color");
		}
		heatTimer = nbt.getInt("HeatTimer");
		hasLiquid = nbt.getBoolean("HasLiquid");
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, inventory);
		nbt.putInt("Color", color);
		nbt.putInt("HeatTimer", heatTimer);
		nbt.putBoolean("HasLiquid", hasLiquid);
	}

	public void setColor(int color) {
		if (world != null) {
			this.color = color;
		}
	}

	public void reset() {
		if (world != null) {
			setColor(0x3f76e4);
			clear();
			retortRecipe = null;
		}
	}

	@Override
	public int size() {
		return inventory.size();
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < size(); i++) {
			if (getStack(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStack(int slot) {
		return inventory.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return Inventories.splitStack(inventory, slot, amount);
	}

	@Override
	public ItemStack removeStack(int slot) {
		return Inventories.removeStack(inventory, slot);
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		inventory.set(slot, stack);
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < 16;
	}

	@Override
	public void clear() {
		inventory.clear();
	}


}
