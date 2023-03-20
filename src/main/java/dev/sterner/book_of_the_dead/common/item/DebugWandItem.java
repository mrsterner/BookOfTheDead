package dev.sterner.book_of_the_dead.common.item;

import dev.sterner.book_of_the_dead.BotD;
import dev.sterner.book_of_the_dead.common.block.entity.ButcherTableBlockEntity;
import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.CorpseDataComponent;
import dev.sterner.book_of_the_dead.common.entity.PlayerCorpseEntity;
import dev.sterner.book_of_the_dead.common.registry.BotDEntityTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.registry.BotDStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugWandItem extends Item {
	public DebugWandItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		PlayerEntity player = context.getPlayer();
		ItemStack syringe = new ItemStack(BotDObjects.SYRINGE);
		StatusEffectInstance instance = new StatusEffectInstance(StatusEffects.WITHER, 20 * 2, 2);
		SyringeItem.writeStatusEffectNbt(syringe, instance);
		ItemScatterer.spawn(world, player.getX(), player.getY(), player.getZ(), syringe);
		return super.useOnBlock(context);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if(BotD.isDebugMode()){
			if(!user.isSneaking()){
				PlayerCorpseEntity entity = BotDEntityTypes.PLAYER_CORPSE_ENTITY.create(world);
				if (entity != null) {
					entity.setSkinProfile(user.getGameProfile());
					entity.refreshPositionAndAngles(user.getBlockPos(), 0, 0);
					world.spawnEntity(entity);
					CorpseDataComponent dataComponent = BotDComponents.CORPSE_COMPONENT.get(entity);
					dataComponent.isCorpse(true);
					entity.damage(user.getDamageSources().magic(), Integer.MAX_VALUE);
				}
			}

		}
		return super.use(world, user, hand);
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		entity.addStatusEffect(new StatusEffectInstance(BotDStatusEffects.SANGUINE, 20 * 5));
		return super.useOnEntity(stack, user, entity, hand);
	}
}
