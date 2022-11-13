package dev.sterner.legemeton;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import dev.sterner.legemeton.api.event.OnEntityDeathEvent;
import dev.sterner.legemeton.api.interfaces.Hauler;
import dev.sterner.legemeton.common.entity.CorpseEntity;
import dev.sterner.legemeton.common.registry.LegemetonBlockEntityTypes;
import dev.sterner.legemeton.common.registry.LegemetonEnchantments;
import dev.sterner.legemeton.common.registry.LegemetonEntityTypes;
import dev.sterner.legemeton.common.registry.LegemetonObjects;
import dev.sterner.legemeton.common.util.Constants;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EulerAngle;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Legemeton implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("The Legemeton");

	@Override
	public void onInitialize(ModContainer mod) {
		LegemetonObjects.init();
		LegemetonEntityTypes.init();
		LegemetonBlockEntityTypes.init();
		LegemetonEnchantments.init();

		OnEntityDeathEvent.ON_ENTITY_DEATH.register(this::onButcheredEntity);
		UseEntityCallback.EVENT.register(this::onPickupCorpse);
		UseBlockCallback.EVENT.register(this::placeCorpse);
	}

	private ActionResult placeCorpse(PlayerEntity player, World world, Hand hand, BlockHitResult blockHitResult) {
		if(world instanceof ServerWorld serverWorld){
			Hauler.of(player).ifPresent(hauler -> {
				if(hauler.getCorpseEntity() != null){
					NbtCompound nbtCompound = hauler.getCorpseEntity();
					EntityType.get(nbtCompound.getString("id")).ifPresent(type -> {
						Entity entity = type.create(serverWorld);
						if (entity != null) {
							BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());

							CorpseEntity corpseEntity = LegemetonEntityTypes.CORPSE_ENTITY.create(serverWorld);
							if (corpseEntity != null) {

								corpseEntity.setCorpseEntity(hauler.getCorpseEntity());
								corpseEntity.setIsBaby(hauler.getIsBaby());
								corpseEntity.setIsDying(false);
								corpseEntity.setVillagerData(hauler.getVillagerData());
								corpseEntity.setBodyRotation(new EulerAngle(0, -player.bodyYaw, 0));
								corpseEntity.refreshPositionAndAngles(player.getBlockPos(), 0, 0);
								corpseEntity.teleport(pos.getX(), pos.getY(), pos.getZ());
								serverWorld.spawnEntity(corpseEntity);
							}

							serverWorld.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1, 1);

							//Reset Data
							hauler.setIsBaby(false);
							hauler.setCorpseEntity(new NbtCompound());
							hauler.setVillagerData(new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 1));
						}
					});
				}
			});
		}

		return ActionResult.PASS;
	}

	private ActionResult onPickupCorpse(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult entityHitResult) {
		if(!world.isClient() && entity instanceof CorpseEntity corpse && player.isSneaking()){

			Hauler.of(player).ifPresent(hauler -> {
				NbtCompound entityCompound = new NbtCompound();
				corpse.saveSelfNbt(entityCompound);
				NbtCompound nbt = corpse.writeNbt(entityCompound);

				hauler.setCorpseEntity(nbt.getCompound(Constants.Nbt.CORPSE_ENTITY));
				hauler.setIsBaby(nbt.getBoolean(Constants.Nbt.IS_BABY));

				if (nbt.contains(Constants.Nbt.VILLAGER_DATA, NbtElement.COMPOUND_TYPE)) {
					DataResult<VillagerData> dataResult = VillagerData.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, nbt.get(Constants.Nbt.VILLAGER_DATA)));
					dataResult.resultOrPartial(Legemeton.LOGGER::error).ifPresent(hauler::setVillagerData);
				}
				corpse.remove(Entity.RemovalReason.DISCARDED);
			});
			return ActionResult.CONSUME;
		}
		return ActionResult.PASS;
	}

	private void onButcheredEntity(LivingEntity livingEntity, BlockPos blockPos, DamageSource source) {
		if(source.getAttacker() instanceof PlayerEntity player && (player.getMainHandStack().isOf(LegemetonObjects.BUTCHER_KNIFE) || EnchantmentHelper.getLevel(LegemetonEnchantments.BUTCHERING, player.getMainHandStack()) != 0)){
			if(livingEntity.getType().isIn(Constants.Tags.BUTCHERABLE)){
				World world = player.world;
				CorpseEntity corpse = LegemetonEntityTypes.CORPSE_ENTITY.create(world);
				if (corpse != null) {
					corpse.setBodyRotation(new EulerAngle(livingEntity.getPitch(), livingEntity.bodyYaw, livingEntity.getRoll()));
					if(livingEntity instanceof VillagerEntity villagerEntity){
						corpse.setVillagerData(villagerEntity.getVillagerData());
					}
					if(livingEntity.isBaby()){
						corpse.setIsBaby(true);
					}

					NbtCompound nbtCompound = new NbtCompound();
					nbtCompound.putString("id", livingEntity.getSavedEntityId());
					corpse.writeNbt(nbtCompound);
					corpse.setCorpseEntity(nbtCompound);
					corpse.copyPositionAndRotation(livingEntity);
					corpse.refreshPositionAndAngles(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), livingEntity.getYaw(), livingEntity.getPitch());
					corpse.setPersistent();
					world.spawnEntity(corpse);
					livingEntity.discard();
				}
			}
		}
	}
}
