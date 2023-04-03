package dev.sterner.book_of_the_dead.client.screen.widget;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.client.screen.book.Knowledge;
import dev.sterner.book_of_the_dead.common.registry.BotDKnowledgeRegistry;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KnowledgeTab extends DrawableHelper {
	public final List<KnowledgeWidget> widgets = new ArrayList<>();
	private final BookOfTheDeadScreen screen;
	private double originX;
	private double originY;
	private int minPanX = Integer.MAX_VALUE;
	private int minPanY = Integer.MAX_VALUE;
	private int maxPanX = Integer.MIN_VALUE;
	private int maxPanY = Integer.MIN_VALUE;
	private boolean initialized;

	public KnowledgeTab(BookOfTheDeadScreen screen) {
		this.screen = screen;
		this.init();
	}

	private void init() {
		addKnowledge(BotDKnowledgeRegistry.ALCHEMY, 32 * 4, 32 * 10);
		addKnowledge(BotDKnowledgeRegistry.ASH, 32 * 4, 32 * 10 - 18);
	}

	public void render(MatrixStack matrices, int x, int y) {
		if (!this.initialized) {
			this.originX = (double)(117 - (this.maxPanX + this.minPanX) / 2);
			this.originY = (double)(56 - (this.maxPanY + this.minPanY) / 2);
			this.initialized = true;
		}

		enableScissor(x, y, x + 122, y + 173);
		matrices.push();
		matrices.translate((float)x, (float)y, 0.0F);
		Identifier identifier = Constants.id("textures/gui/parallax.png");
		if (identifier != null) {
			RenderSystem.setShaderTexture(0, identifier);
		} else {
			RenderSystem.setShaderTexture(0, TextureManager.MISSING_IDENTIFIER);
		}

		drawTexture(matrices, 32 + 9, 32 + 9, 0.0F, 0.0F, 122, 173, 2507 / 2, 1205 / 2);

		matrices.pop();
		disableScissor();
	}

	public void move(double offsetX, double offsetY) {
		if (this.maxPanX - this.minPanX > 234) {
			this.originX = MathHelper.clamp(this.originX + offsetX, (-(this.maxPanX - 234)), 0.0);
		}

		if (this.maxPanY - this.minPanY > 113) {
			this.originY = MathHelper.clamp(this.originY + offsetY, (-(this.maxPanY - 113)), 0.0);
		}
	}

	public void addKnowledge(Knowledge knowledge, int x, int y) {
		KnowledgeWidget knowledgeWidget = new KnowledgeWidget(x, y, screen, knowledge);
		this.addWidget(knowledgeWidget);
	}

	private void addWidget(KnowledgeWidget widget) {
		this.widgets.add(widget);
		int i = widget.getX();
		int j = i + 28;
		int k = widget.getY();
		int l = k + 27;
		this.minPanX = Math.min(this.minPanX, i);
		this.maxPanX = Math.max(this.maxPanX, j);
		this.minPanY = Math.min(this.minPanY, k);
		this.maxPanY = Math.max(this.maxPanY, l);
	}
}
