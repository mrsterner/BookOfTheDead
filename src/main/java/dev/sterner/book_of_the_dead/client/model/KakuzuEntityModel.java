package dev.sterner.book_of_the_dead.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;

public class KakuzuEntityModel extends Model {
	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Constants.id("kakuzu"), "main");

	private final ModelPart main;
	public final ModelPart one;
	public final ModelPart two;
	public final ModelPart three;

	public KakuzuEntityModel(ModelPart root) {
		super(RenderLayer::getEntityTranslucent);
		this.main = root.getChild("main");
		this.one = main.getChild("one");
		this.two = main.getChild("two");
		this.three = main.getChild("three");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData main = modelPartData.addChild("main", ModelPartBuilder.create(), ModelTransform.of(0.0F, 38.0F, 0.0F, 0.0F, 3.1416F, 0.0F));
		ModelPartData one = main.addChild("one", ModelPartBuilder.create().uv(10, 10).cuboid(-1.5F, -1.9532F, -0.5387F, 3.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-2.4F, -16.0468F, -2.9613F, 0.1309F, 0.1309F, 0.0F));
		one.addChild("cube_r1", ModelPartBuilder.create().uv(16, 12).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.9532F, -0.0387F, -0.4363F, 0.0F, 0.0F));
		ModelPartData two = main.addChild("two", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		two.addChild("cube_r2", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, -1.5F, -1.5F, 4.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(1.8F, -17.5F, -2.5F, 0.0F, -0.1745F, -0.0873F));
		ModelPartData three = main.addChild("three", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		three.addChild("cube_r3", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, -21.5F, -2.5F, -0.0436F, 0.1309F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		one.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		two.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		three.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
