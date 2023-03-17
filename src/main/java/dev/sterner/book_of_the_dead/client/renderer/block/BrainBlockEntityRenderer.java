package dev.sterner.book_of_the_dead.client.renderer.block;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.common.block.entity.BrainBlockEntity;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;
import net.minecraft.world.World;

public class BrainBlockEntityRenderer implements BlockEntityRenderer<BrainBlockEntity> {
	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Constants.id("brain_model"), "main");
	private final Identifier TEXTURE = Constants.id("textures/entity/brain.png");
	private final ModelPart main;

	public BrainBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		ModelPart modelPart = ctx.getLayerModelPart(LAYER_LOCATION);
		this.main = modelPart.getChild("main");
	}

	@Override
	public void render(BrainBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = entity.getWorld();
		if(world != null){
			BlockState blockState = entity.getCachedState();
			matrices.push();
			float f = blockState.get(HorizontalFacingBlock.FACING).asRotation();
			matrices.translate(0.5, 1.5, 0.5);
			matrices.scale(-1.0F, -1.0F, 1.0F);
			matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(-f));

			render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)), light, overlay);
			matrices.pop();
		}
	}

	public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay) {
		main.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData main = modelPartData.addChild("main", ModelPartBuilder.create().uv(0, 0).cuboid(-2.5F, -5.0F, -3.0F, 5.0F, 5.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}
}
