package dev.sterner.book_of_the_dead.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class AllBlackSwordItem extends SwordItem {
	public final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
	public AllBlackSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings, boolean allBlack) {
		super(toolMaterial, attackDamage, attackSpeed, settings);
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		/*TODO
		if(allBlack){
			builder.put(ReachEntityAttributes.REACH, new EntityAttributeModifier("Attack range", 1.1D, EntityAttributeModifier.Operation.ADDITION));
			builder.put(ReachEntityAttributes.ATTACK_RANGE, new EntityAttributeModifier("Attack range", 1.1D, EntityAttributeModifier.Operation.ADDITION));
		}else{
			builder.put(ReachEntityAttributes.REACH, new EntityAttributeModifier("Attack range", 0.5D, EntityAttributeModifier.Operation.ADDITION));
			builder.put(ReachEntityAttributes.ATTACK_RANGE, new EntityAttributeModifier("Attack range", 0.5D, EntityAttributeModifier.Operation.ADDITION));
		}

		 */

		this.attributeModifiers = builder.build();
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot) {
		return equipmentSlot == EquipmentSlot.MAINHAND ? attributeModifiers : super.getAttributeModifiers(equipmentSlot);
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
}
