package dev.sterner.book_of_the_dead.mixin;

import net.minecraft.village.VillagerGossips;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(VillagerGossips.class)
public class VillagerGossipsMixin {

	@Inject(method = "startGossip", at = @At(value = "INVOKE", target = "Lnet/minecraft/village/VillagerGossips;getReputationFor(Ljava/util/UUID;)Lnet/minecraft/village/VillagerGossips$Reputation;"))
	private void book_of_the_dead$worsenReputation(){

	}
}
