package dev.sterner.book_of_the_dead.common.entity;

import dev.sterner.book_of_the_dead.common.util.BotDUtils;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FloatingItemEntity extends Entity {
	private static final TrackedData<ItemStack> DATA_ITEM_STACK = DataTracker.registerData(FloatingItemEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
	public final float hoverStart;
	public ItemStack stack = ItemStack.EMPTY;

	public FloatingItemEntity(EntityType<?> type, World world) {
		super(type, world);
		this.hoverStart = (float) (Math.random() * Math.PI * 2.0D);
		this.refreshPosition();
	}

	@Override
	public boolean collides() {
		return true;
	}

	@Override
	public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
		if(!world.isClient()){
			ItemStack handStack = player.getMainHandStack();
			if(handStack.isEmpty() && hand == Hand.MAIN_HAND){
				player.setStackInHand(hand, getItem());
				this.remove(RemovalReason.DISCARDED);
				return ActionResult.CONSUME;
			}

		}
		return super.interactAt(player, hitPos, hand);
	}

	protected Item getDefaultItem() {
		return Items.AIR;
	}

	protected ItemStack getItemRaw() {
		return this.getDataTracker().get(DATA_ITEM_STACK);
	}

	public ItemStack getItem() {
		ItemStack itemstack = this.getItemRaw();
		return itemstack.isEmpty() ? this.getDefaultItem().getDefaultStack() : itemstack;
	}

	public void setItem(ItemStack stack) {
		if (!stack.isOf(this.getDefaultItem()) || stack.hasNbt()) {
			this.getDataTracker().set(DATA_ITEM_STACK, stack);
		}
	}

	@Override
	public Packet<ClientPlayPacketListener> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
	}

	@Override
	protected void initDataTracker() {
		this.getDataTracker().startTracking(DATA_ITEM_STACK, ItemStack.EMPTY);
	}

	@Override
	public void onTrackedDataUpdate(TrackedData<?> data) {
		if (DATA_ITEM_STACK.equals(data)) {
			stack = getDataTracker().get(DATA_ITEM_STACK);
		}
		super.onTrackedDataUpdate(data);
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound tag) {
		ItemStack itemStack = this.getItemRaw();
		if (!itemStack.isEmpty()) {
			tag.put(Constants.Nbt.ITEM, itemStack.writeNbt(new NbtCompound()));
		}
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound tag) {
		ItemStack itemstack = ItemStack.fromNbt(tag.getCompound(Constants.Nbt.ITEM));
		this.setItem(itemstack);
	}

	public float getYOffset(float partialTicks) {
		return MathHelper.sin(((float) age + partialTicks) / 20.0F + hoverStart) * 0.1F + 0.1F;
	}

	public float getRotation(float partialTicks) {
		return ((float) age + partialTicks) / 20.0F + this.hoverStart;
	}
}
