package dev.sterner.book_of_the_dead.common.ritual;

import dev.sterner.book_of_the_dead.common.block.entity.RitualBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class CreateItemRitual extends ConsumeItemsRitual {
	public CreateItemRitual(Identifier id, Identifier largeCircleSprite, Identifier smallCircleSprite) {
		super(id, largeCircleSprite, smallCircleSprite);
		height = 1;
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
				for(ItemStack output : recipe.outputs){
					ItemScatterer.spawn(world, x, y, z, output);
				}
			}
		}
		super.onStopped(world, blockPos, blockEntity);
	}
}
