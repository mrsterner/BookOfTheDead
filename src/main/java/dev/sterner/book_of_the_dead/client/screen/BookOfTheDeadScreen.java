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


public class BookOfTheDeadScreen extends Screen{
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
		addDrawableChild(new KnowledgeWidget((this.width - 192) / 4 + 134, 38 + 4 * 32, this, BotDKnowledgeRegistry.ALCHEMY));
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		this.setFocused(false);
		renderTransparentTexture(matrices, (this.width - 192) / 4 - 16, 32, 0, 0, 270, 180, 512, 256);

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
