package dev.sterner.book_of_the_dead.common.block.entity;

import dev.sterner.book_of_the_dead.common.registry.BotDBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class BotDSkullBlockEntity extends BlockEntity {
	public BotDSkullBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.HEAD, pos, state);
	}
}
