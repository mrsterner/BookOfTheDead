package dev.sterner.book_of_the_dead.common.item;

import dev.sterner.book_of_the_dead.api.block.HorizontalDoubleBlock;
import dev.sterner.book_of_the_dead.api.enums.HorizontalDoubleBlockHalf;
import dev.sterner.book_of_the_dead.common.block.ButcherBlock;
import dev.sterner.book_of_the_dead.common.block.entity.ButcherTableBlockEntity;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class SoapItem extends Item {
	private static final int MAX_USE_TIME = 7200;
	public SoapItem(Settings settings) {
		super(settings.maxDamage(8));
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos blockPos = context.getBlockPos();
		PlayerEntity player = context.getPlayer();
		BlockState state = world.getBlockState(blockPos);
		if(context.getHand() == Hand.MAIN_HAND && world.getBlockState(blockPos).isOf(BotDObjects.BUTCHER_TABLE) && world.getBlockEntity(blockPos) instanceof ButcherTableBlockEntity be){
			if(world.getBlockState(blockPos).get(HorizontalDoubleBlock.HHALF) == HorizontalDoubleBlockHalf.LEFT){
				be = ButcherBlock.getNeighbourButcherBlockEntity(world, state, blockPos);
			}
			if (player != null && be != null && (be.getFilthLevel() > 0) && be.latter <= 0) {
				player.setCurrentHand(context.getHand());
				be.latter = be.latter + 10;
				context.getStack().damage(1, player, p -> p.sendToolBreakStatus(context.getHand()));
				be.markDirty();
				return ActionResult.CONSUME;
			}
		}
		return super.useOnBlock(context);
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return MAX_USE_TIME;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}
}
