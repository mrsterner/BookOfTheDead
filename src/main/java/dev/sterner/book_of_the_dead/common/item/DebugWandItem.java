package dev.sterner.book_of_the_dead.common.item;

import dev.sterner.book_of_the_dead.client.network.BloodSplashParticlePacket;
import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.LivingEntityDataComponent;
import dev.sterner.book_of_the_dead.common.component.PlayerKnowledgeComponent;
import dev.sterner.book_of_the_dead.common.component.PlayerSanityComponent;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.quiltmc.qsl.networking.api.PlayerLookup;

import static dev.sterner.book_of_the_dead.BotD.isDebugMode;

public class DebugWandItem extends Item {
	public DebugWandItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (!isDebugMode()) return super.useOnBlock(context);
		World world = context.getWorld();
		PlayerEntity player = context.getPlayer();
		ItemStack syringe = new ItemStack(BotDObjects.SYRINGE);
		StatusEffectInstance instance = new StatusEffectInstance(StatusEffects.WITHER, 20 * 2, 2);
		SyringeItem.writeStatusEffectNbt(syringe, instance);
		if (player != null) {
			ItemScatterer.spawn(world, player.getX(), player.getY(), player.getZ(), syringe);
		}
		return super.useOnBlock(context);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		/*
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

		 */

		if (world instanceof ServerWorld serverWorld) {

			PlayerLookup.tracking(user).forEach(track -> BloodSplashParticlePacket.send(track, user.getX() + 0.5, user.getY(), user.getZ() + 0.5));
			BloodSplashParticlePacket.send(user, user.getX() + 0.5, user.getY(), user.getZ() + 0.5);
		}
		PlayerSanityComponent component = BotDComponents.SANITY_COMPONENT.get(user);
		if(user.isSneaking()){
			component.increaseSanity(10);
		}else{
			component.decreaseSanity(10);
		}




		PlayerKnowledgeComponent c = BotDComponents.KNOWLEDGE_COMPONENT.get(user);
		if (c.clearData()) {
			System.out.println("KnowledgeData Cleared!");
		}

		/*
		if (world instanceof ServerWorld serverWorld) {
			serverWorld.spawnParticles(new OrbitParticleEffect(1, 0, 0.25f, (float) user.getX(), (float) user.getY() + 1, (float) user.getZ(), 3), user.getX(), user.getY() + 1, user.getZ(), 0, 0, 0, 0, 0.05);
		}

		 */

		/*
		LivingEntityDataComponent component = BotDComponents.LIVING_COMPONENT.get(user);
		component.setRitualPos(user.getPos().add(5, 0, 5));
		user.addStatusEffect(new StatusEffectInstance(BotDStatusEffects.SOUL_SIPHON, 20 * 10));

		 */
		return super.use(world, user, hand);
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		if (!isDebugMode()) return super.useOnEntity(stack, user, entity, hand);
		LivingEntityDataComponent source = BotDComponents.LIVING_COMPONENT.get(user);
		LivingEntityDataComponent target = BotDComponents.LIVING_COMPONENT.get(entity);
		source.setEntangledEntityId(entity.getId());
		target.setEntangledEntityId(user.getId());

		return super.useOnEntity(stack, user, entity, hand);
	}
}
