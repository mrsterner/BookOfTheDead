package dev.sterner.book_of_the_dead.common.block.entity;

import dev.sterner.book_of_the_dead.api.block.entity.BaseButcherBlockEntity;
import dev.sterner.book_of_the_dead.api.interfaces.IBlockEntityInventory;
import dev.sterner.book_of_the_dead.api.interfaces.IHauler;
import dev.sterner.book_of_the_dead.common.registry.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ButcherTableBlockEntity extends BaseButcherBlockEntity implements IBlockEntityInventory, IHauler {


	public ButcherTableBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.BUTCHER, pos, state);
	}

	public ActionResult onUse(World world, BlockState state, BlockPos pos, PlayerEntity player, Hand hand, boolean isNeighbour) {
		return onUse(world, state, pos, player, hand, 0.5d, 0d, isNeighbour);
	}
}
