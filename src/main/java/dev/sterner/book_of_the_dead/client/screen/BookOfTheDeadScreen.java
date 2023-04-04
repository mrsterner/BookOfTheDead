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

import java.util.ArrayList;
import java.util.List;


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
	public List<KnowledgeWidget> widgets = new ArrayList<>();


	public BookOfTheDeadScreen(PlayerEntity player) {
		super(ChatNarratorManager.NO_TITLE);
		this.player = player;
		this.knowledgeTab = new KnowledgeTab(this);
		this.scissorX = 50;
		this.scissorY = 30;
		this.scissorWidth = 122 + scissorX;
		this.scissorHeight = 172 + scissorY;
	}


	public static void openScreen(PlayerEntity player) {
		MinecraftClient.getInstance().setScreen(getInstance(player));
	}

	@Override
	protected void init() {
		widgets.clear();
		float x = (float) (this.width - 192) / 4 + 9 * 5 - 4;
		float y = 32 * 3 + 18 + 4;
		//Void
		widgets.add(new KnowledgeWidget(x - 9, y + 15, this, BotDKnowledgeRegistry.VOID));
		widgets.add(new KnowledgeWidget(x + 9, y + 15, this, BotDKnowledgeRegistry.AMALGAM));
		widgets.add(new KnowledgeWidget(x, y + 12 + 18, this, BotDKnowledgeRegistry.DISPOSITION));
		widgets.add(new KnowledgeWidget(x, y + 12 + 18 * 2, this, BotDKnowledgeRegistry.BALANCE));
		widgets.add(new KnowledgeWidget(x, y + 12 + 18 * 3, this, BotDKnowledgeRegistry.PROJECTION));
		//Core
		widgets.add(new KnowledgeWidget(x, y, this, BotDKnowledgeRegistry.ALCHEMY));
		//Main
		widgets.add(new KnowledgeWidget(x - 9, y - 15, this, BotDKnowledgeRegistry.CALCINATION));
		widgets.add(new KnowledgeWidget(x + 9, y - 15, this, BotDKnowledgeRegistry.DISSOLUTION));
		widgets.add(new KnowledgeWidget(x, y - 12 - 18, this, BotDKnowledgeRegistry.SEPARATION));
		widgets.add(new KnowledgeWidget(x + 9, y - 15 - 30, this, BotDKnowledgeRegistry.CONJUNCTION));
		widgets.add(new KnowledgeWidget(x - 9, y - 15 - 30, this, BotDKnowledgeRegistry.FERMENTATION));
		widgets.add(new KnowledgeWidget(x, y - 12 - 18 - 30, this, BotDKnowledgeRegistry.DISTILLATION));
		widgets.add(new KnowledgeWidget(x, y - 12 - 18 * 2 - 30, this, BotDKnowledgeRegistry.COAGULATION));
		widgets.add(new KnowledgeWidget(x, y - 12 - 18 * 3 - 30, this, BotDKnowledgeRegistry.PHILOSOPHER));
		//Soul
		widgets.add(new KnowledgeWidget(x + 18, y, this, BotDKnowledgeRegistry.SOUL));
		widgets.add(new KnowledgeWidget(x + 9 + 18, y - 15, this, BotDKnowledgeRegistry.ESSENCE));
		widgets.add(new KnowledgeWidget(x + 18, y - 12 - 18, this, BotDKnowledgeRegistry.FUSION));
		widgets.add(new KnowledgeWidget(x + 9 + 18, y - 15 - 30, this, BotDKnowledgeRegistry.MULTIPLICATION));
		widgets.add(new KnowledgeWidget(x + 18 + 18, y - 12 - 18 - 30, this, BotDKnowledgeRegistry.LIFE));
		//Infernal
		widgets.add(new KnowledgeWidget(x - 18, y, this, BotDKnowledgeRegistry.BRIMSTONE));
		widgets.add(new KnowledgeWidget(x - 9 - 18, y - 15, this, BotDKnowledgeRegistry.ASH));
		widgets.add(new KnowledgeWidget(x - 18, y - 12 - 18, this, BotDKnowledgeRegistry.MELT));
		widgets.add(new KnowledgeWidget(x - 9 - 18, y - 15 - 30, this, BotDKnowledgeRegistry.ROT));
		widgets.add(new KnowledgeWidget(x - 18 - 18, y - 12 - 18 - 30, this, BotDKnowledgeRegistry.CADUCEUS));

		for(KnowledgeWidget widget : widgets){
			addDrawableChild(widget);
		}
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		int scaledWidth = (this.width - 192) / 4 - 5;

		matrices.push();
		this.renderBackground(matrices);
		this.setFocused(false);
		renderBookTexture(matrices, (this.width - 192) / 4 - 16, 32, 0, 0, 272, 182, 512, 256);

		matrices.pop();
		RenderSystem.setShaderTexture(0, BookOfTheDeadScreen.PARALLAX);
		enableScissor(scaledWidth, this.scissorY + 13, this.scissorWidth + scaledWidth - 61, this.scissorHeight + 1);
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
				if (mouseX >= (double) (this.width - 192) / 4 - 16 && mouseX <= (double) (this.width - 192) / 4 - 16 + 122 && mouseY >= this.yOffset && mouseY <= this.scissorHeight) {
					this.isDragging = true;
				}
			}

			// Check if the mouse is over any Button widgets
			for (KnowledgeWidget widget : this.widgets) {
				if (widget.isMouseOver(mouseX, mouseY)) {
					// Return false to prevent dragging if the mouse is over a Button widget
					return false;
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

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	public void renderBookTexture(MatrixStack matrices, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
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
