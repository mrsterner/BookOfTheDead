package dev.sterner.book_of_the_dead.common.util;

import com.mojang.blaze3d.lighting.DiffuseLighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import me.shedaniel.math.Rectangle;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.List;

public class RenderUtils {
	/**
	 * Draw an entity just like the player is drawn in the PlayerInventoryScreen screen
	 */
	public static void drawEntity(int x, int y, int size, float mouseX, float mouseY, LivingEntity entity, @Nullable Rectangle bounds) {
		float f;

		float g;
		if (bounds != null) {
			f = (float) Math.atan((bounds.getCenterX() - mouseX) / 40.0F);
			g = (float) Math.atan((bounds.getCenterY() - mouseY) / 40.0F);
		} else {
			f = (float) Math.atan((x - mouseX) / 40.0F);
			g = (float) Math.atan((y - mouseY) / 40.0F);
		}
		MatrixStack matrixStack = RenderSystem.getModelViewStack();
		matrixStack.push();
		if (bounds != null) {
			matrixStack.translate(bounds.getCenterX(), bounds.getCenterY() + 20, 1050.0);
		} else {
			matrixStack.translate(x, y + 20, 1050.0);
		}
		matrixStack.scale(1.0F, 1.0F, -1.0F);
		RenderSystem.applyModelViewMatrix();
		MatrixStack matrixStack2 = new MatrixStack();
		matrixStack2.translate(0.0, 0.0, 1000.0);
		matrixStack2.scale((float) size, (float) size, (float) size);
		Quaternionf quaternionf = new Quaternionf().rotateZ((float) Math.PI);
		Quaternionf quaternionf2 = new Quaternionf().rotateX(g * 20.0F * (float) (Math.PI / 180.0));
		quaternionf.mul(quaternionf2);
		matrixStack2.multiply(quaternionf);
		float h = entity.bodyYaw;
		float i = entity.getYaw();
		float j = entity.getPitch();
		float k = entity.prevHeadYaw;
		float l = entity.headYaw;
		entity.bodyYaw = 180.0F + f * 20.0F;
		entity.setYaw(180.0F + f * 40.0F);
		entity.setPitch(-g * 20.0F);
		entity.headYaw = entity.getYaw();
		entity.prevHeadYaw = entity.getYaw();
		DiffuseLighting.setupInventoryEntityLighting();
		EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
		quaternionf2.conjugate();
		entityRenderDispatcher.setRotation(quaternionf2);
		entityRenderDispatcher.setRenderShadows(false);
		VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
		RenderSystem.runAsFancy(() -> entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, matrixStack2, immediate, 15728880));
		immediate.draw();
		entityRenderDispatcher.setRenderShadows(true);
		entity.bodyYaw = h;
		entity.setYaw(i);
		entity.setPitch(j);
		entity.prevHeadYaw = k;
		entity.headYaw = l;
		matrixStack.pop();
		RenderSystem.applyModelViewMatrix();
		DiffuseLighting.setup3DGuiLighting();
	}

	public static void renderMesh(Mesh mesh, MatrixStack matrices, VertexConsumer consumer, int light, int overlay) {
		for (List<BakedQuad> bakedQuads : ModelHelper.toQuadLists(mesh)) {
			for (BakedQuad bq : bakedQuads) {
				consumer.complexBakedQuad(matrices.peek(), bq, new float[]{1f, 1f, 1f, 1f}, 1f, 1f, 1f, new int[]{light, light, light, light}, overlay, true);
			}
		}
	}

	public static void emitFluidFace(QuadEmitter emitter, Sprite sprite, int color, Direction direction, float height, float depth, float EDGE_SIZE, float INNER_SIZE) {
		emitter.square(direction, EDGE_SIZE, EDGE_SIZE, (1f - EDGE_SIZE), (EDGE_SIZE + (height * INNER_SIZE)), EDGE_SIZE + (depth * INNER_SIZE));
		emitter.spriteBake(0, sprite, MutableQuadView.BAKE_ROTATE_NONE);
		emitter.spriteColor(0, color, color, color, color);
		emitter.sprite(0, 0, sprite.getMinU() + EDGE_SIZE * (sprite.getMaxU() - sprite.getMinU()), sprite.getMinV() + (1f - (EDGE_SIZE + (height * INNER_SIZE))) * (sprite.getMaxV() - sprite.getMinV()));
		emitter.sprite(1, 0, sprite.getMinU() + EDGE_SIZE * (sprite.getMaxU() - sprite.getMinU()), sprite.getMinV() + (1f - EDGE_SIZE) * (sprite.getMaxV() - sprite.getMinV()));
		emitter.sprite(2, 0, sprite.getMinU() + (1f - EDGE_SIZE) * (sprite.getMaxU() - sprite.getMinU()), sprite.getMinV() + (1f - EDGE_SIZE) * (sprite.getMaxV() - sprite.getMinV()));
		emitter.sprite(3, 0, sprite.getMinU() + (1f - EDGE_SIZE) * (sprite.getMaxU() - sprite.getMinU()), sprite.getMinV() + (1f - (EDGE_SIZE + (height * INNER_SIZE))) * (sprite.getMaxV() - sprite.getMinV()));
		emitter.emit();
	}

	public static void drawTexture(MatrixStack matrices, float x, float y, int width, int height, float u, float v, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
		drawTexturedQuad(matrices, x, x + width, y, y + height, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight);
	}

	public static void drawTexture(MatrixStack matrices, float x, float y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
		drawTexture(matrices, x, y, width, height, u, v, width, height, textureWidth, textureHeight);
	}

	private static void drawTexturedQuad(MatrixStack matrices, float x0, float x1, float y0, float y1, int z, int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight) {
		drawTexturedQuad(matrices.peek().getModel(), x0, x1, y0, y1, z, (u + 0.0F) / (float) textureWidth, (u + (float) regionWidth) / (float) textureWidth, (v + 0.0F) / (float) textureHeight, (v + (float) regionHeight) / (float) textureHeight);
	}

	private static void drawTexturedQuad(Matrix4f matrix, float x0, float x1, float y0, float y1, int z, float u0, float u1, float v0, float v1) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBufferBuilder();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferBuilder.vertex(matrix, x0, y0, (float) z).uv(u0, v0).next();
		bufferBuilder.vertex(matrix, x0, y1, (float) z).uv(u0, v1).next();
		bufferBuilder.vertex(matrix, x1, y1, (float) z).uv(u1, v1).next();
		bufferBuilder.vertex(matrix, x1, y0, (float) z).uv(u1, v0).next();
		BufferRenderer.drawWithShader(bufferBuilder.end());
	}
}
