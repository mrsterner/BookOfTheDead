package dev.sterner.book_of_the_dead.mixin.client;

import dev.sterner.book_of_the_dead.client.registry.BotDRenderLayer;
import dev.sterner.book_of_the_dead.common.item.AllBlackSwordItem;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
final class ItemRendererMixin {
	@Shadow
	@Final
	private ItemModels models;

	@Inject(method = "getHeldItemModel", at = @At("HEAD"), cancellable = true)
	private void book_of_the_dead$getHeldItemModel(ItemStack stack, World world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir) {
		if (stack.getItem() instanceof AllBlackSwordItem) {
			BakedModel bakedModel = models.getModelManager().getModel(new ModelIdentifier("minecraft", "trident_in_hand", "inventory")); // this is the model type (not the texture), its insane that copy-pasting this works first try
			ClientWorld clientWorld = world instanceof ClientWorld ? (ClientWorld) world : null;
			BakedModel bakedModel2 = bakedModel.getOverrides().apply(bakedModel, stack, clientWorld, entity, seed);
			cir.setReturnValue(bakedModel2 == null ? this.models.getModelManager().getMissingModel() : bakedModel2);
		}
	}

	@Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At("HEAD"))
	private void book_of_the_dead$setAllBlack(ItemStack stack, ModelTransformationMode modelTransformationMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
		BotDRenderLayer.setTargetStack(stack);
	}

	@Redirect(method = "getItemGlintConsumer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;getGlint()Lnet/minecraft/client/render/RenderLayer;"))
	private static RenderLayer book_of_the_dead$getGlint() {
		return BotDRenderLayer.getGlint();
	}

	@Redirect(method = "getDirectItemGlintConsumer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;getDirectGlint()Lnet/minecraft/client/render/RenderLayer;"))
	private static RenderLayer book_of_the_dead$getGlintDirect() {
		return BotDRenderLayer.getGlintDirect();
	}
}
