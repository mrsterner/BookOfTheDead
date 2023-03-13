package dev.sterner.book_of_the_dead.common.ritual;

import dev.sterner.book_of_the_dead.api.NecrotableRitual;
import dev.sterner.book_of_the_dead.common.block.entity.PedestalBlockEntity;
import dev.sterner.book_of_the_dead.common.block.entity.RitualBlockEntity;
import dev.sterner.book_of_the_dead.common.registry.BotDSoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ConsumeItemsRitual extends NecrotableRitual {
	private int index = 0;
	public ConsumeItemsRitual(Identifier id) {
		super(id);
	}

	@Override
	public void tick(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		double x = blockPos.getX() + 0.5;
		double y = blockPos.getY() + 0.5 + height;
		double z = blockPos.getZ() + 0.5;
		List<BlockPos> pedestalToActivate = new ArrayList<>();
		List<Pair<ItemStack, BlockPos>> stream = blockEntity.getPedestalInfo(world).stream().filter(itemStackBlockPosPair -> !itemStackBlockPosPair.getLeft().isEmpty()).toList();
		int dividedTime = recipe.getDuration();
		if(stream.size() > 0){
			dividedTime = (dividedTime / (stream.size() + 1));
		}

		for (Pair<ItemStack, BlockPos> itemStackBlockPosPair : stream) {
			if (recipe.inputs != null) {
				for(Ingredient ingredient : recipe.inputs){
					if (ingredient.test(itemStackBlockPosPair.getLeft())) {
						BlockPos checkPos = itemStackBlockPosPair.getRight().add(blockPos.getX(), blockPos.getY(), blockPos.getZ());
						if(world.getBlockEntity(checkPos) instanceof PedestalBlockEntity){
							pedestalToActivate.add(checkPos);
						}
					}
				}
			}
		}
		if(ticker == 1 || (ticker + 1) % dividedTime == 0){
			if(index < pedestalToActivate.size() && world.getBlockEntity(pedestalToActivate.get(index)) instanceof PedestalBlockEntity pedestalBlockEntity){
				world.playSound(null, pedestalToActivate.get(index).getX(), pedestalToActivate.get(index).getY(), pedestalToActivate.get(index).getZ(), BotDSoundEvents.MISC_ITEM_BEAM, SoundCategory.BLOCKS, 0.75f, 0.75f * world.random.nextFloat() / 2);
				pedestalBlockEntity.setCrafting(true);
				pedestalBlockEntity.duration = dividedTime;
				pedestalBlockEntity.targetY = height;
			}
			index++;
		}

		if(world instanceof ServerWorld serverWorld) {
			generateFX(serverWorld, x, y, z);

		}
		super.tick(world, blockPos, blockEntity);
	}

	@Override
	public void onStopped(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		index = 0;
		super.onStopped(world, blockPos, blockEntity);
	}

	private void generateFX(ServerWorld serverWorld, double x, double y, double z) {
		if(ticker % 5 == 0 && ticker < recipe.getDuration() - 40){
			serverWorld.playSound(null, x,y,z, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 10,0.5f);
		}
		if (recipe.outputs != null) {
			for(ItemStack output : recipe.outputs){
				for (int i = 0; i < recipe.outputs.size() * 2; i++) {
					serverWorld.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, output),
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
		}
	}
}
