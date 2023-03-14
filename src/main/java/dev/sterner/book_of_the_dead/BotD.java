package dev.sterner.book_of_the_dead;

import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.CorpseDataComponent;
import dev.sterner.book_of_the_dead.common.event.BotDItemGroupEvents;
import dev.sterner.book_of_the_dead.common.event.BotDUseEvents;
import dev.sterner.book_of_the_dead.common.registry.*;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.entity.event.api.ServerPlayerEntityCopyCallback;

import java.util.Optional;

public class BotD implements ModInitializer {

	static final boolean DEBUG_MODE = true;
	public static boolean isDebugMode() {
		return DEBUG_MODE && QuiltLoader.isDevelopmentEnvironment();
	}

	@Override
	public void onInitialize(ModContainer mod) {
		BotDObjects.init();
		BotDEntityTypes.init();
		BotDBlockEntityTypes.init();
		BotDEnchantments.init();
		BotDRecipeTypes.init();
		BotDWorldGenerators.init();
		BotDTrades.init();
		BotDRituals.init();
		BotDSoundEvents.init();

		BotDUseEvents.init();
		BotDItemGroupEvents.init();

		ServerPlayerEntityCopyCallback.EVENT.register(this::afterRespawn);
	}

	private void afterRespawn(ServerPlayerEntity serverPlayerEntity, ServerPlayerEntity serverPlayerEntity1, boolean b) {
		Optional<CorpseDataComponent> component = BotDComponents.CORPSE_COMPONENT.maybeGet(serverPlayerEntity);
		if(component.isPresent() && component.get().isCorpse){
			component.get().isCorpse(false);
		}
	}
}
