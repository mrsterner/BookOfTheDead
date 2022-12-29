package dev.sterner.book_of_the_dead.common.ritual;

import dev.sterner.book_of_the_dead.api.NecrotableRitual;
import dev.sterner.book_of_the_dead.common.block.entity.RitualBlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StatusEffectRitual extends ConsumeItemsRitual {
	public StatusEffectRitual(Identifier id, Identifier largeCircleSprite, Identifier smallCircleSprite) {
		super(id, largeCircleSprite, smallCircleSprite);
	}

	@Override
	public void onStopped(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		if(user != null){
			PlayerEntity player = world.getPlayerByUuid(user);
			if(player != null && recipe.statusEffectInstance != null){
				player.addStatusEffect(recipe.statusEffectInstance);
			}
		}
		super.onStopped(world, blockPos, blockEntity);
	}
}
