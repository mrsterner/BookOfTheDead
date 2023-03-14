package dev.sterner.book_of_the_dead.api;

import dev.sterner.book_of_the_dead.common.registry.BotDEnchantments;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

public class BotDApi {

	/**
	 * Check if a killed entity should become a corpse or not with the EntityTag, Enchantment and ItemStack
	 * @param livingEntity killed entity
	 * @return true if killed entity should become a corpse
	 */
	public static boolean isButchering(@Nullable LivingEntity livingEntity){
		if(livingEntity == null){
			return false;
		}else if(livingEntity.getAttacker() instanceof PlayerEntity player){
			boolean isInTag = livingEntity.getType().isIn(Constants.Tags.BUTCHERABLE);
			boolean isItem = (player.getMainHandStack().isOf(BotDObjects.BUTCHER_KNIFE) || player.getMainHandStack().isOf(BotDObjects.BLOODY_BUTCHER_KNIFE) || EnchantmentHelper.getLevel(BotDEnchantments.BUTCHERING, player.getMainHandStack()) != 0);
			return isInTag && isItem;
		}
		return false;
	}
}
