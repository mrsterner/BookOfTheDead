package dev.sterner.legemeton.common.block.entity;

import dev.sterner.legemeton.common.registry.LegemetonBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class NecroTableBlockEntity extends BlockEntity {
	public NecroTableBlockEntity(BlockPos pos, BlockState state) {
		super(LegemetonBlockEntityTypes.NECRO, pos, state);
	}
}
