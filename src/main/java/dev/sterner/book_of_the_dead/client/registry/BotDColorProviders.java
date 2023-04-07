package dev.sterner.book_of_the_dead.client.registry;

import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public interface BotDColorProviders {

	static void init() {
		ColorProviderRegistry.ITEM.register((itemStack, index) -> {
			if (index == 0) {
				if (itemStack.hasNbt() && itemStack.getOrCreateNbt().contains(Constants.Nbt.STATUS_EFFECT_INSTANCE)) {
					NbtCompound nbt = itemStack.getSubNbt(Constants.Nbt.STATUS_EFFECT_INSTANCE);
					if (nbt != null) {
						StatusEffect effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(nbt.getString(Constants.Nbt.STATUS_EFFECT)));
						if (effect != null) {
							return effect.getColor();
						}
					}
				}
			}
			return -1;
		}, BotDObjects.SYRINGE);
	}
}
