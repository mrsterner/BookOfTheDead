package dev.sterner.book_of_the_dead.common.item;

import dev.sterner.book_of_the_dead.common.block.ReinforcedBlock;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static dev.sterner.book_of_the_dead.common.block.ReinforcedBlock.REINFORCED_TYPE;

public class CellarKeyItem extends Item {
	private static final int MAX_USE_TIME = 80;

	public CellarKeyItem(Item.Settings settings) {
		super(settings);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		super.finishUsing(stack, world, user);
		if (user instanceof ServerPlayerEntity serverPlayerEntity) {
			Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
			serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
		}
		boolean consume = false;
		if (!world.isClient) {
			consume = this.getAndConvertReinforcedBlocks(world, user);
			if (consume) {
				world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.1f, (user.getRandom().nextFloat() - user.getRandom().nextFloat()) * 0.35F + 0.9F);
			} else {
				world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS);
			}
		}
		return consume ? ItemStack.EMPTY : this.getDefaultStack();
	}

	private boolean getAndConvertReinforcedBlocks(World world, LivingEntity user) {
		boolean consumeKey = false;
		int range = 4;
		for (int x = -range; x < range; x++) {
			for (int y = -range; y < range; y++) {
				for (int z = -range; z < range; z++) {
					BlockPos blockPos = user.getBlockPos().add(x, y, z);
					BlockState checkedState = world.getBlockState(blockPos);
					if (checkedState.isOf(BotDObjects.REINFORCED_BLOCK)) {
						if (checkedState.get(REINFORCED_TYPE) == ReinforcedBlock.ReinforcedType.DEEPSLATE_TILES) {
							world.setBlockState(blockPos, Blocks.DEEPSLATE_TILES.getDefaultState());
						} else if (checkedState.get(REINFORCED_TYPE) == ReinforcedBlock.ReinforcedType.SPRUCE_PLANKS) {
							world.setBlockState(blockPos, Blocks.SPRUCE_PLANKS.getDefaultState());
						}
						consumeKey = true;
					} else if (checkedState.isOf(BotDObjects.REINFORCED_DOOR)) {
						if (checkedState.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
							world.breakBlock(blockPos, true, null);
							consumeKey = true;
						}

					}
				}
			}
		}
		return consumeKey;
	}


	@Override
	public int getMaxUseTime(ItemStack stack) {
		return MAX_USE_TIME;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ItemUsage.consumeHeldItem(world, user, hand);
	}
}
