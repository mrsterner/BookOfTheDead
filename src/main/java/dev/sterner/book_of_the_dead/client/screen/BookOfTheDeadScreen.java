package dev.sterner.book_of_the_dead.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.sterner.book_of_the_dead.client.screen.widget.KnowledgeTab;
import dev.sterner.book_of_the_dead.client.screen.widget.KnowledgeWidget;
import dev.sterner.book_of_the_dead.common.registry.BotDKnowledgeRegistry;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ChatNarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;


public class BookOfTheDeadScreen extends Screen {
	public static final Identifier BOOK_TEXTURE = Constants.id("textures/gui/background.png");
	public static final Identifier PARALLAX = Constants.id("textures/gui/parallax.png");
	public PlayerEntity player;
	public static BookOfTheDeadScreen screen;
	public final KnowledgeTab knowledgeTab;
	public double xOffset = 0;
	public double yOffset = 0;
	public int scissorX;
	public int scissorY;
	public int scissorWidth;
	public int scissorHeight;
	public boolean isDragging;
	public int buttonOffset;


	public BookOfTheDeadScreen(PlayerEntity player) {
		super(ChatNarratorManager.NO_TITLE);
		this.player = player;
		this.knowledgeTab = new KnowledgeTab(this);
		this.scissorX = 50;
		this.scissorY = 30;
		this.scissorWidth = 122 + scissorX;
		this.scissorHeight = 172 + scissorY;
		/*
		int i = (this.width - 252) / 2;
		int j = (this.height - 140) / 2;
		 */
	}


	public static void openScreen(PlayerEntity player) {
		MinecraftClient.getInstance().setScreen(getInstance(player));
	}

	@Override
	protected void init() {
		float x = (this.width - 192) / 4 + 9 * 5 - 4;
		float y = 32 * 3 + 18 + 4;

		//Void
		addDrawableChild(new KnowledgeWidget(x - 9, y + 15, this, BotDKnowledgeRegistry.VOID));
		addDrawableChild(new KnowledgeWidget(x + 9, y + 15, this, BotDKnowledgeRegistry.AMALGAM));
		addDrawableChild(new KnowledgeWidget(x, y + 12 + 18, this, BotDKnowledgeRegistry.DISPOSITION));
		addDrawableChild(new KnowledgeWidget(x, y + 12 + 18 * 2, this, BotDKnowledgeRegistry.BALANCE));
		addDrawableChild(new KnowledgeWidget(x, y + 12 + 18 * 3, this, BotDKnowledgeRegistry.PROJECTION));
		//Core
		addDrawableChild(new KnowledgeWidget(x, y, this, BotDKnowledgeRegistry.ALCHEMY));
		//Main
		addDrawableChild(new KnowledgeWidget(x - 9, y - 15, this, BotDKnowledgeRegistry.CALCINATION));
		addDrawableChild(new KnowledgeWidget(x + 9, y - 15, this, BotDKnowledgeRegistry.DISSOLUTION));
		addDrawableChild(new KnowledgeWidget(x, y - 12 - 18, this, BotDKnowledgeRegistry.SEPARATION));
		addDrawableChild(new KnowledgeWidget(x + 9, y - 15 - 30, this, BotDKnowledgeRegistry.CONJUNCTION));
		addDrawableChild(new KnowledgeWidget(x - 9, y - 15 - 30, this, BotDKnowledgeRegistry.FERMENTATION));
		addDrawableChild(new KnowledgeWidget(x, y - 12 - 18 - 30, this, BotDKnowledgeRegistry.DISTILLATION));
		addDrawableChild(new KnowledgeWidget(x, y - 12 - 18 * 2 - 30, this, BotDKnowledgeRegistry.COAGULATION));
		addDrawableChild(new KnowledgeWidget(x, y - 12 - 18 * 3 - 30, this, BotDKnowledgeRegistry.PHILOSOPHER));
		//Soul
		addDrawableChild(new KnowledgeWidget(x + 18, y, this, BotDKnowledgeRegistry.SOUL));
		addDrawableChild(new KnowledgeWidget(x + 9 + 18, y - 15, this, BotDKnowledgeRegistry.ESSENCE));
		addDrawableChild(new KnowledgeWidget(x + 18, y - 12 - 18, this, BotDKnowledgeRegistry.FUSION));
		addDrawableChild(new KnowledgeWidget(x + 9 + 18, y - 15 - 30, this, BotDKnowledgeRegistry.MULTIPLICATION));
		addDrawableChild(new KnowledgeWidget(x + 18 + 18, y - 12 - 18 - 30, this, BotDKnowledgeRegistry.LIFE));
		//Infernal
		addDrawableChild(new KnowledgeWidget(x - 18, y, this, BotDKnowledgeRegistry.BRIMSTONE));
		addDrawableChild(new KnowledgeWidget(x - 9 - 18, y - 15, this, BotDKnowledgeRegistry.ASH));
		addDrawableChild(new KnowledgeWidget(x - 18, y - 12 - 18, this, BotDKnowledgeRegistry.MELT));
		addDrawableChild(new KnowledgeWidget(x - 9 - 18, y - 15 - 30, this, BotDKnowledgeRegistry.ROT));
		addDrawableChild(new KnowledgeWidget(x - 18 - 18, y - 12 - 18 - 30, this, BotDKnowledgeRegistry.CADUCEUS));
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		int scaledWidth = (this.width - 192) / 4;

		matrices.push();
		this.renderBackground(matrices);
		this.setFocused(false);
		renderTransparentTexture(matrices, (this.width - 192) / 4 - 16, 32, 0, 0, 272, 182, 512, 256);
		matrices.pop();
		this.buttonOffset = scaledWidth;
		RenderSystem.setShaderTexture(0, BookOfTheDeadScreen.PARALLAX);
		enableScissor(scaledWidth, this.scissorY, this.scissorWidth + scaledWidth, this.scissorHeight);
		matrices.push();
		matrices.translate(xOffset, yOffset, 0.0F);

		drawTexture(matrices, 0, 0, 300.0F, 200.0F, 2507, 1205, 2507, 1205);

		matrices.pop();
		disableScissor();
		super.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (button == 0) {
			if (!this.isDragging) {
				// Check if the mouse is within the screen texture
				if (mouseX >= this.xOffset && mouseX <= this.xOffset + this.buttonOffset && mouseY >= this.yOffset && mouseY <= this.yOffset + this.scissorHeight) {
					this.isDragging = true;
				}
			}

			if (this.isDragging) {
				// Update the position of the screen
				this.xOffset = MathHelper.clamp(this.xOffset + deltaX, - 20,   20);
				this.yOffset = MathHelper.clamp(this.yOffset + deltaY, - 20,   20);


				return true;
			}
		}
		return false;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		this.isDragging = false;
		return super.mouseReleased(mouseX, mouseY, button);
	}

	public void renderTransparentTexture(MatrixStack matrices, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BOOK_TEXTURE);

		drawTexture(matrices, x, y, u, v, width, height, textureWidth, textureHeight);

		RenderSystem.defaultBlendFunc();
		RenderSystem.disableBlend();
	}

	public static BookOfTheDeadScreen getInstance(PlayerEntity player) {
		if (screen == null) {
			screen = new BookOfTheDeadScreen(player);
		}
		return screen;
	}
}
