package dev.sterner.book_of_the_dead.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class JarEntityModel extends Model{

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
		ModelPartData partData = modelData.getRoot();

		partData.addChild("cork", ModelPartBuilder.create().uv(0, 23).cuboid(-11.0F, -16.0F, 5.0F, 6.0F, 3.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 8.0F, -8.0F));
		partData.addChild("jar", ModelPartBuilder.create().uv(0, 0).cuboid(-12.0F, -11.0F, 4.0F, 8.0F, 11.0F, 8.0F, new Dilation(0.0F)).uv(0, 55).cuboid(-11.0F, -12.0F, 5.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F)).uv(0, 32).cuboid(-11.5F, -14.0F, 4.5F, 7.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 8.0F, -8.0F));

		return TexturedModelData.of(modelData, 32, 64);
	}

	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		cork.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		jar.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

	public void renderNoCap(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		jar.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
