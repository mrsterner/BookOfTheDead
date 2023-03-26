package dev.sterner.book_of_the_dead.client.renderer.block;

import dev.sterner.book_of_the_dead.common.block.entity.PedestalBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.MathHelper;

public class PedestalBlockEntityRenderer implements BlockEntityRenderer<PedestalBlockEntity> {
	private final BlockEntityRenderDispatcher dispatcher;
	private final TextRenderer textRenderer;

	public PedestalBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
		dispatcher = context.getRenderDispatcher();
		textRenderer = context.getTextRenderer();
	}

	@Override
	public void render(PedestalBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (!entity.getStack().isEmpty() && entity.getWorld() != null) {
			matrices.push();
			double yOffset = MathHelper.sin((entity.getWorld().getTime() + tickDelta) / 10F) / 10D;
			matrices.translate(0.5D, 1.15D + yOffset, 0.5D);
			float angle = (float) ((entity.getWorld().getTime() + tickDelta) / 20F + yOffset);
			matrices.multiply(Axis.Y_POSITIVE.rotation(angle));
			ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
			itemRenderer.renderItem(entity.getStack(), ModelTransformationMode.GROUND, false, matrices, vertexConsumers, light, overlay, itemRenderer.getHeldItemModel(entity.getStack(), null, null, 0));
			matrices.pop();
		}
	}
}
