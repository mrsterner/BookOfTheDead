package dev.sterner.book_of_the_dead.mixin;

import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.PlayerAbilityData;
import dev.sterner.book_of_the_dead.common.component.PlayerDataComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityInteraction;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.village.VillageGossipType;
import net.minecraft.village.VillagerGossips;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin {

	@Shadow
	@Final
	private VillagerGossips gossip;

	@Inject(method = "onInteractionWith", at = @At(value = "HEAD"), cancellable = true)
	private void book_of_the_dead$worsenReputation(EntityInteraction interaction, Entity entity, CallbackInfo ci) {
		if (entity instanceof PlayerEntity player) {
			PlayerDataComponent component = BotDComponents.PLAYER_COMPONENT.get(player);
			if (component.getReputationDebuffModifier() < PlayerAbilityData.REPUTATION[0]) {
				float positiveMod = component.getReputationDebuffModifier();
				float negativeMod = 2 - positiveMod;

				if (interaction == EntityInteraction.ZOMBIE_VILLAGER_CURED) {
					this.gossip.startGossip(entity.getUuid(), VillageGossipType.MAJOR_POSITIVE, (int) (20 * positiveMod));
					this.gossip.startGossip(entity.getUuid(), VillageGossipType.MINOR_POSITIVE, (int) (25 * positiveMod));
				} else if (interaction == EntityInteraction.TRADE) {
					this.gossip.startGossip(entity.getUuid(), VillageGossipType.TRADING, 2);
				} else if (interaction == EntityInteraction.VILLAGER_HURT) {
					this.gossip.startGossip(entity.getUuid(), VillageGossipType.MINOR_NEGATIVE, (int) (25 * negativeMod));
				} else if (interaction == EntityInteraction.VILLAGER_KILLED) {
					this.gossip.startGossip(entity.getUuid(), VillageGossipType.MAJOR_NEGATIVE, (int) (25 * negativeMod));
				}
				ci.cancel();
			}
		}
	}
}
