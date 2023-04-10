package dev.sterner.book_of_the_dead.common.item;

import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class CageItem extends Item {
	public CageItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity target, Hand hand) {


		return super.useOnEntity(stack, user, target, hand);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		if (!world.isClient()) {
			ItemStack stack = context.getStack();
			if (stack.hasNbt() && stack.getOrCreateNbt().contains(Constants.Nbt.STORED_ENTITY)) {
				NbtCompound entityCompound = stack.getOrCreateNbt().getCompound(Constants.Nbt.STORED_ENTITY);
				BlockPos pos = context.getBlockPos().offset(context.getSide());
				if (Registries.ENTITY_TYPE.get(new Identifier(entityCompound.getString("id"))).create((ServerWorld) world, null, null, pos, SpawnReason.SPAWN_EGG, true, false) instanceof MobEntity mob) {
					mob.readNbt(entityCompound);
					mob.setUuid(UUID.randomUUID());
					mob.refreshPositionAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
					world.spawnEntity(mob);
					world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.5f, 1);
					if (context.getPlayer() == null || !context.getPlayer().isCreative()) {
						stack.removeSubNbt(Constants.Nbt.STORED_ENTITY);
					}
				}
			}
			return ActionResult.success(world.isClient);
		}
		return super.useOnBlock(context);
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return stack.hasNbt() && stack.getOrCreateNbt().contains(Constants.Nbt.STORED_ENTITY);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (stack.hasNbt() && stack.getOrCreateNbt().contains(Constants.Nbt.STORED_ENTITY)) {
			Text name;
			NbtCompound entityCompound = stack.getNbt().getCompound(Constants.Nbt.STORED_ENTITY);
			if (entityCompound.contains("CustomName")) {
				name = Text.Serializer.fromJson(entityCompound.getString("CustomName"));
			} else {
				name = Registries.ENTITY_TYPE.get(new Identifier(entityCompound.getString("id"))).getName();
			}
			if (name != null) {
				tooltip.add(((MutableText) name).formatted(Formatting.GRAY));
			}
		}
		super.appendTooltip(stack, world, tooltip, context);
	}
}
