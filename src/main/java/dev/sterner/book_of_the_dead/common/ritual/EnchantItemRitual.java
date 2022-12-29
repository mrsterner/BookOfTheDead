package dev.sterner.book_of_the_dead.common.ritual;

import dev.sterner.book_of_the_dead.api.NecrotableRitual;
import dev.sterner.book_of_the_dead.common.block.entity.RitualBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class EnchantItemRitual extends ConsumeItemsRitual {
	public EnchantItemRitual(Identifier id, Identifier largeCircleSprite, Identifier smallCircleSprite) {
		super(id, largeCircleSprite, smallCircleSprite);
		height = 0;
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
				Optional<ItemEntity> items = world.getEntitiesByType(EntityType.ITEM, new Box(new BlockPos(x,y,z)).expand(2, 0, 2), entity -> true).stream().findFirst();
				if(items.isPresent()){
					if(world instanceof ServerWorld serverWorld){
						for (int i = 0; i < 16; i++) {
							serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER,
									items.get().getX() + ((world.random.nextDouble() / 2) - 0.25),
									items.get().getY() + ((world.random.nextDouble() / 2) - 0.25),
									items.get().getZ() + ((world.random.nextDouble() / 2) - 0.25),
									0,
									1 * ((world.random.nextDouble() / 2) - 0.25),
									1 * ((world.random.nextDouble() / 2) - 0.25),
									1 * ((world.random.nextDouble() / 2) - 0.25),
									0);
						}
					}
					ItemStack out = items.get().getStack();
					out.addEnchantment(recipe.enchantment, recipe.enchantmentLevel);
				}
			}
		}
		super.onStopped(world, blockPos, blockEntity);
	}
}
