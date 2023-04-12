package dev.sterner.book_of_the_dead.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import org.joml.Matrix4f;



public class EyeManager {
	private RandomGenerator random;
	public static final Identifier EYES_UP = Constants.id("textures/gui/eyes/1.png");
	public static final Identifier EYES_MIDDLE = Constants.id("textures/gui/eyes/2.png");
	public static final Identifier EYES_DOWN = Constants.id("textures/gui/eyes/3.png");

	public Identifier texture = EYES_MIDDLE;

	public int width = 14;
	public int height = 10;
	public SpriteCoordinates textureCoord = new SpriteCoordinates(2,2);

	public boolean isBlinking = false;
	public boolean blinkingUp = false;
	public int blinkingTimer = 0;
	public int blinkCooldown = 20 * 10;

	public int lookDirectionCooldown = 100;
	public int lookTimer = 0;
	public EyeDirection eyeDirection = EyeDirection.NONE;

	public EyeManager(RandomGenerator randomGenerator){
		this.random = randomGenerator;
	}

	public void tick(){
		if (isBlinking) {
			handleBlinkingUV();
		} else if (lookDirectionCooldown < 0) {
			lookDirectionCooldown = 100;
			handleLookUV();
		}
		lookDirectionCooldown--;
		blinkCooldown--;
	}

	private void handleLookUV() {
		lookTimer++;
		switch (eyeDirection){
			case UP -> {}
			case DOWN -> {}
			case LEFT -> {}
			case RIGHT -> {}
			default -> lookTimer = 0;
		}
	}

	private void handleBlinkingUV() {
		if(!blinkingUp){
			blinkingTimer++;
			textureCoord = traverseUp(textureCoord);
		}else{
			textureCoord = traverseDown(textureCoord);
		}
		if(blinkingTimer > 20 * 3){
			blinkingTimer = 0;
			blinkingUp = true;
		}
	}


	private static void drawTexturedQuad(Identifier texture, MatrixStack matrixStack, int x, int y, int width, int height, float minU, float maxU, float minV, float maxV) {
		Matrix4f matrix = matrixStack.peek().getModel();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, texture);
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBufferBuilder();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferBuilder.vertex(matrix, x, y, 0).uv(minU, minV).next();
		bufferBuilder.vertex(matrix, x, y + height, 0).uv(minU, maxV).next();
		bufferBuilder.vertex(matrix, x + width, y + height, 0).uv(maxU, maxV).next();
		bufferBuilder.vertex(matrix, x + width, y, 0).uv(maxU, minV).next();
		BufferRenderer.drawWithShader(bufferBuilder.end());
	}

	public SpriteCoordinates traverseDown(SpriteCoordinates origin){
		return new SpriteCoordinates(origin.x, Math.min(origin.y + 1, 4));
	}

	public SpriteCoordinates traverseUp(SpriteCoordinates origin){
		return new SpriteCoordinates(origin.x, Math.max(origin.y - 1, 0));
	}

	public SpriteCoordinates traverseLeft(SpriteCoordinates origin){
		return new SpriteCoordinates(Math.max(origin.x - 1, 0), origin.y);
	}

	public SpriteCoordinates traverseRight(SpriteCoordinates origin){
		return new SpriteCoordinates(Math.min(origin.x + 1, 4), origin.y);
	}

	public static class SpriteCoordinates {
		private final int x;
		private final int y;

		public SpriteCoordinates(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public enum EyeDirection{
		NONE,
		UP,
		DOWN,
		RIGHT,
		LEFT;
	}
}
