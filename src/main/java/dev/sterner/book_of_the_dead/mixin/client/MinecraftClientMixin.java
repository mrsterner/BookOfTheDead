package dev.sterner.book_of_the_dead.mixin.client;

import dev.sterner.book_of_the_dead.client.event.ClientTickHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ClientOnly
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
	@Shadow
	public abstract boolean isPaused();

	@Shadow
	private float pausedTickDelta;

	@Shadow
	public abstract float getTickDelta();

	@Shadow
	@Nullable
	public ClientPlayerEntity player;

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;render(FJZ)V"))
	private void book_of_the_dead$onFrameStart(boolean tick, CallbackInfo ci) {
		ClientTickHandler.renderTick(isPaused() ? pausedTickDelta : getTickDelta());
	}
}
