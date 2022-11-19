package dev.sterner.book_of_the_dead.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class LargeCircleEntityModel<T extends Entity> extends EntityModel<T> {
	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Constants.id("circle_large"), "main");
	private final ModelPart base;

	public LargeCircleEntityModel(ModelPart root) {
		this.base = root.getChild("base");
	}

	public static TexturedModelData createBodyLayer() {
		ModelData meshDefinition = new ModelData();
		ModelPartData partDefinition = meshDefinition.getRoot();

		ModelPartData base = partDefinition.addChild("base", ModelPartBuilder.create()
				.uv(81, 0)
				.cuboid(-40.0F, 0.0F, -39.2F, 80.0F, 0.0F, 80.0F, new Dilation(0.0F)),
				ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		return TexturedModelData.of(meshDefinition, 512, 512);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		base.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
