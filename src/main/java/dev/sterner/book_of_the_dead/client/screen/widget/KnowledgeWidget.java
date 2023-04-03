package dev.sterner.book_of_the_dead.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.client.screen.book.Knowledge;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.Material;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.ChatNarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class KnowledgeWidget extends ClickableWidget {
	public Knowledge knowledge;
	public BookOfTheDeadScreen screen;
	public float x;
	public float y;

	public KnowledgeWidget(float x, float y, BookOfTheDeadScreen screen, Knowledge knowledge) {
		super((int)x, (int)y, 17, 17, ChatNarratorManager.NO_TITLE);
		this.screen = screen;
		this.knowledge = knowledge;
		this.x = x;
		this.y = y;
	}

	@Override
	public void drawWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {

		RenderSystem.setShaderTexture(0, knowledge.icon);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, !isHovered() ? 0.25F : this.alpha);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		// Render the screen texture

		enableScissor(screen.buttonOffset, screen.scissorY, screen.scissorWidth + screen.buttonOffset, screen.scissorHeight);
		matrices.push();
		matrices.translate(screen.xOffset, screen.yOffset, 0.0F);
		drawTexture(matrices, this.x + (float) screen.xOffset, this.y + (float) screen.yOffset, 0 , 0, this.width, this.height, 17, 17);
		matrices.pop();
		disableScissor();


	}

	public static void drawTexture(MatrixStack matrices, float x, float y, int width, int height, float u, float v, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
		drawTexturedQuad(matrices, x, x + width, y, y + height, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight);
	}

	public static void drawTexture(MatrixStack matrices, float x, float y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
		drawTexture(matrices, x, y, width, height, u, v, width, height, textureWidth, textureHeight);
	}

	private static void drawTexturedQuad(MatrixStack matrices, float x0, float x1, float y0, float y1, int z, int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight) {
		drawTexturedQuad(matrices.peek().getModel(), x0, x1, y0, y1, z, (u + 0.0F) / (float)textureWidth, (u + (float)regionWidth) / (float)textureWidth, (v + 0.0F) / (float)textureHeight, (v + (float)regionHeight) / (float)textureHeight);
	}

	private static void drawTexturedQuad(Matrix4f matrix, float x0, float x1, float y0, float y1, int z, float u0, float u1, float v0, float v1) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBufferBuilder();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferBuilder.vertex(matrix, (float)x0, (float)y0, (float)z).uv(u0, v0).next();
		bufferBuilder.vertex(matrix, (float)x0, (float)y1, (float)z).uv(u0, v1).next();
		bufferBuilder.vertex(matrix, (float)x1, (float)y1, (float)z).uv(u1, v1).next();
		bufferBuilder.vertex(matrix, (float)x1, (float)y0, (float)z).uv(u1, v0).next();
		BufferRenderer.drawWithShader(bufferBuilder.end());
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.hovered = mouseX >= this.getX() + screen.xOffset * 2 && mouseY >= this.getY() + screen.yOffset * 2 && mouseX < this.getX() + screen.xOffset * 2 + this.width && mouseY < this.getY() + screen.yOffset * 2 + this.height;
		this.drawWidget(matrices, mouseX, mouseY, delta);
	}

	@Override
	protected boolean isValidClickButton(int button) {
		return super.isValidClickButton(button);
	}

	@Override
	protected boolean clicked(double mouseX, double mouseY) {
		if(!this.active && !this.visible){
			return false;
		}

		double halfWidth = (double) this.width / 2;
		double halfHeight = (double) this.height / 2;
		double centerX = this.getX() + halfWidth;
		double centerY = this.getY() + halfHeight;
		double radius = Math.min(halfWidth, halfHeight);

		double dx = Math.abs(mouseX + screen.xOffset - centerX);
		double dy = Math.abs(mouseY + screen.yOffset - centerY);

		if (dx > radius || dy > radius) {
			return false;
		}

		return dx + dy <= radius;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return super.isMouseOver(mouseX + screen.xOffset, mouseY + screen.yOffset);
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		if (isValidClickButton(0)) {

		}
	}

	@Override
	protected void updateNarration(NarrationMessageBuilder builder) {

	}

	public boolean isHovering(double mouseX, double mouseY) {
		return clicked(mouseX, mouseY);
	}
}
