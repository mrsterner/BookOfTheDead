package dev.sterner.legemeton.mixin;

import dev.sterner.legemeton.common.util.LegemetonUtils;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructurePools.class)
public class StructurePoolsMixin {
	@Inject(method = "register", at = @At("HEAD"))
	private static void legemeton$register(StructurePool pool, CallbackInfoReturnable<DynamicRegistryManager.RegistryEntry<StructurePool>> cir) {
		//LegemetonUtils.tryAddElementToPool(new Identifier("village/common/"), pool, "legemeton:village/common/houses/old_house", StructurePool.Projection.RIGID, 2);

		LegemetonUtils.tryAddElementToPool(new Identifier("village/plains/houses"), pool, "legemeton:village/plains/houses/old_house", StructurePool.Projection.RIGID, 2);
		LegemetonUtils.tryAddElementToPool(new Identifier("village/desert/houses"), pool, "legemeton:village/desert/houses/old_house", StructurePool.Projection.RIGID, 2);
		LegemetonUtils.tryAddElementToPool(new Identifier("village/savanna/houses"), pool, "legemeton:village/savanna/houses/old_house", StructurePool.Projection.RIGID, 2);
		LegemetonUtils.tryAddElementToPool(new Identifier("village/taiga/houses"), pool, "legemeton:village/taiga/houses/old_house", StructurePool.Projection.RIGID, 2);
		LegemetonUtils.tryAddElementToPool(new Identifier("village/snowy/houses"), pool, "legemeton:village/snowy/houses/old_house", StructurePool.Projection.RIGID, 2);



	}
}
