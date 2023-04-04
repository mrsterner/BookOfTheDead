package dev.sterner.book_of_the_dead.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.sterner.book_of_the_dead.api.KnowledgeData;
import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.api.Knowledge;
import dev.sterner.book_of_the_dead.client.screen.tag.KnowledgeTab;
import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.PlayerKnowledgeComponent;
import dev.sterner.book_of_the_dead.common.util.RenderUtils;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.ChatNarratorManager;
import net.minecraft.client.util.math.MatrixStack;

import java.util.List;
import java.util.Set;

public class KnowledgeWidget extends ClickableWidget {
	public Knowledge knowledge;
	public KnowledgeTab tab;
	public float x;
	public float y;
	public Set<KnowledgeData> knowledgeDataList;

	public KnowledgeWidget(float x, float y, KnowledgeTab tab, Knowledge knowledge) {
		super((int) x, (int) y, 17, 17, ChatNarratorManager.NO_TITLE);
		this.tab = tab;
		this.knowledge = knowledge;
		this.x = x;
		this.y = y;
		this.knowledgeDataList = BotDComponents.KNOWLEDGE_COMPONENT.get(tab.player).getKnowledgeData();
	}

	@Override
	public void drawWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		List<Knowledge> knowledgeList = knowledgeDataList.stream().map(KnowledgeData::knowledge).toList();
		boolean isActivated = knowledgeList.contains(knowledge);
		boolean shouldRender = true;

		if (!knowledge.children.isEmpty()) {
			for (Knowledge child : knowledge.children) {
				if (!knowledgeList.contains(child)) {
					shouldRender = false;
					break;
				}
			}
		}

		if (shouldRender) {
			RenderSystem.setShaderTexture(0, knowledge.icon);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, isHovered() || isActivated ? this.alpha : 0.25F);
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.enableDepthTest();
			// Render the screen texture

			enableScissor((tab.width - 192) / 4 - 5, tab.scissorY + 13, tab.scissorWidth + (tab.width - 192) / 4 - 5 - 61, tab.scissorHeight + 1);
			matrices.push();
			matrices.translate(tab.xOffset, tab.yOffset, 0.0F);
			RenderUtils.drawTexture(matrices, this.x + (float) tab.xOffset, this.y + (float) tab.yOffset, 0, 0, this.width, this.height, 17, 17);
			matrices.pop();
			disableScissor();
		}
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.hovered = mouseX >= this.getX() + tab.xOffset * 2 && mouseY >= this.getY() + tab.yOffset * 2 && mouseX < this.getX() + tab.xOffset * 2 + this.width && mouseY < this.getY() + tab.yOffset * 2 + this.height;
		this.drawWidget(matrices, mouseX, mouseY, delta);
	}

	@Override
	protected boolean clicked(double mouseX, double mouseY) {
		if (!this.active && !this.visible) {
			return false;
		}

		double halfWidth = (double) this.width / 2;
		double halfHeight = (double) this.height / 2;
		double centerX = this.getX() + halfWidth + tab.xOffset * 2;
		double centerY = this.getY() + halfHeight + tab.yOffset * 2;
		double radius = Math.min(halfWidth, halfHeight);

		double dx = Math.abs(mouseX - centerX);
		double dy = Math.abs(mouseY - centerY);

		if (dx > radius || dy > radius) {
			return false;
		}
		return dx + dy <= radius;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return super.isMouseOver(mouseX, mouseY);
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		if (isValidClickButton(0)) {
			PlayerKnowledgeComponent component = BotDComponents.KNOWLEDGE_COMPONENT.get(tab.player);
			component.addKnowledge(knowledge);
		}
	}

	@Override
	protected void updateNarration(NarrationMessageBuilder builder) {

	}
}
