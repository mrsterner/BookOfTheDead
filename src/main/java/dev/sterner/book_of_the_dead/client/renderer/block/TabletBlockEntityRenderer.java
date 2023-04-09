package dev.sterner.book_of_the_dead.client.renderer.block;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.common.block.BookBlock;
import dev.sterner.book_of_the_dead.common.block.entity.TabletBlockEntity;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.properties.RotationSegment;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;
import org.quiltmc.loader.api.minecraft.ClientOnly;


@ClientOnly
public class TabletBlockEntityRenderer implements BlockEntityRenderer<TabletBlockEntity> {
	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Constants.id("tablet"), "main");
	public static final Identifier TEXTURE = Constants.id("textures/entity/emerald_tablet.png");
	public final TabletModel model;

	public TabletBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		model = new TabletModel(ctx.getLayerModelPart(LAYER_LOCATION));
	}

	@Override
	public void render(TabletBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		BlockState blockState = entity.getCachedState();
		matrices.push();
		if (blockState.getBlock() instanceof BookBlock) {
			matrices.translate(0.5F, 0.5F, 0.5F);
			float h = -RotationSegment.convertToAngle(blockState.get(BookBlock.ROTATION));
			matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(h));
		}
		matrices.scale(1F, -1F, -1F);
		matrices.translate(0, -1, 0);
		model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
		matrices.pop();
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("tablet", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -1.0F, -4.5F, 6.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}

	@ClientOnly
	public static final class TabletModel extends Model {
		public final ModelPart tablet;

		public TabletModel(ModelPart root) {
			super(RenderLayer::getEntityCutoutNoCull);
			this.tablet = root.getChild("tablet");
		}

		@Override
		public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
			this.tablet.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		}
	}
}
