package dev.sterner.legemeton.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.legemeton.common.entity.OldManEntity;
import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class OldManEntityModel extends BipedEntityModel<OldManEntity> {
	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Constants.id("old_man"), "main");
	private final ModelPart head;
	private final ModelPart nose;
	private final ModelPart body;
	private final ModelPart leg0;
	private final ModelPart leg1;
	private final ModelPart arms;

	public OldManEntityModel(ModelPart root) {
		super(root);
		this.head = root.getChild(EntityModelPartNames.HEAD);
		this.nose = head.getChild("nose");
		this.body = root.getChild(EntityModelPartNames.BODY);
		this.leg0 = root.getChild(EntityModelPartNames.LEFT_LEG);
		this.leg1 = root.getChild(EntityModelPartNames.RIGHT_LEG);
		this.arms = root.getChild("arms");
	}

	public static TexturedModelData createBodyLayer() {
		ModelData data = new ModelData();
		ModelPartData root = data.getRoot();
		ModelPartData head = root.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new Dilation(0.0F)).mirrored(false).uv(34, 51).cuboid(-3.0F, -2.0F, -5.0F, 6.0F, 4.0F, 2.0F, new Dilation(0.0F)).uv(32, 3).cuboid(-4.0F, -7.0F, -4.0F, 8.0F, 6.0F, 8.0F, new Dilation(0.3F)).uv(34, 53).cuboid(-2.0F, 2.0F, -5.0F, 4.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData nose = head.addChild("nose", ModelPartBuilder.create().uv(24, 0).mirrored().cuboid(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, -2.0F, 0.0F));
		ModelPartData body = root.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(16, 20).mirrored().cuboid(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, new Dilation(0.0F)).mirrored(false).uv(0, 38).mirrored().cuboid(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, new Dilation(0.5F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData leg0 = root.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create().uv(0, 22).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(2.0F, 12.0F, 0.0F));
		ModelPartData leg1 = root.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create().uv(0, 22).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-2.0F, 12.0F, 0.0F));
		ModelPartData arms = root.addChild("arms", ModelPartBuilder.create().uv(40, 38).mirrored().cuboid(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, new Dilation(0.0F)).mirrored(false).uv(44, 22).mirrored().cuboid(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)).mirrored(false).uv(44, 22).mirrored().cuboid(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 2.0F, 0.0F));
		root.addChild(EntityModelPartNames.HAT, ModelPartBuilder.create(), ModelTransform.NONE);
		root.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create(), ModelTransform.NONE);
		root.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create(), ModelTransform.NONE);
		return TexturedModelData.of(data, 64, 64);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		arms.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

	@Override
	public void setAngles(OldManEntity livingEntity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		boolean bl = false;
		if (livingEntity != null) {
			bl = livingEntity.getHeadRollingTimeLeft() > 0;
		}
		this.arms.pitch = -0.65F;
		this.head.yaw = headYaw * (float) (Math.PI / 180.0);
		this.head.pitch = headPitch * (float) (Math.PI / 180.0);
		if (bl) {
			this.head.roll = 0.3F * MathHelper.sin(0.45F * animationProgress);
			this.head.pitch = 0.4F;
		} else {
			this.head.roll = 0.0F;
		}

		this.rightLeg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance * 0.5F;
		this.leftLeg.pitch = MathHelper.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance * 0.5F;
		this.rightLeg.yaw = 0.0F;
		this.leftLeg.yaw = 0.0F;
	}
}
