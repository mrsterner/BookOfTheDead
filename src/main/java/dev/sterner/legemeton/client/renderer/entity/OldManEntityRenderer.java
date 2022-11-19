package dev.sterner.legemeton.client.renderer.entity;

import dev.sterner.legemeton.client.model.OldManEntityModel;
import dev.sterner.legemeton.common.entity.OldManEntity;
import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.VillagerEntityRenderer;
import net.minecraft.client.render.entity.WanderingTraderEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import net.minecraft.entity.passive.VillagerEntity;
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
