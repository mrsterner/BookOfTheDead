package dev.sterner.book_of_the_dead.client.renderer.feature;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.client.model.BloodSlimeEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class BloodSlimeOverlayFeatureRenderer<T extends LivingEntity> extends FeatureRenderer<T, BloodSlimeEntityModel<T>> {
	private final EntityModel<T> model;

	public BloodSlimeOverlayFeatureRenderer(FeatureRendererContext<T, BloodSlimeEntityModel<T>> context, EntityModelLoader loader) {
		super(context);
		this.model = new BloodSlimeEntityModel<>(loader.getModelPart(BloodSlimeEntityModel.OUTER_LAYER_LOCATION));
	}

	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		boolean bl = minecraftClient.hasOutline(livingEntity) && livingEntity.isInvisible();
		if (!livingEntity.isInvisible() || bl) {
			VertexConsumer vertexConsumer;
			if (bl) {
				vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getOutline(this.getTexture(livingEntity)));
			} else {
				vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(livingEntity)));
			}
			matrixStack.translate(0,1.5,0);
			this.getContextModel().copyStateTo(this.model);
			this.model.animateModel(livingEntity, f, g, h);
			this.model.setAngles(livingEntity, f, g, j, k, l);
			this.model.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(livingEntity, 0.0F), 1.0F, 1.0F, 1.0F, 0.75F);
		}
	}
}
