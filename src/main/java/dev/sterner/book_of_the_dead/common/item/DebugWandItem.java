package dev.sterner.book_of_the_dead.common.item;

import dev.sterner.book_of_the_dead.BotD;
import dev.sterner.book_of_the_dead.common.block.entity.ButcherTableBlockEntity;
import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.CorpseDataComponent;
import dev.sterner.book_of_the_dead.common.entity.PlayerCorpseEntity;
import dev.sterner.book_of_the_dead.common.registry.BotDEntityTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
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
		if(!world.isClient() && player.isSneaking() && world.getBlockState(pos).isOf(BotDObjects.BUTCHER_TABLE) && world.getBlockEntity(pos) instanceof ButcherTableBlockEntity be){
			be.makeFilth(world);
			be.markDirty();
		}
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
}
