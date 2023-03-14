package dev.sterner.book_of_the_dead.common.ritual;

import dev.sterner.book_of_the_dead.api.NecrotableRitual;
import dev.sterner.book_of_the_dead.common.block.entity.RitualBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CreateItemRitual extends NecrotableRitual {

	public CreateItemRitual(Identifier id) {
		super(id);
	}

	@Override
	public void onStopped(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		double x = blockPos.getX() + 0.5;
		double y = blockPos.getY() + 0.5;
		double z = blockPos.getZ() + 0.5;

		if(world.getBlockEntity(blockPos) instanceof RitualBlockEntity ritualBlockEntity){
			if(ritualBlockEntity.currentNecrotableRitual != null && recipe != null){
				if(world instanceof ServerWorld serverWorld){
					serverWorld.playSound(null, x, y, z, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1F,1F);
				}
				if (recipe.outputs != null) {
					for(ItemStack output : recipe.outputs){
						ItemScatterer.spawn(world, x, y, z, output);
					}
				}
			}
		}
		super.onStopped(world, blockPos, blockEntity);
	}
}
