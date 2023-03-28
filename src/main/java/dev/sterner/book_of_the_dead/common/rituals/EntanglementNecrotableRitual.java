package dev.sterner.book_of_the_dead.common.rituals;

import dev.sterner.book_of_the_dead.api.BotDApi;
import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.LivingEntityDataComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class EntanglementNecrotableRitual extends BasicNecrotableRitual {
	public EntanglementNecrotableRitual(Identifier id) {
		super(id);
	}

	@Override
	public void onStopped(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
		super.onStopped(world, blockPos, blockEntity);
		if (ritualManager.contract.get(0) != 0 && ritualManager.contract.get(1) != 0) {
			Entity entity = world.getEntityById(ritualManager.contract.get(0));
			Entity entity2 = world.getEntityById(ritualManager.contract.get(1));
			if (entity instanceof LivingEntity livingEntity && entity2 instanceof LivingEntity livingEntity2) {
				if (world instanceof ServerWorld serverWorld) {
					if (!serverWorld.isChunkLoaded(livingEntity.getChunkPos().x, livingEntity.getChunkPos().z)) {
						serverWorld.setChunkForced(livingEntity.getChunkPos().x, livingEntity.getChunkPos().z, true);
					}
					if (!serverWorld.isChunkLoaded(livingEntity2.getChunkPos().x, livingEntity2.getChunkPos().z)) {
						serverWorld.setChunkForced(livingEntity2.getChunkPos().x, livingEntity2.getChunkPos().z, true);
					}
				}
				LivingEntityDataComponent livingComponent = BotDComponents.LIVING_COMPONENT.get(livingEntity);
				LivingEntityDataComponent livingComponent2 = BotDComponents.LIVING_COMPONENT.get(livingEntity2);
				if (BotDApi.canEntangle(livingEntity) && BotDApi.canEntangle(livingEntity2)) {
					livingComponent.setEntangledEntityId(livingEntity2.getId());
					livingComponent2.setEntangledEntityId(livingEntity.getId());
				}
			}

		}
	}
}
