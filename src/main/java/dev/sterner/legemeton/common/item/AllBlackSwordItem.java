package dev.sterner.legemeton.common.item;

import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class AllBlackSwordItem extends SwordItem {
	public AllBlackSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
		super(toolMaterial, attackDamage, attackSpeed, settings);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);
		if(!stack.hasNbt()){
			NbtCompound tag = stack.getOrCreateNbt();
			tag.putInt(Constants.Nbt.ALL_BLACK, 0);
			stack.setNbt(tag);
		}
	}

	public void onCreation(ItemStack stack, World world, PlayerEntity player) {
		NbtCompound tag = stack.getOrCreateNbt();
		tag.putInt(Constants.Nbt.ALL_BLACK, 0);
		stack.setNbt(tag);
	}
}
