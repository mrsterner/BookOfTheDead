package dev.sterner.book_of_the_dead.api.block;

import dev.sterner.book_of_the_dead.common.block.BotDSkullBlock;
import dev.sterner.book_of_the_dead.common.block.entity.BotDSkullBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.Equippable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public abstract class AbstractBotDSkullBlock extends BlockWithEntity implements Equippable {
	private final BotDSkullBlock.BotDType type;

	public AbstractBotDSkullBlock(BotDSkullBlock.BotDType type, AbstractBlock.Settings settings) {
		super(settings);
		this.type = type;
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new BotDSkullBlockEntity(pos, state);
	}

	public BotDSkullBlock.BotDType getSkullType() {
		return this.type;
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
}
