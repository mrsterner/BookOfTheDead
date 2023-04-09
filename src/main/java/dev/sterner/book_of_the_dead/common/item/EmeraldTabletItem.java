package dev.sterner.book_of_the_dead.common.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class EmeraldTabletItem extends BlockItem {
	public EmeraldTabletItem(Block block, Settings settings) {
		super(block, settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		PlayerEntity playerEntity = context.getPlayer();
		if (playerEntity != null && playerEntity.isSneaking() && !BookOfTheDeadItem.cancelPlacement(context.getWorld(), playerEntity)) {
			return super.useOnBlock(context);
		}
		return ActionResult.PASS;
	}
}
