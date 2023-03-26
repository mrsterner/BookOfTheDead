package dev.sterner.book_of_the_dead.common.block.entity;

import dev.sterner.book_of_the_dead.api.block.entity.BaseButcherBlockEntity;
import dev.sterner.book_of_the_dead.common.registry.BotDBlockEntityTypes;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HookBlockEntity extends BaseButcherBlockEntity {
	public int hookedAge = 0;

	public HookBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.HOOK, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState tickerState, HookBlockEntity blockEntity) {
		boolean mark = false;

		if (world != null && !world.isClient) {
			if (world.getTime() % 20 == 0 && !blockEntity.storedCorpseNbt.isEmpty()) {
				mark = true;
				if (blockEntity.hookedAge < Constants.Values.BLEEDING) {
					blockEntity.hookedAge++;
				} else {
					blockEntity.hookedAge = Constants.Values.BLEEDING;
				}
			}
			if (blockEntity.storedCorpseNbt.isEmpty()) {
				blockEntity.hookedAge = 0;
			}
		}
		if (mark) {
			markDirty(world, pos, tickerState);
		}
	}

	public ActionResult onUse(World world, BlockState state, BlockPos pos, PlayerEntity player, Hand hand, boolean isNeighbour) {
		return onUse(world, state, pos, player, hand, 0.25f, -1d, false);
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putInt(Constants.Nbt.HOOKED_AGE, this.hookedAge);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.hookedAge = nbt.getInt(Constants.Nbt.HOOKED_AGE);
	}
}
