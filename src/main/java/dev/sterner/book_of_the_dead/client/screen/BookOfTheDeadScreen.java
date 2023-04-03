package dev.sterner.book_of_the_dead.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.sterner.book_of_the_dead.client.screen.widget.KnowledgeWidget;
import dev.sterner.book_of_the_dead.common.registry.BotDKnowledgeRegistry;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ChatNarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;


public class BookOfTheDeadScreen extends Screen {
	public static final Identifier BOOK_TEXTURE = Constants.id("textures/gui/background.png");
	public PlayerEntity player;
	public static BookOfTheDeadScreen screen;

	public BookOfTheDeadScreen(PlayerEntity player) {
		super(ChatNarratorManager.NO_TITLE);
		this.player = player;
	}


	public static void openScreen(PlayerEntity player) {
		MinecraftClient.getInstance().setScreen(getInstance(player));
	}

	@Override
	protected void init() {
		int x = (this.width - 192) / 4 + 9 * 5 - 4;
		int y = 32 * 3 + 18 + 4;
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
		this.renderBackground(matrices);
		this.setFocused(false);
		renderTransparentTexture(matrices, (this.width - 192) / 4 - 16, 32, 0, 0, 272, 182, 512, 256);
		super.render(matrices, mouseX, mouseY, delta);
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
