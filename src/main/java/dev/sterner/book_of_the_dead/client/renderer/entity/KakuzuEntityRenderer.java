package dev.sterner.book_of_the_dead.client.renderer.entity;

import dev.sterner.book_of_the_dead.client.model.KakuzuLivingEntityModel;
import dev.sterner.book_of_the_dead.common.entity.KakuzuEntity;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class KakuzuEntityRenderer extends MobEntityRenderer<KakuzuEntity, KakuzuLivingEntityModel> {
	private static final Identifier TEXTURE = Constants.id("textures/entity/kakuzu.png");

	public KakuzuEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new KakuzuLivingEntityModel(context.getPart(KakuzuLivingEntityModel.LAYER_LOCATION)), 0.1f);
	}

	@Override
	public void render(KakuzuEntity mobEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		if(mobEntity != null && mobEntity.world != null){
			this.model.one.visible = false;
			this.model.two.visible = false;
			this.model.three.visible = false;
			switch (mobEntity.getVariant()) {
				case 2 -> this.model.two.visible = true;
				case 3 -> this.model.three.visible = true;
				default -> this.model.one.visible = true;
			}
		}
		super.render(mobEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
	}

	@Nullable
	@Override
	protected RenderLayer getRenderLayer(KakuzuEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
		return super.getRenderLayer(entity, showBody, true, showOutline);
	}

	@Override
	public Identifier getTexture(KakuzuEntity entity) {
		return TEXTURE;
	}
}
