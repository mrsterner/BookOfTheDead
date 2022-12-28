package dev.sterner.book_of_the_dead.common.ritual;

import dev.sterner.book_of_the_dead.api.NecrotableRitual;
import dev.sterner.book_of_the_dead.common.block.entity.PedestalBlockEntity;
import dev.sterner.book_of_the_dead.common.block.entity.RitualBlockEntity;
import dev.sterner.book_of_the_dead.common.recipe.RitualRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CreateItemRitual extends NecrotableRitual {
	public RitualRecipe recipe;
	public int ticker = 0;
	int index = 0;
	public CreateItemRitual(Identifier id, Identifier largeCircleSprite, Identifier smallCircleSprite, int duration) {
		super(id, largeCircleSprite, smallCircleSprite, duration);
	}

	@Override
	public void tick(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		List<BlockPos> pedestalToActivate = new ArrayList<>();
		List<Pair<ItemStack, BlockPos>> stream = blockEntity.getPedestalInfo(world).stream().filter(itemStackBlockPosPair -> !itemStackBlockPosPair.getLeft().isEmpty()).toList();
		ticker++;
		int dividedTime = (duration / stream.size());

		for (Pair<ItemStack, BlockPos> itemStackBlockPosPair : stream) {
			for(Ingredient ingredient : recipe.ingredients){
				if (ingredient.test(itemStackBlockPosPair.getLeft())) {
					BlockPos checkPos = itemStackBlockPosPair.getRight().add(blockPos.getX(), blockPos.getY(), blockPos.getZ());
					if(world.getBlockEntity(checkPos) instanceof PedestalBlockEntity){
						pedestalToActivate.add(checkPos);
					}
				}
			}
		}

		if(ticker % dividedTime == 0){
			if(index <= pedestalToActivate.size() && world.getBlockEntity(pedestalToActivate.get(index)) instanceof PedestalBlockEntity pedestalBlockEntity){
				pedestalBlockEntity.setCrafting(true);
				pedestalBlockEntity.duration = dividedTime;
			}
			index++;
		}


		super.tick(world, blockPos, blockEntity);
	}

	@Override
	public void onStopped(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		index = 0;
		ticker = 0;
		if(world.getBlockEntity(blockPos) instanceof RitualBlockEntity ritualBlockEntity){
			if(ritualBlockEntity.currentNecrotableRitual != null && recipe != null){
				ItemScatterer.spawn(world, blockPos.getX(), blockPos.getY() + 1, blockPos.getZ(), recipe.output.copy());
			}
		}

		super.onStopped(world, blockPos, blockEntity);
	}
}
