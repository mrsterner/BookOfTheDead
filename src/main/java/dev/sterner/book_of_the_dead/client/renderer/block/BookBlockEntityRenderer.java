package dev.sterner.book_of_the_dead.client.renderer.block;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.common.block.BookBlock;
import dev.sterner.book_of_the_dead.common.block.entity.BookBlockEntity;
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
public class BookBlockEntityRenderer implements BlockEntityRenderer<BookBlockEntity> {
	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Constants.id("book"), "main");
	public static final Identifier TEXTURE = Constants.id("textures/entity/book.png");
	public final BookModel model;

	public BookBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		model = new BookModel(ctx.getLayerModelPart(LAYER_LOCATION));
	}

	@Override
	public void render(BookBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		BlockState blockState = entity.getCachedState();
		matrices.push();
		if (blockState.getBlock() instanceof BookBlock) {
			matrices.translate(0.5F, 0.5F, 0.5F);
			float h = -RotationSegment.convertToAngle(blockState.get(BookBlock.ROTATION));
			matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(h));
		}
		matrices.scale(1F, -1F, -1F);
		matrices.translate(0,-1,0);
		model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1,1,1,1);
		matrices.pop();
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("book", ModelPartBuilder.create().uv(0, 0).cuboid(-5.5F, 0.0F, -3.2F, 10.0F, 1.0F, 8.0F, new Dilation(0.0F)).uv(0, 9).cuboid(-4.5F, -1.0F, -2.2F, 8.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, 23.0F, -0.8F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@ClientOnly
	public static final class BookModel extends Model {
		public final ModelPart book;

		public BookModel(ModelPart root) {
			super(RenderLayer::getEntityCutoutNoCull);
			this.book = root.getChild("book");
		}

		@Override
		public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
			this.book.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		}
	}
}
