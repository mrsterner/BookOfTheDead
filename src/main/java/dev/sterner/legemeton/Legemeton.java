package dev.sterner.legemeton;

import dev.sterner.legemeton.api.event.OnEntityDeathEvent;
import dev.sterner.legemeton.common.entity.CorpseEntity;
import dev.sterner.legemeton.common.registry.LegemetonBlockEntityTypes;
import dev.sterner.legemeton.common.registry.LegemetonEnchantments;
import dev.sterner.legemeton.common.registry.LegemetonEntityTypes;
import dev.sterner.legemeton.common.registry.LegemetonObjects;
import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EulerAngle;
import net.minecraft.world.World;
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
