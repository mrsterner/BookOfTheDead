package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.client.screen.book.Knowledge;
import dev.sterner.book_of_the_dead.common.rituals.BasicNecrotableRitual;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public interface BotDRegistries {
	RegistryKey<Registry<BasicNecrotableRitual>> RITUAL = RegistryKey.ofRegistry(Constants.id("ritual"));
	RegistryKey<Registry<Knowledge>> KNOWLEDGE_KEY = RegistryKey.ofRegistry(Constants.id("knowledge"));

	Registry<BasicNecrotableRitual> NECROTABLE_RITUALS = FabricRegistryBuilder.createSimple(RITUAL).buildAndRegister();
	Registry<Knowledge> KNOWLEDGE = FabricRegistryBuilder.createSimple(KNOWLEDGE_KEY).buildAndRegister();
}
