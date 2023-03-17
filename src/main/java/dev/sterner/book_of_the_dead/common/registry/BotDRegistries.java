package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.api.NecrotableRitual;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public interface BotDRegistries {
	RegistryKey<Registry<NecrotableRitual>> RITUAL = RegistryKey.ofRegistry(Constants.id("ritual"));

	Registry<NecrotableRitual> NECROTABLE_RITUALS = FabricRegistryBuilder.createSimple(RITUAL).buildAndRegister();
}
