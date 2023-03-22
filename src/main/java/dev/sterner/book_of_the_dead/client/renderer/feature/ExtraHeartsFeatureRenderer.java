package dev.sterner.book_of_the_dead.client.renderer.feature;

import dev.sterner.book_of_the_dead.client.model.KakuzuEntityModel;
import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;

public class ExtraHeartsFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	private final KakuzuEntityModel model;
	public static final Identifier KAKUZU = Constants.id("textures/entity/kakuzu.png");

	public ExtraHeartsFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context, EntityModelLoader loader) {
		super(context);
		this.model = new KakuzuEntityModel(loader.getModelPart(KakuzuEntityModel.LAYER_LOCATION));
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		BotDComponents.PLAYER_COMPONENT.maybeGet(entity).ifPresent(component -> {
			if(component.getKakuzu() > 0){
				matrices.push();
				matrices.multiply(Axis.X_POSITIVE.rotationDegrees(entity.isInSneakingPose() ? 20 : 0));
				matrices.translate(0, entity.isInSneakingPose() ? 1.70 : 1.55,0.0);
				matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(180f));
				this.model.one.visible = component.getKakuzu() >= 1;
				this.model.two.visible = component.getKakuzu() >= 2;
				this.model.three.visible = component.getKakuzu() >= 3;
				this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(KAKUZU)),light, OverlayTexture.DEFAULT_UV, 1,1,1,1);
				matrices.pop();
			}
		});

	}
}
