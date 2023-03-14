package dev.sterner.book_of_the_dead.mixin;

import dev.sterner.book_of_the_dead.common.util.BotDUtils;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructurePools.class)
public class StructurePoolsMixin {
	@Inject(method = "register", at = @At("HEAD"))
	private static void book_of_the_dead$register(StructurePool pool, CallbackInfoReturnable<DynamicRegistryManager.RegistryEntry<StructurePool>> cir) {
		BotDUtils.tryAddElementToPool(new Identifier("village/plains/houses"), pool, "book_of_the_dead:village/plains/houses/old_house", StructurePool.Projection.RIGID, 2);
		BotDUtils.tryAddElementToPool(new Identifier("village/desert/houses"), pool, "book_of_the_dead:village/desert/houses/old_house", StructurePool.Projection.RIGID, 2);
		BotDUtils.tryAddElementToPool(new Identifier("village/savanna/houses"), pool, "book_of_the_dead:village/savanna/houses/old_house", StructurePool.Projection.RIGID, 2);
		BotDUtils.tryAddElementToPool(new Identifier("village/taiga/houses"), pool, "book_of_the_dead:village/taiga/houses/old_house", StructurePool.Projection.RIGID, 2);
		BotDUtils.tryAddElementToPool(new Identifier("village/snowy/houses"), pool, "book_of_the_dead:village/snowy/houses/old_house", StructurePool.Projection.RIGID, 2);
	}
}
