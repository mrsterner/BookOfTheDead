package dev.sterner.book_of_the_dead.mixin;

import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.CorpseDataComponent;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {

	@Shadow
	protected M model;

	protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Inject(method = "<init>", at = @At(value = "TAIL"))
	private void book_of_the_dead$init(EntityRendererFactory.Context ctx, EntityModel model, float shadowRadius, CallbackInfo ci){

	}

	@Inject(method = "getOverlay", at = @At("HEAD"), cancellable = true)
	private static void book_of_the_dead$getOverlay(LivingEntity entity, float whiteOverlayProgress, CallbackInfoReturnable<Integer> cir){
		Optional<CorpseDataComponent> component = BotDComponents.CORPSE_COMPONENT.maybeGet(entity);
		if(component.isPresent() && component.get().isCorpse){
			cir.setReturnValue(OverlayTexture.packUv(OverlayTexture.getU(whiteOverlayProgress), OverlayTexture.getV(false)));
		}
	}
}
