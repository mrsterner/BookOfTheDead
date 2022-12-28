package dev.sterner.book_of_the_dead.common.block.entity;

import dev.sterner.book_of_the_dead.api.interfaces.IBlockEntityInventory;
import dev.sterner.book_of_the_dead.api.interfaces.IHauler;
import dev.sterner.book_of_the_dead.common.registry.BotDBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ButcherTableBlockEntity extends BlockEntity implements IBlockEntityInventory {
	public ButcherTableBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.BUTCHER, pos, state);
	}

	public static void tick(World tickerWorld, BlockPos pos, BlockState tickerState, ButcherTableBlockEntity blockEntity) {
	}

	@Override
	public DefaultedList<ItemStack> getItems() {
		return null;
	}

	public ActionResult onUse(World world, BlockState state, BlockPos pos, PlayerEntity player, Hand hand) {
		return ActionResult.PASS;
	}
}
