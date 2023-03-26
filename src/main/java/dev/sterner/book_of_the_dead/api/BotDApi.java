package dev.sterner.book_of_the_dead.api;

import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.LivingEntityDataComponent;
import dev.sterner.book_of_the_dead.common.component.PlayerDataComponent;
import dev.sterner.book_of_the_dead.common.entity.KakuzuEntity;
import dev.sterner.book_of_the_dead.common.registry.BotDEnchantments;
import dev.sterner.book_of_the_dead.common.registry.BotDEntityTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

public class BotDApi {

	/**
	 * Check if a killed entity should become a corpse or not with the EntityTag, Enchantment and ItemStack
	 *
	 * @param livingEntity killed entity
	 * @return true if killed entity should become a corpse
	 */
	public static boolean isButchering(@Nullable LivingEntity livingEntity) {
		if (livingEntity == null) {
			return false;
		} else if (livingEntity.getAttacker() instanceof PlayerEntity player) {
			boolean isInTag = livingEntity.getType().isIn(Constants.Tags.BUTCHERABLE);
			boolean isItem = (player.getMainHandStack().isOf(BotDObjects.MEAT_CLEAVER) || EnchantmentHelper.getLevel(BotDEnchantments.BUTCHERING, player.getMainHandStack()) != 0);
			return isInTag && isItem;
		}
		return false;
	}

	/**
	 * If the player is capable of spawning a kakuzu, spawn a kakuzu
	 *
	 * @param player player who is spawning
	 */
	public static void spawnKakuzuFromPlayer(PlayerEntity player) {
		PlayerDataComponent component = BotDComponents.PLAYER_COMPONENT.get(player);
		if (component.getKakuzu() > 0 && component.getDispatchedKakuzuMinions() < 3) {
			component.decreaseKakuzuBuffLevel();
			component.increaseDispatchedMinionBuffLevel();
			KakuzuEntity kakuzuEntity = new KakuzuEntity(BotDEntityTypes.KAKUZU_ENTITY, player.world);
			kakuzuEntity.setOwner(player.getUuid());
			kakuzuEntity.setVariant(component.getKakuzu());
			kakuzuEntity.refreshPositionAndAngles(player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
			player.world.spawnEntity(kakuzuEntity);
		}
	}

	/**
	 * If the player is capable of fusing with its dispatched kakuzu, it will, and it will discard the kakuzu
	 *
	 * @param player       player to fuse with
	 * @param kakuzuEntity kakuzu to enter the player
	 */
	public static void pickupKakuzu(PlayerEntity player, KakuzuEntity kakuzuEntity) {
		PlayerDataComponent component = BotDComponents.PLAYER_COMPONENT.get(player);
		if (component.getKakuzu() < 3 && component.getDispatchedKakuzuMinions() > 0) {
			component.increaseKakuzuBuffLevel();
			component.decreaseDispatchedMinionBuffLevel();
			kakuzuEntity.remove(Entity.RemovalReason.DISCARDED);
		}
	}

	/**
	 * Determines if the player can have Kakuzu immortality or not
	 *
	 * @param player player
	 * @return if the player already is a lich or has an entanglement, returns false
	 */
	public static boolean canHaveKakuzu(PlayerEntity player) {
		PlayerDataComponent component = BotDComponents.PLAYER_COMPONENT.get(player);
		return !component.getLich() && !component.getEntangled() && component.getKakuzu() < 3 && component.getDispatchedKakuzuMinions() < 3;
	}

	/**
	 * Determines if the player can have Lichdom immortality or not
	 *
	 * @param player player
	 * @return if the player already has kakuzu or has an entanglement, returns false
	 */
	public static boolean canHaveLichdom(PlayerEntity player) {
		PlayerDataComponent component = BotDComponents.PLAYER_COMPONENT.get(player);
		return component.getKakuzu() <= 0 && !component.getEntangled() && !component.getLich();
	}

	/**
	 * Determines if the player can have entanglement immortality or not
	 *
	 * @param living living
	 * @return if the player already has kakuzu or is a lich, returns false, living entities can always be entangled
	 */
	public static boolean canEntangle(LivingEntity living) {
		if (living instanceof PlayerEntity player) {
			PlayerDataComponent component = BotDComponents.PLAYER_COMPONENT.get(player);
			return component.getKakuzu() <= 0 && !component.getLich() && !component.getEntangled();
		}
		LivingEntityDataComponent livingEntityDataComponent = BotDComponents.LIVING_COMPONENT.get(living);
		return livingEntityDataComponent.getEntangledEntityId() == 0;
	}
}
