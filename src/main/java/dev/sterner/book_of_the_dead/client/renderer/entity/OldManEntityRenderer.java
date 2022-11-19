package dev.sterner.book_of_the_dead.client.renderer.entity;

import dev.sterner.book_of_the_dead.client.model.OldManEntityModel;
import dev.sterner.book_of_the_dead.common.entity.OldManEntity;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.util.Identifier;

public class OldManEntityRenderer extends MobEntityRenderer<OldManEntity, OldManEntityModel> {

	public OldManEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new OldManEntityModel(context.getPart(OldManEntityModel.LAYER_LOCATION)), 0.5F);
		this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
	}

	@Override
	public Identifier getTexture(OldManEntity entity) {
		return Constants.id("textures/entity/old_man.png");
	}
}
