package dev.sterner.book_of_the_dead.common.item;

import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class BookOfTheDeadItem extends BlockItem {


	public BookOfTheDeadItem(Block block, Settings settings) {
		super(block, settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		PlayerEntity playerEntity = context.getPlayer();
		if (playerEntity != null && playerEntity.isSneaking() && !cancelPlacement(context.getWorld(), playerEntity)) {
			return super.useOnBlock(context);
		}
		return ActionResult.PASS;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		if (world.isClient && !player.isSneaking()) {
			if (cancelPlacement(world, player)) {
				return TypedActionResult.success(player.getStackInHand(hand));
			}
			BookOfTheDeadScreen.openScreen(player);
			player.swingHand(hand);
			return TypedActionResult.success(player.getStackInHand(hand));
		}
		return TypedActionResult.success(player.getStackInHand(hand));
	}

	public static boolean cancelPlacement(World world, PlayerEntity player) {
		BlockHitResult blockHitResult = raycast(world, player, RaycastContext.FluidHandling.SOURCE_ONLY);
		if (blockHitResult.getType() == HitResult.Type.BLOCK) {
			BlockPos pos = blockHitResult.getBlockPos();
			return world.getBlockState(pos).isOf(BotDObjects.NECRO_TABLE);
		}
		return false;
	}
}
