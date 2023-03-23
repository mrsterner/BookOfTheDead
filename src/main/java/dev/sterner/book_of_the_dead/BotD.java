package dev.sterner.book_of_the_dead;

import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.CorpseDataComponent;
import dev.sterner.book_of_the_dead.common.component.PlayerDataComponent;
import dev.sterner.book_of_the_dead.common.event.BotDItemGroupEvents;
import dev.sterner.book_of_the_dead.common.event.BotDUseEvents;
import dev.sterner.book_of_the_dead.common.registry.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.base.api.util.TriState;
import org.quiltmc.qsl.entity.effect.api.StatusEffectEvents;
import org.quiltmc.qsl.entity.effect.api.StatusEffectRemovalReason;
import org.quiltmc.qsl.entity.event.api.EntityReviveEvents;
import org.quiltmc.qsl.entity.event.api.ServerPlayerEntityCopyCallback;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

import java.util.Optional;

public class BotD implements ModInitializer {

	static final boolean DEBUG_MODE = true;

	public static boolean isDebugMode() {
		return DEBUG_MODE && QuiltLoader.isDevelopmentEnvironment();
	}

	@Override
	public void onInitialize(ModContainer mod) {
		BotDObjects.init();
		BotDEntityTypes.init();
		BotDBlockEntityTypes.init();
		BotDEnchantments.init();
		BotDStatusEffects.init();
		BotDRecipeTypes.init();
		BotDWorldGenerators.init();
		BotDTrades.init();
		BotDRituals.init();
		BotDSoundEvents.init();
		BotDMemoryModuleTypes.init();
		BotDSensorTypes.init();
		BotDDamageTypes.init();

		BotDUseEvents.init();
		BotDItemGroupEvents.init();

		ServerPlayerEntityCopyCallback.EVENT.register(this::afterRespawn);
		EntityReviveEvents.BEFORE_TOTEM.register(this::tryUseExtraLives);
		StatusEffectEvents.SHOULD_REMOVE.register(this::dontRemoveEffect);
	}

	/**
	 * Soul Siphon and Soul Sickness can only be removed by expiration
	 */
	private TriState dontRemoveEffect(@NotNull LivingEntity living, @NotNull StatusEffectInstance instance, @NotNull StatusEffectRemovalReason statusEffectRemovalReason) {
		boolean bl = instance.getEffectType().equals(BotDStatusEffects.SOUL_SIPHON) || instance.getEffectType().equals(BotDStatusEffects.SOUL_SICKNESS);
		if(!statusEffectRemovalReason.equals(StatusEffectRemovalReason.EXPIRED) && bl){
			return TriState.FALSE;
		}
		return TriState.TRUE;
	}

	/**
	 * If the Player has Kakuzu souls, try kill one instead of killing the player.
	 */
	private boolean tryUseExtraLives(LivingEntity livingEntity, DamageSource damageSource) {
		if(livingEntity instanceof PlayerEntity player){
			PlayerDataComponent component = BotDComponents.PLAYER_COMPONENT.get(player);
			if(component.getKakuzu() > 0){
				component.decreaseKakuzuBuffLevel();
				player.setHealth(player.getMaxHealth());
				player.extinguish();
				player.clearStatusEffects();
				player.setVelocity(Vec3d.ZERO);
				player.fallDistance = 0;
				player.setFrozenTicks(0);
				player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, 0.5f,0.75f);
				return true;
			}
		}
		return false;
	}

	/**
	 * So players don't become corpses on respawn tihi, might now be needed if we change respawn strategy but anyway
	 */
	private void afterRespawn(ServerPlayerEntity serverPlayerEntity, ServerPlayerEntity serverPlayerEntity1, boolean b) {
		Optional<CorpseDataComponent> component = BotDComponents.CORPSE_COMPONENT.maybeGet(serverPlayerEntity);
		if(component.isPresent() && component.get().isCorpse){
			component.get().isCorpse(false);
		}
	}
}
