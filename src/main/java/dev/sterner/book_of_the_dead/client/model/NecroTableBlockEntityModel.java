package dev.sterner.book_of_the_dead.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;

public class NecroTableBlockEntityModel extends Model{
	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Constants.id("necro_model"), "main");
	public final ModelPart base;
	public final ModelPart book;
	public final ModelPart candle;
	public final ModelPart slate;
	public final ModelPart cruse;
	public final ModelPart paper;
	public final ModelPart ink;
	public final ModelPart tablet;

	public NecroTableBlockEntityModel(ModelPart modelPart) {
		super(RenderLayer::getEntityCutoutNoCull);
		this.base = modelPart.getChild("base");
		this.book = modelPart.getChild("book");
		this.candle = modelPart.getChild("candle");
		this.slate = modelPart.getChild("slate");
		this.cruse = modelPart.getChild("cruse");
		this.paper = modelPart.getChild("paper");
		this.ink = modelPart.getChild("ink");
		this.tablet = modelPart.getChild("tablet");
	}

	public static TexturedModelData createBodyLayer() {
		ModelData modelData = new ModelData();
		ModelPartData ModelPartData = modelData.getRoot();

		ModelPartData base = ModelPartData.addChild("base", ModelPartBuilder.create(), ModelTransform.pivot(-8.0F, 24.0F, 0.0F));
		ModelPartData foot = base.addChild("foot", ModelPartBuilder.create().uv(0, 23).cuboid(-12.0F, -3.0F, -8.0F, 24.0F, 3.0F, 16.0F, new Dilation(0.0F)).uv(0, 0).cuboid(-14.0F, -16.0F, -10.0F, 28.0F, 3.0F, 20.0F, new Dilation(0.0F)).uv(0, 59).cuboid(-10.0F, -10.0F, -6.0F, 20.0F, 7.0F, 12.0F, new Dilation(0.0F)).uv(0, 101).cuboid(-10.0F, -10.0F, -6.0F, 20.0F, 7.0F, 12.0F, new Dilation(0.001F)).uv(0, 42).cuboid(-11.0F, -13.0F, -7.0F, 22.0F, 3.0F, 14.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData book = ModelPartData.addChild("book", ModelPartBuilder.create().uv(52, 59).cuboid(-5.0F, 0.0F, -4.0F, 10.0F, 1.0F, 8.0F, new Dilation(0.0F)).uv(64, 68).cuboid(-4.0F, -1.0F, -3.0F, 8.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(-18.5F, 7.0F, -3.8F, 0.0F, -0.4363F, 0.0F));
		ModelPartData candle = ModelPartData.addChild("candle", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		ModelPartData candle1 = candle.addChild("candle1", ModelPartBuilder.create().uv(12, 0).cuboid(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F)).uv(9, 0).cuboid(0.0F, -7.0F, -0.5F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)).uv(0, 11).cuboid(-0.5F, -7.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -16.0F, 4.0F, 0.0F, -0.3054F, 0.0F));
		ModelPartData candle2 = candle.addChild("candle2", ModelPartBuilder.create().uv(0, 9).cuboid(-1.5F, -4.0F, -1.5F, 3.0F, 4.0F, 3.0F, new Dilation(0.0F)).uv(0, 9).cuboid(0.0F, -5.0F, -0.5F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)).uv(9, 9).cuboid(-0.5F, -5.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, -16.0F, 2.5F, 0.0F, 0.1309F, 0.0F));
		ModelPartData candle3 = candle.addChild("candle3", ModelPartBuilder.create().uv(12, 12).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)).uv(0, 8).cuboid(0.0F, -3.0F, -0.5F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)).uv(9, 2).cuboid(-0.5F, -3.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, -16.0F, 1.0F, 0.0F, -0.1309F, 0.0F));
		ModelPartData candle4 = candle.addChild("candle4", ModelPartBuilder.create().uv(0, 23).cuboid(-1.5F, -3.0F, -1.5F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)).uv(0, 1).cuboid(0.0F, -4.0F, -0.5F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)).uv(9, 0).cuboid(-0.5F, -4.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-14.5F, -16.0F, 4.5F, 0.0F, -0.3054F, 0.0F));
		ModelPartData candle5 = candle.addChild("candle5", ModelPartBuilder.create().uv(10, 8).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)).uv(0, 0).cuboid(0.0F, -3.0F, -0.5F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)).uv(0, 0).cuboid(-0.5F, -3.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-14.0F, -16.0F, 2.0F, 0.0F, -0.1309F, 0.0F));
		ModelPartData slate = ModelPartData.addChild("slate", ModelPartBuilder.create().uv(58, 42).cuboid(-12.5F, -17.0F, -1.0F, 9.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		ModelPartData cruse = ModelPartData.addChild("cruse", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -22.0F, 5.0F, 3.0F, 6.0F, 3.0F, new Dilation(0.0F)).uv(0, 16).cuboid(-1.5F, -23.0F, 5.5F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		ModelPartData paper = ModelPartData.addChild("paper", ModelPartBuilder.create().uv(64, 75).cuboid(-11.5F, -16.5F, -8.8F, 4.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		ModelPartData cube_r1 = paper.addChild("cube_r1", ModelPartBuilder.create().uv(64, 68).cuboid(0.0F, -0.5F, -2.0F, 4.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(-6.5F, -16.0F, -5.8F, 0.0F, 0.3054F, 0.0F));
		ModelPartData cube_r2 = paper.addChild("cube_r2", ModelPartBuilder.create().uv(64, 68).cuboid(-2.0F, -0.5F, -2.7F, 4.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(-7.5F, -15.8F, -4.8F, 0.0F, 0.0873F, 0.0F));
		ModelPartData ink = ModelPartData.addChild("ink", ModelPartBuilder.create().uv(80, 61).cuboid(1.0F, -19.0F, -8.0F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)).uv(90, 65).cuboid(1.5F, -20.0F, -7.5F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		ModelPartData cube_r3 = ink.addChild("cube_r3", ModelPartBuilder.create().uv(98, 60).cuboid(-0.5F, -2.5F, 0.0F, 7.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.5F, -22.5F, -6.5F, 0.0F, -0.4363F, 0.0F));
		ModelPartData tablet = ModelPartData.addChild("tablet", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		ModelPartData cube_r4 = tablet.addChild("cube_r4", ModelPartBuilder.create().uv(98, 75).cuboid(-2.0F, 0.5F, -1.5F, 6.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(-21.0F, -17.5F, 3.5F, 0.0F, 0.2618F, 0.0F));

		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {

	}
}
