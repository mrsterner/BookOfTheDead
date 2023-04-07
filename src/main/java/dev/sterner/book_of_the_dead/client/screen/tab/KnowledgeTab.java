package dev.sterner.book_of_the_dead.client.screen.tab;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.sterner.book_of_the_dead.client.screen.BookEntry;
import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import dev.sterner.book_of_the_dead.client.screen.page.HeadlineBookPage;
import dev.sterner.book_of_the_dead.client.screen.page.TextPage;
import dev.sterner.book_of_the_dead.client.screen.widget.BotDWidget;
import dev.sterner.book_of_the_dead.client.screen.widget.KnowledgeWidget;
import dev.sterner.book_of_the_dead.client.screen.widget.NavigationWidget;
import dev.sterner.book_of_the_dead.common.registry.BotDKnowledgeRegistry;
import dev.sterner.book_of_the_dead.common.util.Constants;
import dev.sterner.book_of_the_dead.common.util.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class KnowledgeTab extends BookTab {
	public static final Identifier TAB_TEXTURE = Constants.id("textures/gui/knowledge_tab.png");
	public final PlayerEntity player;
	public static final Identifier PARALLAX = Constants.id("textures/gui/parallax.png");
	public double xOffset = 0;
	public double yOffset = 0;
	public int scissorX;
	public int scissorY;
	public int scissorWidth;
	public int scissorHeight;

	public KnowledgeTab(BookOfTheDeadScreen screen) {
		super(screen, List.of(Constants.id("textures/gui/background/knowledge_tab.png")));
		this.player = screen.player;
		this.scissorX = 50;
		this.scissorY = 30;
		this.scissorWidth = 122 + scissorX;
		this.scissorHeight = 172 + scissorY;
	}

	@Override
	public void init() {
		ENTRIES.add(BookEntry.of()
			.addPage(TextPage.of())
			.addPage(HeadlineBookPage.of("knowledge", "knowledge.1"))
			.addPage(HeadlineBookPage.of("knowledge.2"))
		);

		float x = (float) (width - 192) / 4 + 9 * 5 - 4;
		float y = 18 * 6 + 10;
		//Void
		widgets.add(new KnowledgeWidget(x - 9, y + 15, this, BotDKnowledgeRegistry.VOID, 0));
		widgets.add(new KnowledgeWidget(x + 9, y + 15, this, BotDKnowledgeRegistry.AMALGAM, 0));
		widgets.add(new KnowledgeWidget(x, y + 12 + 18, this, BotDKnowledgeRegistry.DISPOSITION, 0));
		widgets.add(new KnowledgeWidget(x, y + 12 + 18 * 2, this, BotDKnowledgeRegistry.BALANCE, 0));
		widgets.add(new KnowledgeWidget(x, y + 12 + 18 * 3, this, BotDKnowledgeRegistry.PROJECTION, 0));
		//Core
		widgets.add(new KnowledgeWidget(x, y, this, BotDKnowledgeRegistry.ALCHEMY, 0));
		//Main
		widgets.add(new KnowledgeWidget(x - 9, y - 15, this, BotDKnowledgeRegistry.CALCINATION, 0));
		widgets.add(new KnowledgeWidget(x + 9, y - 15, this, BotDKnowledgeRegistry.DISSOLUTION, 0));
		widgets.add(new KnowledgeWidget(x, y - 12 - 18, this, BotDKnowledgeRegistry.SEPARATION, 0));
		widgets.add(new KnowledgeWidget(x + 9, y - 15 - 30, this, BotDKnowledgeRegistry.CONJUNCTION, 0));
		widgets.add(new KnowledgeWidget(x - 9, y - 15 - 30, this, BotDKnowledgeRegistry.FERMENTATION, 0));
		widgets.add(new KnowledgeWidget(x, y - 12 - 18 - 30, this, BotDKnowledgeRegistry.DISTILLATION, 0));
		widgets.add(new KnowledgeWidget(x, y - 12 - 18 * 2 - 30, this, BotDKnowledgeRegistry.COAGULATION, 0));
		widgets.add(new KnowledgeWidget(x, y - 12 - 18 * 3 - 30, this, BotDKnowledgeRegistry.PHILOSOPHER, 0));
		//Soul
		widgets.add(new KnowledgeWidget(x + 18, y, this, BotDKnowledgeRegistry.SOUL, 0));
		widgets.add(new KnowledgeWidget(x + 9 + 18, y - 15, this, BotDKnowledgeRegistry.ESSENCE, 0));
		widgets.add(new KnowledgeWidget(x + 18, y - 12 - 18, this, BotDKnowledgeRegistry.FUSION, 0));
		widgets.add(new KnowledgeWidget(x + 9 + 18, y - 15 - 30, this, BotDKnowledgeRegistry.MULTIPLICATION, 0));
		widgets.add(new KnowledgeWidget(x + 18 + 18, y - 12 - 18 - 30, this, BotDKnowledgeRegistry.LIFE, 0));
		//Infernal
		widgets.add(new KnowledgeWidget(x - 18, y, this, BotDKnowledgeRegistry.BRIMSTONE, 0));
		widgets.add(new KnowledgeWidget(x - 9 - 18, y - 15, this, BotDKnowledgeRegistry.ASH, 0));
		widgets.add(new KnowledgeWidget(x - 18, y - 12 - 18, this, BotDKnowledgeRegistry.MELT, 0));
		widgets.add(new KnowledgeWidget(x - 9 - 18, y - 15 - 30, this, BotDKnowledgeRegistry.ROT, 0));
		widgets.add(new KnowledgeWidget(x - 18 - 18, y - 12 - 18 - 30, this, BotDKnowledgeRegistry.CADUCEUS, 0));
	}

	@Override
	public boolean move(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if(this.grouping != 0){
			return false;
		}

		if (!this.isDragging) {
			// Check if the mouse is within the screen texture
			if (mouseX >= (double) (this.width - 192) / 4 - 16 && mouseX <= (double) (this.width - 192) / 4 - 16 + 122 && mouseY >= this.yOffset && mouseY <= this.scissorHeight) {
				this.isDragging = true;
			}
		}

		if (this.isDragging) {
			// Update the position of the screen
			this.xOffset = MathHelper.clamp(this.xOffset + deltaX, -20, 20);
			this.yOffset = MathHelper.clamp(this.yOffset + deltaY, -20, 20);


			return true;
		}
		return false;
	}

	@Override
	public void render(MatrixStack matrices, int width, int mouseX, int mouseY, float delta) {
		int scaledWidth = (width - 192) / 4 - 5;

		if(this.grouping == 0){
			matrices.push();
			RenderUtils.renderTexture(matrices, TAB_TEXTURE, (width - 192) / 4 - 16, 32, 0, 0, 272, 182, 512, 256);
			matrices.pop();

			RenderSystem.setShaderTexture(0, PARALLAX);
			enableScissor(scaledWidth, this.scissorY + 13, this.scissorWidth + scaledWidth - 61, this.scissorHeight + 1);
			matrices.push();
			matrices.translate(xOffset, yOffset, 0.0F);

			drawTexture(matrices, 0, 0, 300.0F, 200.0F, 2507, 1205, 2507, 1205);

			matrices.pop();
			disableScissor();
		}

		super.render(matrices, width, mouseX, mouseY, delta);
	}
}
