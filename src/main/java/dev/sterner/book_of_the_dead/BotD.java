package dev.sterner.book_of_the_dead;

import dev.sterner.book_of_the_dead.common.event.UseEvents;
import dev.sterner.book_of_the_dead.common.registry.*;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

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

		UseEvents.init();

		LootTableEvents.MODIFY.register(this::injectCinnabar);
	}


	private void injectCinnabar(ResourceManager resourceManager, LootManager lootManager, Identifier identifier, LootTable.Builder builder, LootTableSource lootTableSource) {
		if(Blocks.DEEPSLATE_REDSTONE_ORE.getLootTableId().equals(identifier) && lootTableSource.isBuiltin()){
			LootPool.Builder poolBuilder = LootPool.builder()
					.conditionally(SurvivesExplosionLootCondition.builder())
					.with(ItemEntry.builder(BotDObjects.CINNABAR)
							.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(-2.0F, 1.0F))));
			builder.pool(poolBuilder);
		}
	}
}
