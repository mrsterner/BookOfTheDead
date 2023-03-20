package dev.sterner.book_of_the_dead.common.item;

import dev.sterner.book_of_the_dead.common.block.entity.RetortFlaskBlockEntity;
import dev.sterner.book_of_the_dead.common.registry.BotDDamageTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.registry.BotDStatusEffects;
import dev.sterner.book_of_the_dead.common.util.Constants;
import dev.sterner.book_of_the_dead.common.util.TextUtils;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
		boolean emptySyringe = false;

		if (!world.isClient && playerEntity != null) {
			StatusEffectInstance instance = readStatusEffectNbt(stack);
			if(instance != null){
				user.addStatusEffect(new StatusEffectInstance(instance));
				playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
				if (!playerEntity.getAbilities().creativeMode) {
					stack.decrement(1);
				}
				emptySyringe = true;
			}else if(stack.getOrCreateNbt().contains(Constants.Nbt.BLOOD)){
				//TODO Inject Blood for some reason
				playerEntity.addStatusEffect(new StatusEffectInstance(BotDStatusEffects.SANGUINE, 20 * 20));
				emptySyringe = true;
			}else{
				stack.getOrCreateNbt().putString(Constants.Nbt.NAME, playerEntity.getEntityName());
				stack.getOrCreateNbt().putUuid(Constants.Nbt.BLOOD, playerEntity.getUuid());
				playerEntity.damage(BotDDamageTypes.getDamageSource(world, BotDDamageTypes.SANGUINE), 4f);
			}
			if(emptySyringe){
				if (!playerEntity.getAbilities().creativeMode) {
					if (stack.isEmpty()) {
						return new ItemStack(BotDObjects.SYRINGE);
					}

					playerEntity.getInventory().insertStack(new ItemStack(BotDObjects.SYRINGE));
				}
			}
		}
		return stack;
	}

	public boolean isFull(ItemStack stack){
		return stack.getOrCreateNbt().contains(Constants.Nbt.STATUS_EFFECT_INSTANCE) || stack.getOrCreateNbt().contains(Constants.Nbt.BLOOD);
	}

	@Nullable
	public static StatusEffectInstance readStatusEffectNbt(ItemStack stack){
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

	public static void writeStatusEffectNbt(ItemStack stack, StatusEffectInstance instance){
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

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if(stack.getOrCreateNbt().contains(Constants.Nbt.BLOOD) && stack.getOrCreateNbt().contains(Constants.Nbt.NAME)){
			String name = stack.getOrCreateNbt().getString(Constants.Nbt.NAME);
			String formattedName = TextUtils.capitalizeString(name);
			tooltip.add(Text.literal(formattedName).setStyle(Style.EMPTY.withColor(0xAC0014)));
		}

		if(stack.getOrCreateNbt().contains(Constants.Nbt.STATUS_EFFECT_INSTANCE)){
			StatusEffectInstance instance = readStatusEffectNbt(stack);
			if(instance != null){
				StatusEffect statusEffect = instance.getEffectType();

				MutableText mutableText = Text.translatable(instance.getTranslationKey());
				if (instance.getAmplifier() > 0) {
					mutableText = Text.translatable("potion.withAmplifier", mutableText, Text.translatable("potion.potency." + instance.getAmplifier()));
				}

				if (!instance.endsWithin(20)) {
					mutableText = Text.translatable("potion.withDuration", mutableText, StatusEffectUtil.durationToString(instance, 1));
				}

				tooltip.add(mutableText.formatted(statusEffect.getType().getFormatting()));
			}

		}
		super.appendTooltip(stack, world, tooltip, context);
	}
}
