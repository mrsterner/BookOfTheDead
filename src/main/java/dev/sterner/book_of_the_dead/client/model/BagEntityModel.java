package dev.sterner.book_of_the_dead.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;

public class BagEntityModel extends Model {
	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Constants.id("bag"), "main");
	private final ModelPart base;

	public BagEntityModel(ModelPart root) {
		super(RenderLayer::getEntityTranslucent);
		this.base = root.getChild("base");
	}

	public static TexturedModelData createBodyLayer() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		modelPartData.addChild("base", ModelPartBuilder.create().uv(0, 36).cuboid(-7.0F, -14.0F, -7.0F, 14.0F, 14.0F, 14.0F, new Dilation(0.0F))
				.uv(0, 15).cuboid(-6.0F, -15.0F, -6.0F, 12.0F, 1.0F, 12.0F, new Dilation(0.0F))
				.uv(0, 0).cuboid(-6.5F, -17.0F, -6.5F, 13.0F, 2.0F, 13.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		base.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
