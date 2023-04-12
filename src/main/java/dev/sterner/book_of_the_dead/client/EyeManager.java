package dev.sterner.book_of_the_dead.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.random.RandomGenerator;
import org.joml.Matrix4f;



public class EyeManager {
	public static EyeManager manager = null;
	private RandomGenerator random;
	public static final Identifier EYES_UP = Constants.id("textures/gui/eyes/1.png");
	public static final Identifier EYES_MIDDLE = Constants.id("textures/gui/eyes/2.png");
	public static final Identifier EYES_DOWN = Constants.id("textures/gui/eyes/3.png");

	public Identifier texture = EYES_MIDDLE;

	public int width = 14;
	public int height = 10;
	public SpriteCoordinates textureCoord = new SpriteCoordinates(2,2);
	public final int MAX_Y = 4;
	public final int MAX_X = 4;

	public EyeDirection eyeDirection = EyeDirection.NONE;
	public int moveDelay = 20 * 5;
	public int blinkDelay = 20 * 2;

	public int horizontalTick = 0;
	public int verticalTick = 0;
	public int blinkTick = 0;

	boolean lookBack = false;
	boolean blinkUpp = false;
	boolean shouldResetVertical = false;

	public EyeManager(RandomGenerator randomGenerator){
		this.random = randomGenerator;
	}

	public static void tickEye(MinecraftClient client) {
		if(client.world != null){
			if (manager == null) {
				manager = new EyeManager(client.world.random);
			} else {
				manager.tick();
			}
		}
	}

	public void tick(){
		boolean blink = blink();
		if(blink){
			blinkTick = 0;
			blinkUpp = false;
		}

		boolean bl = switch (eyeDirection){
			case LEFT -> lookLeft();
			case RIGHT -> lookRight();
			case UP -> lookUp(shouldResetVertical);
			case DOWN -> lookDown(shouldResetVertical);
			default -> false;
		};

		if (bl) {
			eyeDirection = EyeDirection.NONE;
			moveDelay = 20 * 5;
			lookBack = false;
		}
	}

	private boolean blink(){
		boolean bl = false;
		blinkTick++;
		if(blinkTick > blinkDelay){
			if(blinkUpp){
				textureCoord = traverseUp(textureCoord);
				if(textureCoord.y == 0){
					bl = true;
				}
			}else{
				textureCoord = traverseDown(textureCoord);
				if(textureCoord.y == MAX_Y){
					blinkUpp = true;
				}
			}
		}


		return bl;
	}

	private boolean lookDown(boolean reset) {
		boolean bl = false;
		verticalTick++;
		if(verticalTick >= moveDelay){
			verticalTick = 0;
			if(reset){
				texture = EYES_MIDDLE;
				bl = true;
			}else{
				texture = EYES_DOWN;
			}
		}

		return bl;
	}

	private boolean lookUp(boolean reset) {
		boolean bl = false;
		verticalTick++;
		if(verticalTick >= moveDelay){
			verticalTick = 0;
			if(reset){
				texture = EYES_MIDDLE;
				bl = true;
			}else{
				texture = EYES_UP;
			}
		}

		return bl;
	}

	public boolean lookLeft(){
		boolean bl = false;
		horizontalTick++;
		if(horizontalTick >= moveDelay){
			horizontalTick = 0;
			if(lookBack){
				textureCoord = traverseRight(textureCoord);
				if(textureCoord.x == 2){
					bl = true;
				}
			}else{
				textureCoord = traverseLeft(textureCoord);
			}
			if(textureCoord.x == 1 && random.nextFloat() < 0.35f){
				lookBack = true;
			} else if (textureCoord.x == 0) {
				lookBack = true;
			}
			// Add a random delay offset to moveDelay
			moveDelay += (int)(Math.random() * 10) - 5; // Offset by -5 to +5 ticks
			if(moveDelay < 1){
				moveDelay = 1;
			}
		}
		return bl;
	}

	public boolean lookRight(){
		boolean bl = false;
		horizontalTick++;
		if(horizontalTick >= moveDelay){
			horizontalTick = 0;
			if(lookBack){
				textureCoord = traverseLeft(textureCoord);
				if(textureCoord.x == 2){
					bl = true;
				}
			}else{
				textureCoord = traverseRight(textureCoord);
			}
			if(textureCoord.x == 3 && random.nextFloat() < 0.35f){
				lookBack = true;
			} else if (textureCoord.x == MAX_X) {
				lookBack = true;
			}
			// Add a random delay offset to moveDelay
			moveDelay += (int)(Math.random() * 10) - 5; // Offset by -5 to +5 ticks
			if(moveDelay < 1){
				moveDelay = 1;
			}
		}
		return bl;
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
