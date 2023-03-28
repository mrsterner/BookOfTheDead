package dev.sterner.book_of_the_dead.common.rituals;

import dev.sterner.book_of_the_dead.api.BotDApi;
import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.PlayerDataComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class KakuzuNecrotableRitual extends BasicNecrotableRitual {
	public KakuzuNecrotableRitual(Identifier id) {
		super(id);
	}

	@Override
	public void onStopped(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
		super.onStopped(world, blockPos, blockEntity);
		if (ritualManager.contract.get(0) != 0) {
			Entity entity = world.getEntityById(ritualManager.contract.get(0));
			if (entity instanceof PlayerEntity player) {
				PlayerDataComponent component = BotDComponents.PLAYER_COMPONENT.get(player);
				if (BotDApi.canHaveKakuzu(player)) {
					component.increaseKakuzuBuffLevel();
				}
			}
		}
	}
}
