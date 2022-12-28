package dev.sterner.book_of_the_dead.client.renderer.block;

import dev.sterner.book_of_the_dead.common.block.entity.PedestalBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class PedestalBlockEntityRenderer implements BlockEntityRenderer<PedestalBlockEntity> {
	private final BlockEntityRenderDispatcher dispatcher;
	private final TextRenderer textRenderer;
	public PedestalBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
		dispatcher = context.getRenderDispatcher();
		textRenderer = context.getTextRenderer();
	}

	@Override
	public void render(PedestalBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if(!entity.getStack().isEmpty() && entity.getWorld() != null) {
			matrices.push();
			double yOffset = MathHelper.sin((entity.getWorld().getTime() + tickDelta) / 10F) / 10D;
			matrices.translate(0.5D, 1.15D + yOffset, 0.5D);
			float angle = (float)((entity.getWorld().getTime() + tickDelta) / 20F + yOffset);
			matrices.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(angle));
			MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(), ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
			matrices.pop();
		}
	}
}
