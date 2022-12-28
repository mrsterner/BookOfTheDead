package dev.sterner.book_of_the_dead.client.renderer.block;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.BotDClient;
import dev.sterner.book_of_the_dead.client.model.LargeCircleEntityModel;
import dev.sterner.book_of_the_dead.client.renderer.renderlayer.BotDRenderLayer;
import dev.sterner.book_of_the_dead.common.block.RitualBlock;
import dev.sterner.book_of_the_dead.common.block.entity.RitualBlockEntity;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class RitualBlockEntityRenderer implements BlockEntityRenderer<RitualBlockEntity> {
	private final Identifier CIRCLE_TEXTURE = Constants.id("textures/entity/circle.png");
	private final LargeCircleEntityModel<Entity> circleEntityModel =  new LargeCircleEntityModel<>(LargeCircleEntityModel.createBodyLayer().createModel());

	public RitualBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
	}

	@Override
	public void render(RitualBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		matrices.translate(0.5,-1.25,0.5);
		final float rotationModifier = 4F;
		double ticks = (BotDClient.ClientTickHandler.ticksInGame + tickDelta) * 0.5;
		float deg =  (float) (ticks / rotationModifier % 360F);
		matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.sin(deg) / (float) Math.PI));
		matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(MathHelper.cos(deg) / (float) Math.PI));
		renderCircleLarge(matrices, vertexConsumers.getBuffer(BotDRenderLayer.get(CIRCLE_TEXTURE)), light, overlay);

		matrices.pop();
	}

	private void renderCircleLarge(MatrixStack matrices, VertexConsumer buffer, int light, int overlay) {
		circleEntityModel.render(matrices, buffer, light, overlay, 1,1,1,1);
	}
}
