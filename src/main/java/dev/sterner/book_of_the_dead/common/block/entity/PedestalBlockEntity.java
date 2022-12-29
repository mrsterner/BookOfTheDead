package dev.sterner.book_of_the_dead.common.block.entity;

import dev.sterner.book_of_the_dead.api.block.entity.BaseBlockEntity;
import dev.sterner.book_of_the_dead.client.particle.ItemStackBeamParticleEffect;
import dev.sterner.book_of_the_dead.common.registry.BotDBlockEntityTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDParticleTypes;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PedestalBlockEntity extends BaseBlockEntity {
	private ItemStack stack;
	private boolean loaded = false;
	private boolean crafting;
	private boolean craftingFinished;
	public BlockPos ritualCenter;
	public int duration = 0;

	public PedestalBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.PEDESTAL, pos, state);
		stack = ItemStack.EMPTY;
		craftingFinished = false;
	}

	public static void tick(World world, BlockPos blockPos, BlockState blockState, PedestalBlockEntity blockEntity) {
		if (world != null && blockEntity.isCrafting()) {
			if(!blockEntity.getStack().isEmpty() && blockEntity.hasRitualPos() && blockEntity.duration > 0){
				blockEntity.duration--;
				if(blockEntity.duration == 0){
					blockEntity.craftingFinished = true;
				}
				BlockPos b = blockEntity.ritualCenter.subtract(blockPos.add(0.5,1.5,0.5));
				Vec3d directionVector = new Vec3d(b.getX(), b.getY(), b.getZ());

				double x = blockPos.getX() + (world.random.nextDouble() * 0.2D) + 0.4D;
				double y = blockPos.getY() + (world.random.nextDouble() * 0.2D) + 1.2D;
				double z = blockPos.getZ() + (world.random.nextDouble() * 0.2D) + 0.4D;
				if(world instanceof ServerWorld serverWorld && !blockEntity.getStack().isEmpty()){
					serverWorld.spawnParticles(
							new ItemStackBeamParticleEffect(
									BotDParticleTypes.ITEM_BEAM_PARTICLE,
									blockEntity.getStack(),
									10),
							x, y, z, 0, directionVector.x, directionVector.y, directionVector.z, 0.10D);
				}
				if(blockEntity.craftingFinished){
					blockEntity.setStack(Items.AIR.getDefaultStack());
					blockEntity.setCrafting(false);
					blockEntity.setCraftingFinished(false);
				}
			}
		}
	}


	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.setStack(ItemStack.fromNbt(nbt.getCompound(Constants.Nbt.PEDESTAL_ITEM)));
		this.setCrafting(nbt.getBoolean(Constants.Nbt.CRAFTING));
		this.setCraftingFinished(nbt.getBoolean(Constants.Nbt.CRAFTING_FINISHED));
		if (nbt.contains(Constants.Nbt.RITUAL_POS)) {
			this.ritualCenter = NbtHelper.toBlockPos(nbt.getCompound(Constants.Nbt.RITUAL_POS));
		}
		this.duration = nbt.getInt("duration");
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.put(Constants.Nbt.PEDESTAL_ITEM, this.stack.writeNbt(new NbtCompound()));
		nbt.putBoolean(Constants.Nbt.CRAFTING, this.isCrafting());
		nbt.putBoolean(Constants.Nbt.CRAFTING_FINISHED, craftingFinished);
		if(hasRitualPos()){
			nbt.put(Constants.Nbt.RITUAL_POS, NbtHelper.fromBlockPos(this.ritualCenter));
		}
		nbt.putInt("curation", this.duration);
	}

	private boolean hasRitualPos() {
		return this.ritualCenter != null;
	}

	public ItemStack getStack() {
		return stack;
	}

	public void setStack(ItemStack stack) {
		this.stack = stack;
		if(this.world != null && !this.world.isClient) {
			this.sync(world, pos);
		}
	}

	public boolean isCrafting() {
		return crafting;
	}

	public void setCrafting(boolean crafting) {
		this.crafting = crafting;
	}

	public void setCraftingFinished(boolean finished) {
		this.craftingFinished = finished;
	}

}
