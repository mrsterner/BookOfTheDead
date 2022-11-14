package dev.sterner.legemeton.common.block.entity;

import dev.sterner.legemeton.common.registry.LegemetonBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class HookBlockEntity extends BlockEntity {
	public HookBlockEntity(BlockPos pos, BlockState state) {
		super(LegemetonBlockEntityTypes.HOOK, pos, state);
	}


}
