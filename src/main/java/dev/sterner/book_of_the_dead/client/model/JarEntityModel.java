package dev.sterner.book_of_the_dead.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;

public class JarEntityModel extends Model {

	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Constants.id("jar"), "main");
	private final ModelPart cork;
	private final ModelPart jar;

	public JarEntityModel(ModelPart root) {
		super(RenderLayer::getEntityCutoutNoCull);
		this.cork = root.getChild("cork");
		this.jar = root.getChild("jar");
	}

	public static TexturedModelData createBodyLayer() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("cork", ModelPartBuilder.create().uv(22, 22).cuboid(-11.0F, -16.0F, 5.0F, 6.0F, 3.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 8.0F, -8.0F));
		modelPartData.addChild("jar", ModelPartBuilder.create().uv(0, 0).cuboid(-12.0F, -10.0F, 4.0F, 8.0F, 10.0F, 8.0F, new Dilation(0.0F)).uv(24, 0).cuboid(-11.0F, -12.0F, 5.0F, 6.0F, 2.0F, 6.0F, new Dilation(0.0F)).uv(0, 19).cuboid(-11.5F, -14.0F, 4.5F, 7.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 8.0F, -8.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		cork.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		jar.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

	public void renderNoCap(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		jar.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
