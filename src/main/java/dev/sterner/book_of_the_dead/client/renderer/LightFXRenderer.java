package dev.sterner.book_of_the_dead.client.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.client.registry.BotDRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Axis;
import net.minecraft.util.random.RandomGenerator;
import org.joml.Matrix4f;

import java.awt.*;

public class LightFXRenderer {
	private static final float HALF_SQRT_3 = (float) (Math.sqrt(3) / 2);

	public static void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Color color) {
		float rotation = (float) ((System.currentTimeMillis() / 50d) % 20000d) / 150;
		RandomGenerator random = RandomGenerator.createLegacy(4200);
		VertexConsumer buffer = vertexConsumers.getBuffer(BotDRenderLayers.FX);

		matrices.push();

		Matrix4f position = matrices.peek().getModel();

		for (int n = 0; n < 45; ++n) {
			matrices.multiply(Axis.X_POSITIVE.rotationDegrees(random.nextFloat() * 360.0F));
			matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(random.nextFloat() * 360.0F));
			matrices.multiply(Axis.X_POSITIVE.rotationDegrees(random.nextFloat() * 360.0F - rotation * 90.0F));

			float radius = random.nextFloat() * 25 + 50;
			float width = random.nextFloat() * 2 + 3;

			sourceVertex(buffer, position, color.getAlpha());
			negativeXTerminalVertex(buffer, position, radius, width, color);
			positiveXTerminalVertex(buffer, position, radius, width, color);

			sourceVertex(buffer, position, color.getAlpha());
			positiveXTerminalVertex(buffer, position, radius, width, color);
			positiveZTerminalVertex(buffer, position, radius, width, color);

			sourceVertex(buffer, position, color.getAlpha());
			positiveZTerminalVertex(buffer, position, radius, width, color);
			negativeXTerminalVertex(buffer, position, radius, width, color);
		}

		matrices.pop();
	}

	private static void sourceVertex(VertexConsumer vertices, Matrix4f matrix4f, int alpha) {
		vertices.vertex(matrix4f, 0.0F, 0.0F, 0.0F).color(255, 255, 255, alpha).next();
	}

	private static void negativeXTerminalVertex(VertexConsumer vertices, Matrix4f matrix4f, float y, float x, Color color) {
		vertices.vertex(matrix4f, -HALF_SQRT_3 * x, y, -0.5F * x).color(color.getRed(), color.getGreen(), color.getBlue(), 0).next();
	}

	private static void positiveXTerminalVertex(VertexConsumer vertices, Matrix4f matrix4f, float y, float x, Color color) {
		vertices.vertex(matrix4f, HALF_SQRT_3 * x, y, -0.5F * x).color(color.getRed(), color.getGreen(), color.getBlue(), 0).next();
	}

	private static void positiveZTerminalVertex(VertexConsumer vertices, Matrix4f matrix4f, float y, float z, Color color) {
		vertices.vertex(matrix4f, 0.0F, y, z).color(color.getRed(), color.getGreen(), color.getBlue(), 0).next();
	}
}
