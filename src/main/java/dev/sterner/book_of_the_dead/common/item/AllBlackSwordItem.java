package dev.sterner.book_of_the_dead.common.item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import dev.sterner.book_of_the_dead.common.registry.BotDEntityAttributeRegistry;
import dev.sterner.book_of_the_dead.common.util.BotDUtils;
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

import java.util.UUID;

public class AllBlackSwordItem extends SwordItem {
	private static final EntityAttributeModifier REACH_MODIFIER = new EntityAttributeModifier(UUID.fromString("5fc9d1ae-8e00-449b-be79-b8d31fa10eff"), "Weapon modifier", 1.5, EntityAttributeModifier.Operation.ADDITION);


	public AllBlackSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
		super(toolMaterial, attackDamage, attackSpeed, settings);
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		Multimap<EntityAttribute, EntityAttributeModifier> map = LinkedHashMultimap.create(super.getAttributeModifiers(slot));
		if (slot == EquipmentSlot.MAINHAND) {
			map.put(BotDEntityAttributeRegistry.ATTACK_RANGE, REACH_MODIFIER);
		}
		return map;
	}


	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);
		if (!stack.hasNbt()) {
			NbtCompound tag = stack.getOrCreateNbt();
			tag.putInt(Constants.Nbt.ALL_BLACK, 0);
			stack.setNbt(tag);
		}
	}
}
