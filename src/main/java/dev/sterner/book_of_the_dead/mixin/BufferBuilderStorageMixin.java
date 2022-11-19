package dev.sterner.book_of_the_dead.mixin;

import com.mojang.blaze3d.vertex.BufferBuilder;
import dev.sterner.book_of_the_dead.client.renderer.renderlayer.BotDRenderLayer;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BufferBuilderStorage.class)
@Environment(EnvType.CLIENT)
public class BufferBuilderStorageMixin {

	@Inject(method = "assignBufferBuilder", at = @At("HEAD"))
	private static void addAllBlack(Object2ObjectLinkedOpenHashMap<RenderLayer, BufferBuilder> mapBuildersIn, RenderLayer renderTypeIn, CallbackInfo callbackInfo) {
		BotDRenderLayer.addGlintTypes(mapBuildersIn);

	}
}
