package dev.sterner.book_of_the_dead.client.registry;

import dev.sterner.book_of_the_dead.client.renderer.item.AllBlackSwordItemRenderer;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

import java.util.List;

public interface BotDModelRegistry {

	static void init() {
		ModelPredicateProviderRegistry.register(BotDObjects.CONTRACT, new Identifier("signed"), (itemStack, clientWorld, livingEntity, i) -> livingEntity == null ? 0 : itemStack.hasNbt() && itemStack.getOrCreateNbt().contains(Constants.Nbt.CONTRACT) ? 1.0F : 0.0F);
		ModelPredicateProviderRegistry.register(BotDObjects.SYRINGE, new Identifier("filled"), (itemStack, clientWorld, livingEntity, i) -> livingEntity == null ? 0 : itemStack.hasNbt() && itemStack.getOrCreateNbt().contains(Constants.Nbt.STATUS_EFFECT_INSTANCE) ? 1.0F : 0.0F);
		ModelPredicateProviderRegistry.register(BotDObjects.SYRINGE, new Identifier("blood"), (itemStack, clientWorld, livingEntity, i) -> livingEntity == null ? 0 : itemStack.hasNbt() && itemStack.getOrCreateNbt().contains(Constants.Nbt.BLOOD) ? 1.0F : 0.0F);
		ModelPredicateProviderRegistry.register(BotDObjects.MEAT_CLEAVER, new Identifier("bloody"), (itemStack, clientWorld, livingEntity, i) -> livingEntity == null ? 0 : itemStack.hasNbt() && itemStack.getOrCreateNbt().contains(Constants.Nbt.BLOOD) ? 1.0F : 0.0F);

		for (Item item : List.of(BotDObjects.ALL_BLACK)) {
			Identifier allBlackId = Registries.ITEM.getId(item);
			AllBlackSwordItemRenderer allBlackItemRenderer = new AllBlackSwordItemRenderer(allBlackId);
			ResourceLoader.get(ResourceType.CLIENT_RESOURCES).registerReloader(allBlackItemRenderer);
			BuiltinItemRendererRegistry.INSTANCE.register(item, allBlackItemRenderer);
			ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
				out.accept(new ModelIdentifier(allBlackId.withPath(allBlackId.getPath() + "_gui"), "inventory"));
				out.accept(new ModelIdentifier(allBlackId.withPath(allBlackId.getPath() + "_handheld"), "inventory"));
			});
		}
	}
}
