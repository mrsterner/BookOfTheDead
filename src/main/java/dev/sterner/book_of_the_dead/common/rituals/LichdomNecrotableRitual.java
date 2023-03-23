package dev.sterner.book_of_the_dead.common.rituals;

import dev.sterner.book_of_the_dead.api.BotDApi;
import dev.sterner.book_of_the_dead.api.NecrotableRitual;
import dev.sterner.book_of_the_dead.common.block.entity.RitualBlockEntity;
import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.PlayerDataComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class LichdomNecrotableRitual extends NecrotableRitual {
	public LichdomNecrotableRitual(Identifier id) {
		super(id);
	}

	@Override
	public void onStopped(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		super.onStopped(world, blockPos, blockEntity);
		if(contract != 0){
			Entity entity = world.getEntityById(contract);
			if(entity instanceof PlayerEntity player){
				PlayerDataComponent component = BotDComponents.PLAYER_COMPONENT.get(player);
				if(BotDApi.canHaveLichdom(player)){
					component.setLich(true);
				}
			}
		}
	}
}
