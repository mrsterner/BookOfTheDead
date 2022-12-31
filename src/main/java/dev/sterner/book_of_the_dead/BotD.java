package dev.sterner.book_of_the_dead;

import dev.sterner.book_of_the_dead.api.enums.HorizontalDoubleBlockHalf;
import dev.sterner.book_of_the_dead.api.event.OnEntityDeathEvent;
import dev.sterner.book_of_the_dead.api.interfaces.IHauler;
import dev.sterner.book_of_the_dead.api.block.HorizontalDoubleBlock;
import dev.sterner.book_of_the_dead.common.block.RopeBlock;
import dev.sterner.book_of_the_dead.common.entity.CorpseEntity;
import dev.sterner.book_of_the_dead.common.event.UseEvents;
import dev.sterner.book_of_the_dead.common.registry.*;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

import static dev.sterner.book_of_the_dead.common.block.HookBlock.FACING;

public class BotD implements ModInitializer {

	static final boolean DEBUG_MODE = true;
	public static boolean isDebugMode() {
		return DEBUG_MODE && QuiltLoader.isDevelopmentEnvironment();
	}

	@Override
	public void onInitialize(ModContainer mod) {
		BotDObjects.init();
		BotDEntityTypes.init();
		BotDBlockEntityTypes.init();
		BotDEnchantments.init();
		BotDRecipeTypes.init();
		BotDWorldGenerators.init();
		BotDTrades.init();
		BotDRituals.init();
		BotDSoundEvents.init();

		UseEvents.init();

		ServerLivingEntityEvents.AFTER_DEATH.register(this::onButcheredEntity);
		UseEntityCallback.EVENT.register(this::onPickupCorpse);

		LootTableEvents.MODIFY.register(this::injectCinnabar);
	}

	private void onButcheredEntity(LivingEntity livingEntity, DamageSource damageSource) {
		if(damageSource.getAttacker() instanceof PlayerEntity player && (player.getMainHandStack().isOf(BotDObjects.BUTCHER_KNIFE) || player.getMainHandStack().isOf(BotDObjects.BLOODY_BUTCHER_KNIFE) || EnchantmentHelper.getLevel(BotDEnchantments.BUTCHERING, player.getMainHandStack()) != 0)){
			if(livingEntity.getType().isIn(Constants.Tags.BUTCHERABLE)){
				World world = player.world;
				CorpseEntity corpse = BotDEntityTypes.CORPSE_ENTITY.create(world);
				if (corpse != null) {
					corpse.setCorpseEntity(livingEntity);
					corpse.copyPositionAndRotation(livingEntity);
					corpse.refreshPositionAndAngles(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), livingEntity.getYaw(), livingEntity.getPitch());
					corpse.setPersistent();
					world.spawnEntity(corpse);
					livingEntity.discard();
				}
			}
		}
	}

	private void injectCinnabar(ResourceManager resourceManager, LootManager lootManager, Identifier identifier, LootTable.Builder builder, LootTableSource lootTableSource) {
		if(Blocks.DEEPSLATE_REDSTONE_ORE.getLootTableId().equals(identifier) && lootTableSource.isBuiltin()){
			LootPool.Builder poolBuilder = LootPool.builder()
					.conditionally(SurvivesExplosionLootCondition.builder())
					.with(ItemEntry.builder(BotDObjects.CINNABAR)
							.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(-2.0F, 1.0F))));
			builder.pool(poolBuilder);
		}
	}

	private ActionResult onPickupCorpse(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult entityHitResult) {
		if(!world.isClient() && entity instanceof CorpseEntity corpse && player.isSneaking() && player.getMainHandStack().isEmpty()){
			IHauler.of(player).ifPresent(hauler -> {
				if(hauler.getCorpseEntity().isEmpty()){
					hauler.setCorpseEntity(corpse);
					corpse.remove(Entity.RemovalReason.DISCARDED);
				}
			});
			return ActionResult.CONSUME;
		}
		return ActionResult.PASS;
	}
}
