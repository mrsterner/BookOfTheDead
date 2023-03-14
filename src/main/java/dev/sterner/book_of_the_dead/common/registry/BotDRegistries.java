package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.api.NecrotableRitual;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;

public interface BotDRegistries {
	Registry<NecrotableRitual> NECROTABLE_RITUALS = FabricRegistryBuilder.createSimple(NecrotableRitual.class, Constants.id("necro_rituals")).buildAndRegister();
}
