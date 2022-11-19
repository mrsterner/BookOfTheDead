package dev.sterner.legemeton.mixin;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(StructurePool.class)
public interface StructurePoolAccessor {
	@Accessor(value = "elements")
	ObjectArrayList<StructurePoolElement> legemeton$getElements();

	@Accessor(value = "elementCounts")
	List<Pair<StructurePoolElement, Integer>> legemeton$getElementCounts();
}
