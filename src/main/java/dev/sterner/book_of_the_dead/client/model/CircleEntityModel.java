package dev.sterner.book_of_the_dead.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;

public class CircleEntityModel extends Model {
	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Constants.id("circle_model"), "main");
	private final ModelPart main;

	public CircleEntityModel(ModelPart root){
		super(RenderLayer::getEntityTranslucent);
		this.main = root.getChild("main");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData main = modelPartData.addChild("main", ModelPartBuilder.create().uv(-80, 0).cuboid(-40.0F, 0.0F, -40.0F, 80.0F, 0.0F, 80.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		main.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
