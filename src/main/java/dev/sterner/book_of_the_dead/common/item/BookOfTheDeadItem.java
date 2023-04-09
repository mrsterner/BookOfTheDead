package dev.sterner.book_of_the_dead.common.item;

import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BookOfTheDeadItem extends BlockItem {


	public BookOfTheDeadItem(Block block, Settings settings) {
		super(block, settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		PlayerEntity playerEntity = context.getPlayer();
		if(playerEntity != null && playerEntity.isSneaking()){
			return super.useOnBlock(context);
		}
		return ActionResult.PASS;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		if (world.isClient && !player.isSneaking()) {
			BookOfTheDeadScreen.openScreen(player);
			player.swingHand(hand);
			return TypedActionResult.success(player.getStackInHand(hand));
		}
		return TypedActionResult.success(player.getStackInHand(hand));
	}
}
