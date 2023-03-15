package dev.sterner.book_of_the_dead.common.item;

import dev.sterner.book_of_the_dead.common.block.entity.RetortFlaskBlockEntity;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SyringeItem extends Item {
	private static final int MAX_USE_TIME = 32;

	public SyringeItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		ItemStack stack = context.getStack();
		BlockPos blockPos = context.getBlockPos();
		World world = context.getWorld();
		BlockState state = world.getBlockState(blockPos);
		if(state.isOf(BotDObjects.RETORT_FLASK_BLOCK) && world.getBlockEntity(blockPos) instanceof RetortFlaskBlockEntity blockEntity){
			if(blockEntity.getItems().get(0).getItem() instanceof StatusEffectItem statusEffectItem){
				writeStatusEffectNbt(stack, new StatusEffectInstance(statusEffectItem.getStatusEffect(), 20 * 60, 1));
				blockEntity.reset();
			}
		}

		return super.useOnBlock(context);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;
		if (playerEntity instanceof ServerPlayerEntity) {
			Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
		}

		if (!world.isClient) {
			StatusEffectInstance instance = readStatusEffectNbt(stack);
			if(instance != null){
				user.addStatusEffect(new StatusEffectInstance(instance));
			}
		}

		if (playerEntity != null) {
			playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
			if (!playerEntity.getAbilities().creativeMode) {
				stack.decrement(1);
			}
		}

		if (playerEntity == null || !playerEntity.getAbilities().creativeMode) {
			if (stack.isEmpty()) {
				return new ItemStack(Items.GLASS_BOTTLE);
			}

			if (playerEntity != null) {
				playerEntity.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
			}
		}

		return stack;
	}

	@Nullable
	public StatusEffectInstance readStatusEffectNbt(ItemStack stack){
		if(stack.hasNbt() && stack.getOrCreateNbt().contains(Constants.Nbt.STATUS_EFFECT_INSTANCE)){
			NbtCompound nbt = stack.getSubNbt(Constants.Nbt.STATUS_EFFECT_INSTANCE);
			if(nbt != null){
				StatusEffect effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(nbt.getString(Constants.Nbt.STATUS_EFFECT)));
				if(effect != null){
					return new StatusEffectInstance(effect, nbt.getInt(Constants.Nbt.DURATION), nbt.getInt(Constants.Nbt.AMPLIFIER));
				}
			}
		}
		return null;
	}

	public void writeStatusEffectNbt(ItemStack stack, StatusEffectInstance instance){
		NbtCompound nbt = new NbtCompound();
		Identifier identifier = Registries.STATUS_EFFECT.getId(instance.getEffectType());
		if(identifier != null){
			nbt.putString(Constants.Nbt.STATUS_EFFECT, identifier.toString());
			nbt.putInt(Constants.Nbt.DURATION, instance.getDuration());
			nbt.putInt(Constants.Nbt.AMPLIFIER, instance.getAmplifier());
			stack.getOrCreateNbt().put(Constants.Nbt.STATUS_EFFECT_INSTANCE, nbt);
		}
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return MAX_USE_TIME;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ItemUsage.consumeHeldItem(world, user, hand);
	}

}
