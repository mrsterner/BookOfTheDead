package dev.sterner.book_of_the_dead.common.rituals;

import dev.sterner.book_of_the_dead.api.interfaces.IRitual;
import dev.sterner.book_of_the_dead.api.ritual.RitualManager;
import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class BasicNecrotableRitual implements IRitual {
	private final Identifier id;
	public final RitualManager ritualManager = new RitualManager();

	public BasicNecrotableRitual(Identifier id) {
		this.id = id;
	}

	public Identifier getId() {
		return id;
	}

	//Called methods from RitualBlockEntity

	@Override
	public void tick(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
		ritualManager.ticker++;
		boolean canEndSacrifices = ritualManager.consumeSacrifices(world, blockPos, blockEntity);
		boolean canEndRitual = ritualManager.consumeItems(world, blockPos, blockEntity);
		if ((canEndRitual && canEndSacrifices) || ritualManager.lockTick) {
			if (!ritualManager.lockTick) {
				ritualManager.runCommand(world, blockEntity, blockPos, "start");
				ritualManager.generateStatusEffects(world, blockPos, blockEntity);
			}
			ritualManager.lockTick = true;
			ritualManager.runCommand(world, blockEntity, blockPos, "tick");
		}
	}

	@Override
	public void onStopped(World world, BlockPos blockPos, NecroTableBlockEntity blockEntity) {
		ritualManager.ticker = 0;
		if (ritualManager.lockTick) {
			if (ritualManager.userUuid == null) {
				PlayerEntity player = world.getClosestPlayer(blockPos.getZ(), blockPos.getY(), blockPos.getZ(), 16D, true);
				if (player != null) {
					ritualManager.userUuid = player.getUuid();
				}
			}

			ritualManager.runCommand(world, blockEntity, blockPos, "end");
			ritualManager.summonSummons(world, blockPos, blockEntity);
			ritualManager.summonItems(world, blockPos, blockEntity);
			this.reset();
		}
	}

	@Override
	public void reset() {
		ritualManager.lockTick = false;
		ritualManager.canCollectPedestals = true;
		ritualManager.canCollectSacrifices = true;
	}
}
