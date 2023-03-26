package dev.sterner.book_of_the_dead.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;

public class BrainEntityModel extends Model {

	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Constants.id("brain"), "main");
	private final ModelPart brain;

	public BrainEntityModel(ModelPart root) {
		super(RenderLayer::getEntityCutoutNoCull);
		this.brain = root.getChild("brain");
	}

	public static TexturedModelData createBodyLayer() {
		ModelData modelData = new ModelData();
		ModelPartData partData = modelData.getRoot();
		partData.addChild("brain", ModelPartBuilder.create().uv(0, 0).cuboid(-2.5F, -5.0F, -3.0F, 5.0F, 5.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		brain.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
