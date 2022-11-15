package dev.sterner.legemeton;

import dev.sterner.legemeton.api.event.OnEntityDeathEvent;
import dev.sterner.legemeton.api.interfaces.Hauler;
import dev.sterner.legemeton.common.block.RopeBlock;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.sterner.legemeton.common.block.HookBlock.FACING;

public class Legemeton implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("The Legemeton");

	@Override
	public void onInitialize(ModContainer mod) {
		LegemetonObjects.init();
		LegemetonEntityTypes.init();
		LegemetonBlockEntityTypes.init();
		LegemetonEnchantments.init();

		OnEntityDeathEvent.START.register(this::onButcheredEntity);
		UseEntityCallback.EVENT.register(this::onPickupCorpse);
		UseBlockCallback.EVENT.register(this::placeCorpse);
		UseBlockCallback.EVENT.register(this::extendRope);
		UseBlockCallback.EVENT.register(this::placeHook);
	}

	private ActionResult placeHook(PlayerEntity player, World world, Hand hand, BlockHitResult blockHitResult) {
		if(!world.isClient() && player.getMainHandStack().isOf(LegemetonObjects.HOOK) && hand == Hand.MAIN_HAND && world.getBlockState(blockHitResult.getBlockPos()).isOf(LegemetonObjects.ROPE)) {
			if(world.getBlockState(blockHitResult.getBlockPos()).get(RopeBlock.ROPE) == RopeBlock.Rope.BOTTOM){
				world.setBlockState(blockHitResult.getBlockPos(), LegemetonObjects.HOOK_BLOCK.getDefaultState().with(FACING, player.getHorizontalFacing()));
				if(!player.isCreative()){
					player.getMainHandStack().decrement(1);
				}
				return ActionResult.CONSUME;
			}

		}
		return ActionResult.PASS;
	}

	private ActionResult extendRope(PlayerEntity player, World world, Hand hand, BlockHitResult blockHitResult) {
		if(!world.isClient() && player.getMainHandStack().isOf(LegemetonObjects.ROPE.asItem()) && hand == Hand.MAIN_HAND && world.getBlockState(blockHitResult.getBlockPos()).isOf(LegemetonObjects.ROPE)){
			int y = blockHitResult.getBlockPos().getY() - 1;
			while (world.getBlockState(new BlockPos(blockHitResult.getBlockPos().getX(), y, blockHitResult.getBlockPos().getZ())).isOf(LegemetonObjects.ROPE)){
				y--;
			}
			BlockPos targetPos = new BlockPos(blockHitResult.getBlockPos().getX(), y, blockHitResult.getBlockPos().getZ());
			if(world.getBlockState(targetPos).isAir()){
				world.setBlockState(targetPos, LegemetonObjects.ROPE.getDefaultState().with(RopeBlock.ROPE, RopeBlock.Rope.BOTTOM));
				if(!player.isCreative()){
					player.getMainHandStack().decrement(1);
				}
				return ActionResult.CONSUME;
			}
		}
		return ActionResult.PASS;
	}

	private ActionResult placeCorpse(PlayerEntity player, World world, Hand hand, BlockHitResult blockHitResult) {
		if(world instanceof ServerWorld serverWorld && hand == Hand.MAIN_HAND && player.getMainHandStack().isEmpty() && player.isSneaking()){
			Hauler.of(player).ifPresent(hauler -> {
				if(hauler.getCorpseEntity().isEmpty()){
					NbtCompound nbtCompound = hauler.getCorpseEntity();
					if(nbtCompound.contains(Constants.Nbt.CORPSE_ENTITY)){
						EntityType.getEntityFromNbt(nbtCompound.getCompound(Constants.Nbt.CORPSE_ENTITY), world).ifPresent(storedEntity -> {
							BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());


							CorpseEntity corpseEntity = LegemetonEntityTypes.CORPSE_ENTITY.create(serverWorld);
							if (corpseEntity != null && storedEntity instanceof LivingEntity storedLivingEntity) {
								corpseEntity.setCorpseEntity(storedLivingEntity);
								corpseEntity.refreshPositionAndAngles(player.getBlockPos(), 0, 0);
								corpseEntity.teleport(pos.getX(), pos.getY(), pos.getZ());
								serverWorld.spawnEntity(corpseEntity);
							}

							serverWorld.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1, 1);

							//Reset Data
							hauler.clearCorpseData();
						});
					}

				}
			});
		}

		return ActionResult.PASS;
	}

	private ActionResult onPickupCorpse(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult entityHitResult) {
		if(!world.isClient() && entity instanceof CorpseEntity corpse && player.isSneaking() && player.getMainHandStack().isEmpty()){
			Hauler.of(player).ifPresent(hauler -> {
				if(hauler.getCorpseEntity().isEmpty()){
					hauler.setCorpseEntity(corpse);
					corpse.remove(Entity.RemovalReason.DISCARDED);
				}
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
}
