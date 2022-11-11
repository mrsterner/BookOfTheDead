package dev.sterner.legemeton;

import dev.sterner.legemeton.api.event.OnEntityDeathEvent;
import dev.sterner.legemeton.common.entity.CorpseEntity;
import dev.sterner.legemeton.common.registry.LegemetonBlockEntityTypes;
import dev.sterner.legemeton.common.registry.LegemetonEntityTypes;
import dev.sterner.legemeton.common.registry.LegemetonObjects;
import dev.sterner.legemeton.common.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
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

		OnEntityDeathEvent.ON_ENTITY_DEATH.register(this::onButcheredEntity);
	}

	private void onButcheredEntity(LivingEntity livingEntity, BlockPos blockPos, DamageSource source) {
		if(source.getAttacker() instanceof PlayerEntity player && player.getStackInHand(Hand.MAIN_HAND).isOf(LegemetonObjects.BUTCHER_KNIFE)){
			if(livingEntity.getType().isIn(Constants.Tags.BUTCHERABLE)){
				World world = player.world;
				CorpseEntity corpse = LegemetonEntityTypes.CORPSE_ENTITY.create(world);
				if (corpse != null) {
					corpse.setLivingEntity(livingEntity);
					corpse.copyPositionAndRotation(livingEntity);
					corpse.refreshPositionAndAngles(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), livingEntity.getYaw(), livingEntity.getPitch());
					corpse.setPersistent();
					world.spawnEntity(corpse);
				}
			}
		}
	}
}
