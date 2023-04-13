package dev.sterner.book_of_the_dead.common.component;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.sterner.book_of_the_dead.common.util.Constants;
import dev.sterner.book_of_the_dead.common.util.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class PlayerAdviceComponent implements AutoSyncedComponent {
	private final Identifier texture = Constants.id("textures/gui/speech.png");
	public PlayerEntity player;
	private boolean isActive = false;
	private String activeAdvice = "I wouldn't do that if I were you";

	public PlayerAdviceComponent(PlayerEntity player){
		this.player = player;
	}

	public void drawTexture(MatrixStack matrixStack, float x, float y) {
		RenderSystem.setShaderTexture(0, texture);
		RenderUtils.drawTexture(matrixStack, x, y, 0, 0, 97, 40, 97, 40);
	}

	@Override
	public void readFromNbt(@NotNull NbtCompound nbt) {
		setActive(nbt.getBoolean(Constants.Nbt.ADVICE));
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound nbt) {
		nbt.putBoolean(Constants.Nbt.ADVICE, isActive());
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
		BotDComponents.ADVICE_COMPONENT.sync(this.player);
	}

	public String getActiveAdvice() {
		return this.activeAdvice;
	}

	public void setActiveAdvice(String string){
		this.activeAdvice = string;
		BotDComponents.ADVICE_COMPONENT.sync(this.player);
	}
}
