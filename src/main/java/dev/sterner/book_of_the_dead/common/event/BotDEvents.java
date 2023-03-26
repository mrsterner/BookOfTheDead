package dev.sterner.book_of_the_dead.common.event;

import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.CorpseDataComponent;
import dev.sterner.book_of_the_dead.common.component.PlayerDataComponent;
import dev.sterner.book_of_the_dead.common.registry.BotDConfiguredFeatureRegistry;
import dev.sterner.book_of_the_dead.common.registry.BotDPlacedFeatureRegistry;
import dev.sterner.book_of_the_dead.common.registry.BotDStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.base.api.util.TriState;
import org.quiltmc.qsl.entity.effect.api.StatusEffectEvents;
import org.quiltmc.qsl.entity.effect.api.StatusEffectRemovalReason;
import org.quiltmc.qsl.entity.event.api.EntityReviveEvents;
import org.quiltmc.qsl.entity.event.api.ServerPlayerEntityCopyCallback;
import org.quiltmc.qsl.registry.api.event.DynamicRegistryManagerSetupContext;
import org.quiltmc.qsl.registry.api.event.RegistryEvents;

import java.util.Optional;
import java.util.Set;

public class BotDEvents {
	public static void init() {
		ServerPlayerEntityCopyCallback.EVENT.register(BotDEvents::afterRespawn);
		EntityReviveEvents.BEFORE_TOTEM.register(BotDEvents::tryUseExtraLives);
		StatusEffectEvents.SHOULD_REMOVE.register(BotDEvents::dontRemoveEffect);
		RegistryEvents.DYNAMIC_REGISTRY_SETUP.register(BotDEvents::registerFeatures);
	}

	private static void registerFeatures(@NotNull DynamicRegistryManagerSetupContext ctx) {
		ctx.withRegistries(registries -> {
			Registry<ConfiguredFeature<?, ?>> configured = registries.get(RegistryKeys.CONFIGURED_FEATURE);
			BotDConfiguredFeatureRegistry.init(configured);
			BotDPlacedFeatureRegistry.init(configured, registries);
		}, Set.of(RegistryKeys.PLACED_FEATURE, RegistryKeys.CONFIGURED_FEATURE));
	}

	/**
	 * Soul Siphon and Soul Sickness can only be removed by expiration
	 */
	private static TriState dontRemoveEffect(@NotNull LivingEntity living, @NotNull StatusEffectInstance instance, @NotNull StatusEffectRemovalReason statusEffectRemovalReason) {
		boolean bl = instance.getEffectType().equals(BotDStatusEffects.SOUL_SIPHON) || instance.getEffectType().equals(BotDStatusEffects.SOUL_SICKNESS);
		if (!statusEffectRemovalReason.equals(StatusEffectRemovalReason.EXPIRED) && bl) {
			return TriState.FALSE;
		}
		return TriState.TRUE;
	}

	/**
	 * If the Player has Kakuzu souls, try kill one instead of killing the player.
	 */
	private static boolean tryUseExtraLives(LivingEntity livingEntity, DamageSource damageSource) {
		if (livingEntity instanceof PlayerEntity player) {
			PlayerDataComponent component = BotDComponents.PLAYER_COMPONENT.get(player);
			if (component.getKakuzu() > 0) {
				component.decreaseKakuzuBuffLevel();
				player.setHealth(player.getMaxHealth());
				player.extinguish();
				player.clearStatusEffects();
				player.setVelocity(Vec3d.ZERO);
				player.fallDistance = 0;
				player.setFrozenTicks(0);
				player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, 0.5f, 0.75f);
				return true;
			}
		}
		return false;
	}

	/**
	 * So players don't become corpses on respawn tihi, might now be needed if we change respawn strategy but anyway
	 */
	private static void afterRespawn(ServerPlayerEntity serverPlayerEntity, ServerPlayerEntity serverPlayerEntity1, boolean b) {
		Optional<CorpseDataComponent> component = BotDComponents.CORPSE_COMPONENT.maybeGet(serverPlayerEntity);
		if (component.isPresent() && component.get().isCorpse) {
			component.get().isCorpse(false);
		}
	}
}
