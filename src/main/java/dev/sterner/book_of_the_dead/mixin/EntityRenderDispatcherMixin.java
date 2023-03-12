package dev.sterner.book_of_the_dead.mixin;

import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.CorpseDataComponent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {


	@Inject(method = "renderShadow", at = @At(value = "HEAD"), cancellable = true)
	private static void book_of_the_dead$init(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, float opacity, float tickDelta, WorldView world, float radius, CallbackInfo ci){
		Optional<CorpseDataComponent> component = BotDComponents.CORPSE_COMPONENT.maybeGet(entity);
		if(component.isPresent() && component.get().isCorpse){
			ci.cancel();
		}
	}
}
