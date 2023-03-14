package dev.sterner.book_of_the_dead.mixin.client;

import dev.sterner.book_of_the_dead.common.registry.BotDSpriteIdentifiers;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;


@ClientOnly
@Mixin(TexturedRenderLayers.class)
public class TexturedRenderLayersMixin {
	@Inject(method = "addDefaultTextures", at = @At("HEAD"))
	private static void book_of_the_dead$addDefaultTextures(Consumer<SpriteIdentifier> consumer, CallbackInfo info) {
		for (SpriteIdentifier identifier : BotDSpriteIdentifiers.INSTANCE.getIdentifiers()) {
			consumer.accept(identifier);
		}
	}
}
