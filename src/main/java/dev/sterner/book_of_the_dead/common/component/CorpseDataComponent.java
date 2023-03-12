package dev.sterner.book_of_the_dead.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

public class CorpseDataComponent implements AutoSyncedComponent {
	private final LivingEntity livingEntity;
	public boolean isCorpse = false;

	public CorpseDataComponent(LivingEntity livingEntity) {
		this.livingEntity = livingEntity;
	}

	public void isCorpse(boolean isCorpse){
		this.isCorpse = isCorpse;
		BotDComponents.CORPSE_COMPONENT.sync(this.livingEntity);
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putBoolean(Constants.Nbt.IS_CORPSE, isCorpse);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		this.isCorpse = nbt.getBoolean(Constants.Nbt.IS_CORPSE);
	}
}
