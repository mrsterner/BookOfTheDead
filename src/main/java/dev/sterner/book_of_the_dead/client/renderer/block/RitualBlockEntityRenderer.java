package dev.sterner.book_of_the_dead.client.renderer.block;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.BotDClient;
import dev.sterner.book_of_the_dead.client.renderer.renderlayer.BotDRenderLayer;
import dev.sterner.book_of_the_dead.common.block.entity.RitualBlockEntity;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;

public class RitualBlockEntityRenderer implements BlockEntityRenderer<RitualBlockEntity> {
	private float alpha = 0;
	private final Identifier CIRCLE_NECROMANCY = Constants.id("textures/misc/circle_necromancy.png");
	private final Identifier CIRCLE_ALCHEMICAL = Constants.id("textures/misc/circle_alchemical.png");
	private final Identifier CIRCLE_SACRIFICE = Constants.id("textures/misc/circle_sacrifice.png");
	private final Identifier CIRCLE_ELDRITCH = Constants.id("textures/misc/circle_eldritch.png");

	public RitualBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
	}

	@Override
	public void render(RitualBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if(entity.shouldRun){
			alpha = ((float)entity.clientTime + tickDelta - 1.0F) / 20.0F * 1.6F;
			alpha = MathHelper.sqrt(alpha);
			if (alpha > 1.0F) {
				alpha = 1.0F;
			}
		}else{
			if(alpha < 0.02){
				alpha = 0;
			}else {
				alpha = alpha - 0.01f;
			}
		}
		matrices.push();
		matrices.translate(0.5,0.15,0.5);
		final float rotationModifier = 4F;
		double ticks = (BotDClient.ClientTickHandler.ticksInGame + tickDelta) * 0.5;
		float deg =  (float) (ticks / rotationModifier % 360F);
		matrices.multiply(Axis.Z_POSITIVE.rotationDegrees(MathHelper.sin(deg) / (float) Math.PI));
		matrices.multiply(Axis.X_POSITIVE.rotationDegrees(MathHelper.cos(deg) / (float) Math.PI));
		matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(entity.getCachedState().get(HorizontalFacingBlock.FACING).asRotation()));

		Matrix4f mat = matrices.peek().getModel();
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(BotDRenderLayer.GLOWING_LAYER.apply(CIRCLE_ELDRITCH));
		RenderSystem.setShaderTexture(0, CIRCLE_ELDRITCH);
		vertexConsumer.vertex(mat, -2.5F, 0, 2.5F).color(255, 255, 255, 255).uv(0, 1).overlay(overlay).light(light).normal(0, 1, 0).next();//TODO replace 255 with alpha
		vertexConsumer.vertex(mat, 2.5F, 0, 2.5F).color(255, 255, 255, 255).uv(1, 1).overlay(overlay).light(light).normal(0, 1, 0).next();
		vertexConsumer.vertex(mat, 2.5F, 0, -2.5F).color(255, 255, 255, 255).uv(1, 0).overlay(overlay).light(light).normal(0, 1, 0).next();
		vertexConsumer.vertex(mat, -2.5F, 0, -2.5F).color(255, 255, 255, 255).uv(0, 0).overlay(overlay).light(light).normal(0, 1, 0).next();

		matrices.pop();
	}
}
