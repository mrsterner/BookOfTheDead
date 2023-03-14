package dev.sterner.book_of_the_dead.common.item;

import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.CorpseDataComponent;
import dev.sterner.book_of_the_dead.common.entity.PlayerCorpseEntity;
import dev.sterner.book_of_the_dead.common.registry.BotDEntityTypes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class DebugWandItem extends Item {
	public DebugWandItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		PlayerCorpseEntity entity = BotDEntityTypes.PLAYER_CORPSE_ENTITY.create(world);
		if (entity != null) {
			entity.setSkinProfile(user.getGameProfile());
			entity.refreshPositionAndAngles(user.getBlockPos(), 0, 0);
			world.spawnEntity(entity);
			CorpseDataComponent dataComponent2 = BotDComponents.CORPSE_COMPONENT.get(entity);
			dataComponent2.isCorpse(true);
			entity.damage(DamageSource.MAGIC, Integer.MAX_VALUE);
		}

		return super.use(world, user, hand);
	}
}
