package dev.sterner.book_of_the_dead.common.ritual;

import dev.sterner.book_of_the_dead.api.NecrotableRitual;
import dev.sterner.book_of_the_dead.common.block.entity.PedestalBlockEntity;
import dev.sterner.book_of_the_dead.common.block.entity.RitualBlockEntity;
import dev.sterner.book_of_the_dead.common.recipe.RitualRecipe;
import dev.sterner.book_of_the_dead.common.registry.BotDSoundEvents;
import net.minecraft.entity.TntEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CreateItemRitual extends NecrotableRitual {
	int index = 0;
	public CreateItemRitual(Identifier id, Identifier largeCircleSprite, Identifier smallCircleSprite) {
		super(id, largeCircleSprite, smallCircleSprite);
	}

	@Override
	public void tick(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		double x = blockPos.getX() + 0.5;
		double y = blockPos.getY() + 1.5;
		double z = blockPos.getZ() + 0.5;
		List<BlockPos> pedestalToActivate = new ArrayList<>();
		List<Pair<ItemStack, BlockPos>> stream = blockEntity.getPedestalInfo(world).stream().filter(itemStackBlockPosPair -> !itemStackBlockPosPair.getLeft().isEmpty()).toList();
		int dividedTime = recipe.getDuration();
		if(stream.size() > 0){
			dividedTime = (dividedTime / (stream.size() + 1));
		}

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
		if(ticker == 1 || (ticker + 1) % dividedTime == 0){
			if(index < pedestalToActivate.size() && world.getBlockEntity(pedestalToActivate.get(index)) instanceof PedestalBlockEntity pedestalBlockEntity){
				world.playSound(null, pedestalToActivate.get(index).getX(), pedestalToActivate.get(index).getY(), pedestalToActivate.get(index).getZ(), BotDSoundEvents.MISC_ITEM_BEAM, SoundCategory.BLOCKS, 0.75f, 0.75f * world.random.nextFloat() / 2);
				pedestalBlockEntity.setCrafting(true);
				pedestalBlockEntity.duration = dividedTime;
			}
			index++;
		}
		if(world instanceof ServerWorld serverWorld) {
			if(ticker % 5 == 0 && ticker < recipe.getDuration() - 40){
				serverWorld.playSound(null, x,y,z, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 10,0.5f);
			}
			for (int i = 0; i < 8; i++) {
				serverWorld.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, recipe.output),
						x + ((world.random.nextDouble() / 2) - 0.25),
						y + ((world.random.nextDouble() / 2) - 0.25),
						z + ((world.random.nextDouble() / 2) - 0.25),
						0,
						1 * ((world.random.nextDouble() / 2) - 0.25),
						1 * ((world.random.nextDouble() / 2) - 0.25),
						1 * ((world.random.nextDouble() / 2) - 0.25),
						0);
			}
		}

		super.tick(world, blockPos, blockEntity);
	}

	@Override
	public void onStopped(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		index = 0;
		ticker = 0;
		double x = blockPos.getX() + 0.5;
		double y = blockPos.getY() + 0.5;
		double z = blockPos.getZ() + 0.5;

		if(world.getBlockEntity(blockPos) instanceof RitualBlockEntity ritualBlockEntity){
			if(ritualBlockEntity.currentNecrotableRitual != null && recipe != null){
				if(world instanceof ServerWorld serverWorld){
					serverWorld.playSound(null, x, y, z, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1F,1F);
				}
				ItemScatterer.spawn(world, x, y, z, recipe.output.copy());
			}
		}

		super.onStopped(world, blockPos, blockEntity);
	}
}
