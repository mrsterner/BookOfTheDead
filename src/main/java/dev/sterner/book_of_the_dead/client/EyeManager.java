package dev.sterner.book_of_the_dead.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.sterner.book_of_the_dead.common.util.Constants;
import dev.sterner.book_of_the_dead.common.util.RenderUtils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.random.RandomGenerator;


public class EyeManager extends DrawableHelper{
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

	public EyeDirection verticalEyeDirection = EyeDirection.NONE;
	public EyeDirection horizontalEyeDirection = EyeDirection.NONE;
	public int moveDelay = 20 * 3;
	public int moveDelayV = 20 * 3;

	public int blinkDelay = 20 * 2;

	public int horizontalTick = 0;
	public int verticalTick = 0;
	public int blinkTick = 0;

	boolean lookBack = false;
	boolean blinkUpp = false;

	public EyeManager(RandomGenerator randomGenerator){
		this.random = randomGenerator;
	}

	public void tick(){
		boolean blink = blink();
		if(blink){
			blinkTick = 0;
			blinkUpp = false;
		}

		if(horizontalEyeDirection == EyeDirection.NONE){
			if(random.nextFloat() < 0.01f){
				int i = random.nextInt(2);
				horizontalEyeDirection = switch (i){
					case 0 -> EyeDirection.LEFT;
					case 1 -> EyeDirection.RIGHT;
					default -> EyeDirection.NONE;
				};
			}
		}

		if(verticalEyeDirection == EyeDirection.NONE){
			if(random.nextFloat() < 0.01f){
				int i = random.nextInt(2);
				verticalEyeDirection = switch (i){
					case 0 -> {
						texture = EYES_UP;
						yield EyeDirection.UP;
					}
					case 1 -> {
						texture = EYES_DOWN;
						yield EyeDirection.DOWN;
					}
					default -> EyeDirection.NONE;
				};
			}
		}

		boolean bl = switch (horizontalEyeDirection){
			case LEFT -> lookLeft();
			case RIGHT -> lookRight();
			default -> false;
		};

		boolean bl2 = switch (horizontalEyeDirection){
			case UP -> lookUp();
			case DOWN -> lookDown();
			default -> false;
		};

		if (bl) {
			horizontalEyeDirection = EyeDirection.NONE;
			moveDelay = 20 * 5;
			lookBack = false;
		}

		if (bl2) {
			verticalEyeDirection = EyeDirection.NONE;
			moveDelayV = 20 * 5;
		}

		if(verticalEyeDirection != EyeDirection.NONE){
			if(random.nextFloat() < 0.01){
				verticalEyeDirection = EyeDirection.NONE;
				texture = EYES_MIDDLE;
			}
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

	private boolean lookDown() {
		boolean bl = false;
		verticalTick++;
		if(verticalTick >= moveDelayV){
			verticalTick = 0;
			bl = true;
		}

		return bl;
	}

	private boolean lookUp() {
		boolean bl = false;
		verticalTick++;
		if(verticalTick >= moveDelayV){
			verticalTick = 0;
			bl = true;
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

	public void drawTexture(MatrixStack matrixStack, float x, float y){
		int u = (textureCoord.x) * 14;
		int v = (textureCoord.y) * 10;

		RenderSystem.setShaderTexture(0, texture);
		RenderUtils.drawTexture(matrixStack, x, y, u, v, 14, 10, 70, 50);
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
