package dev.sterner.book_of_the_dead.mixin.reach;

import dev.sterner.book_of_the_dead.common.registry.BotDEntityAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerInteractionWithEntityC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/server/network/ServerPlayNetworkHandler$C_wsexhymd")
public abstract class ServerPlayNetworkHandlerMixin implements PlayerInteractionWithEntityC2SPacket.Handler {
	@Shadow(aliases = "field_28963") @Final private ServerPlayNetworkHandler field_28963;
	@Shadow(aliases = "field_28962") @Final private Entity field_28962;

	@Inject(method = "attack()V", at = @At("HEAD"), require = 1, allow = 1, cancellable = true)
	public void book_of_the_dead$onPlayerInteractEntity(CallbackInfo info) {
		if (!BotDEntityAttributeRegistry.isWithinAttackRange(this.field_28963.player, this.field_28962)) {
			info.cancel();
		}
	}
}
