package dev.sterner.book_of_the_dead.common.block.entity;

import dev.sterner.book_of_the_dead.api.block.entity.BaseBlockEntity;
import dev.sterner.book_of_the_dead.api.interfaces.IBlockEntityInventory;
import dev.sterner.book_of_the_dead.common.registry.*;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class NecroTableBlockEntity extends BaseBlockEntity {
	public boolean hasBotD = false;
	public boolean hasEmeraldTablet = false;
	public BlockPos ritualPos = null;

	public NecroTableBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.NECRO, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState blockState, NecroTableBlockEntity blockEntity) {

	}

	public ActionResult onUse(World world, BlockState state, BlockPos pos, PlayerEntity player, Hand hand) {
		if (world != null && world.getBlockEntity(pos) instanceof NecroTableBlockEntity necroTableBlockEntity) {
			if(player.getMainHandStack().isEmpty() && hand == Hand.MAIN_HAND){
				if(player.isSneaking()){
					if(necroTableBlockEntity.hasBotD){
						player.setStackInHand(hand, BotDObjects.BOOK_OF_THE_DEAD.getDefaultStack());
						necroTableBlockEntity.hasBotD = false;
					}else if(necroTableBlockEntity.hasEmeraldTablet){
						player.setStackInHand(hand, BotDObjects.EMERALD_TABLET.getDefaultStack());
						necroTableBlockEntity.hasEmeraldTablet = false;
					}
					necroTableBlockEntity.markDirty();
				}else{
					sendInfoToRitualBlock(world, player, pos, hasBotD, hasEmeraldTablet);
				}
			}else if(player.getMainHandStack().isOf(BotDObjects.BOOK_OF_THE_DEAD)){
				necroTableBlockEntity.hasBotD = true;
				player.getMainHandStack().decrement(1);
				necroTableBlockEntity.markDirty();
			}else if(player.getMainHandStack().isOf(BotDObjects.EMERALD_TABLET)){
				necroTableBlockEntity.hasEmeraldTablet = true;
				player.getMainHandStack().decrement(1);
				necroTableBlockEntity.markDirty();
			}
		}
		return ActionResult.PASS;
	}

	private void sendInfoToRitualBlock(World world, PlayerEntity player, BlockPos pos, boolean hasBotD, boolean hasEmeraldTablet) {
		label1:
		if(ritualPos == null){
			for(Direction dir : HorizontalFacingBlock.FACING.getValues()){
				for(int i = 0; i < 8; i++){
					BlockPos checkedPos = pos.offset(dir, i);
					BlockState block = world.getBlockState(checkedPos);
					if(block.isOf(BotDObjects.RITUAL)){
						ritualPos = checkedPos;
						break label1;
					}
				}
			}
		}

		if(ritualPos != null){
			if (world.getBlockEntity(ritualPos) instanceof RitualBlockEntity ritualBlockEntity) {
				System.out.println("FoundPos: " +ritualPos);
				ritualBlockEntity.shouldRun = true;
				ritualBlockEntity.hasBotD = hasBotD;
				ritualBlockEntity.hasEmeraldTablet = hasEmeraldTablet;
				ritualBlockEntity.user = player.getUuid();
				ritualBlockEntity.markDirty();
			}
		}
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.hasBotD = nbt.getBoolean(Constants.Nbt.HAS_LEGEMETON);
		this.hasEmeraldTablet = nbt.getBoolean(Constants.Nbt.HAS_EMERALD_TABLET);
		if (nbt.contains(Constants.Nbt.RITUAL_POS)) {
			this.ritualPos = NbtHelper.toBlockPos(nbt.getCompound(Constants.Nbt.RITUAL_POS));
		}
		markDirty();
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putBoolean(Constants.Nbt.HAS_LEGEMETON, this.hasBotD);
		nbt.putBoolean(Constants.Nbt.HAS_EMERALD_TABLET, this.hasEmeraldTablet);
		if(this.ritualPos != null){
			nbt.put(Constants.Nbt.RITUAL_POS, NbtHelper.fromBlockPos(this.ritualPos));
		}
	}
}
