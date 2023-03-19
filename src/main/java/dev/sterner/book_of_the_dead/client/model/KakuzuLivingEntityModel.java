package dev.sterner.book_of_the_dead.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.common.entity.KakuzuEntity;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.MathHelper;

public class KakuzuLivingEntityModel extends EntityModel<KakuzuEntity> implements ModelWithArms, ModelWithHead {
	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Constants.id("kakuzu_living"), "main");

	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart lArm;
	private final ModelPart rArm;
	public final ModelPart one;
	public final ModelPart two;
	public final ModelPart three;

	public KakuzuLivingEntityModel(ModelPart root) {
		this.body = root.getChild("body");
		this.lArm = body.getChild("lArm");
		this.rArm = body.getChild("rArm");
		this.head = body.getChild("head");
		this.one = head.getChild("one");
		this.two = head.getChild("two");
		this.three = head.getChild("three");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(20, 0).cuboid(-1.0F, -6.0F, -2.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(20, 7).cuboid(-1.5F, -3.0F, -1.5F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -5.5F, -0.5F));
		ModelPartData one = head.addChild("one", ModelPartBuilder.create().uv(10, 10).cuboid(-1.5F, -1.9532F, -0.5387F, 3.0F, 4.0F, 2.0F, new Dilation(0.01F)), ModelTransform.pivot(0.0F, -1.5468F, -1.9613F));
		one.addChild("cube_r1", ModelPartBuilder.create().uv(16, 12).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.01F)), ModelTransform.of(0.0F, -0.9532F, -0.0387F, -0.4363F, 0.0F, 0.0F));
		head.addChild("two", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, -1.5F, -1.5F, 4.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.6F, -2.0F));
		head.addChild("three", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.5F, -1.5F));
		body.addChild("lArm", ModelPartBuilder.create().uv(28, 0).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.5F, -5.0F, -1.0F, 0.0F, 0.0F, -0.1309F));
		body.addChild("rArm", ModelPartBuilder.create().uv(28, 0).mirrored().cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-1.5F, -5.0F, -1.0F, 0.0F, 0.0F, 0.1309F));

		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void setArmAngle(Arm arm, MatrixStack matrices) {
		this.body.rotate(matrices);
		matrices.translate(0.0F, 0.0625F, 0.1875F);
		matrices.multiply(Axis.X_POSITIVE.rotation(this.rArm.pitch));
		matrices.scale(0.7F, 0.7F, 0.7F);
		matrices.translate(0.0625F, 0.0F, 0.0F);
	}

	@Override
	public ModelPart getHead() {
		return this.head;
	}

	@Override
	public void setAngles(KakuzuEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		float n = animationProgress * 9.0F * (float) (Math.PI / 180.0);
		float p = 1.0F - Math.min(limbDistance / 0.3F, 1.0F);

		this.head.pitch = headPitch * (float) (Math.PI / 180.0);
		this.head.yaw = headYaw * (float) (Math.PI / 180.0);

		float t = 0.3F - MathHelper.cos(n + ((float) (Math.PI * 3.0 / 2.0))) * (float) Math.PI * 0.05F * p;
		this.lArm.roll = -t;
		this.rArm.roll = t;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		body.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
