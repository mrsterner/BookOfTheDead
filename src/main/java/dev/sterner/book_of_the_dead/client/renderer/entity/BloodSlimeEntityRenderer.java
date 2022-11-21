package dev.sterner.book_of_the_dead.client.renderer.entity;

import dev.sterner.book_of_the_dead.client.model.BloodSlimeEntityModel;
import dev.sterner.book_of_the_dead.client.renderer.feature.BloodSlimeOverlayFeatureRenderer;
import dev.sterner.book_of_the_dead.common.entity.BloodSlimeEntity;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class BloodSlimeEntityRenderer extends MobEntityRenderer<BloodSlimeEntity, BloodSlimeEntityModel<BloodSlimeEntity>> {
	private static final Identifier TEXTURE = Constants.id("textures/entity/blood_slime.png");

	public BloodSlimeEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new BloodSlimeEntityModel<>(context.getPart(EntityModelLayers.SLIME)), 0.25F);
		this.addFeature(new BloodSlimeOverlayFeatureRenderer<>(this, context.getModelLoader()));
	}

	@Override
	protected void scale(BloodSlimeEntity entity, MatrixStack matrices, float amount) {
		float g = 0.999F;
		matrices.scale(g, g, g);
		matrices.translate(0.0, 0.001F, 0.0);
		float h = 2;
		float i = MathHelper.lerp(amount, entity.lastStretch, entity.stretch) / (h * 0.5F + 1.0F);
		float j = 1.0F / (i + 1.0F);
		matrices.scale(j * h, 1.0F / j * h, j * h);
	}


	@Override
	public Identifier getTexture(BloodSlimeEntity entity) {
		return TEXTURE;
	}
}
