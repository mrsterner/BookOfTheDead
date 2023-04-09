package dev.sterner.book_of_the_dead.common.block.entity;

import dev.sterner.book_of_the_dead.api.block.entity.BaseBlockEntity;
import dev.sterner.book_of_the_dead.common.registry.BotDBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class BookBlockEntity extends BaseBlockEntity {
	public BookBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.BOOK, pos, state);
	}
}
