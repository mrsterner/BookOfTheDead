package dev.sterner.book_of_the_dead.mixin;

import com.mojang.datafixers.util.Pair;
import dev.sterner.book_of_the_dead.client.renderer.shader.BotDShaders;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public class GameRenderMixin {

	@Inject(method = "loadShaders", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", remap = false), locals = LocalCapture.CAPTURE_FAILHARD)
	private void loadShaders(ResourceManager manager, CallbackInfo ci, List<RenderPhase.Shader> list, List<Pair<ShaderProgram, Consumer<ShaderProgram>>> list2)
			throws IOException {
		BotDShaders.init(manager, list2);
	}
}
