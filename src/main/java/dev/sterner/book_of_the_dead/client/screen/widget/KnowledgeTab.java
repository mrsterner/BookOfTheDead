package dev.sterner.book_of_the_dead.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.client.screen.book.Knowledge;
import dev.sterner.book_of_the_dead.common.registry.BotDKnowledgeRegistry;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeTab extends DrawableHelper {
	public final List<KnowledgeWidget> widgets = new ArrayList<>();
	private final BookOfTheDeadScreen screen;
	public float xO = 0;
	public float y0 = 0;

	public KnowledgeTab(BookOfTheDeadScreen screen) {
		this.screen = screen;
		this.init();
	}

	private void init() {
		addKnowledge(BotDKnowledgeRegistry.ALCHEMY, 32 * 4, 32 * 10);
		addKnowledge(BotDKnowledgeRegistry.ASH, 32 * 4, 32 * 10 - 18);
	}

	public void move(float xOffset, float yOffset) {
	}

	public void render(MatrixStack matrices, int x, int y) {
		enableScissor(x, y, x + 122, y + 173);
		matrices.push();
		matrices.translate((float) x, (float) y, 0.0F);
		Identifier identifier = Constants.id("textures/gui/parallax.png");
		RenderSystem.setShaderTexture(0, identifier);

		drawTexture(matrices, 32 + 9, 32 + 9, 0.0F, 0.0F, 122, 173, 2507 / 2, 1205 / 2);

		matrices.pop();
		disableScissor();
	}

	public void addKnowledge(Knowledge knowledge, int x, int y) {
		KnowledgeWidget knowledgeWidget = new KnowledgeWidget(x, y, screen, knowledge);
		this.addWidget(knowledgeWidget);
	}

	private void addWidget(KnowledgeWidget widget) {
		this.widgets.add(widget);
	}


}
