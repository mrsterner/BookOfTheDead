package dev.sterner.book_of_the_dead;

import dev.sterner.book_of_the_dead.api.enums.HorizontalDoubleBlockHalf;
import dev.sterner.book_of_the_dead.api.event.OnEntityDeathEvent;
import dev.sterner.book_of_the_dead.api.interfaces.IHauler;
import dev.sterner.book_of_the_dead.api.block.HorizontalDoubleBlock;
import dev.sterner.book_of_the_dead.common.block.RopeBlock;
import dev.sterner.book_of_the_dead.common.entity.CorpseEntity;
import dev.sterner.book_of_the_dead.common.registry.*;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
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

		OnEntityDeathEvent.START.register(this::onButcheredEntity);
		UseEntityCallback.EVENT.register(this::onPickupCorpse);
		UseBlockCallback.EVENT.register(this::placeCorpse);
		UseBlockCallback.EVENT.register(this::extendRope);
		UseBlockCallback.EVENT.register(this::placeHook);
		UseBlockCallback.EVENT.register(this::createNecroTable);
		UseBlockCallback.EVENT.register(this::createButcherTable);

	}



	private ActionResult createDoubleBlock(Item useItem, Block targetBlock, BlockState resultState, PlayerEntity player, World world, Hand hand, BlockHitResult blockHitResult){
		if(!world.isClient() && player.getMainHandStack().isOf(useItem) && hand == Hand.MAIN_HAND){
			BlockPos blockPos = blockHitResult.getBlockPos();
			if(world.getBlockState(blockPos).isOf(targetBlock)){
				BlockPos left = blockPos.offset(player.getHorizontalFacing().rotateYCounterclockwise());
				BlockPos right = blockPos.offset(player.getHorizontalFacing().rotateYClockwise());

				if(world.getBlockState(left).isOf(targetBlock)){
					world.setBlockState(blockPos, resultState.with(HorizontalDoubleBlock.HHALF, HorizontalDoubleBlockHalf.RIGHT).with(FACING, player.getHorizontalFacing()));
					world.setBlockState(left, resultState.with(HorizontalDoubleBlock.HHALF, HorizontalDoubleBlockHalf.LEFT).with(FACING, player.getHorizontalFacing()));
				}else if(world.getBlockState(right).isOf(targetBlock)){
					world.setBlockState(blockPos, resultState.with(HorizontalDoubleBlock.HHALF, HorizontalDoubleBlockHalf.LEFT).with(FACING, player.getHorizontalFacing()));
					world.setBlockState(right, resultState.with(HorizontalDoubleBlock.HHALF, HorizontalDoubleBlockHalf.RIGHT).with(FACING, player.getHorizontalFacing()));
				}else {
					return ActionResult.PASS;
				}
				return ActionResult.CONSUME;
			}
		}
		return ActionResult.PASS;
	}

	private ActionResult createNecroTable(PlayerEntity player, World world, Hand hand, BlockHitResult blockHitResult) {
		if(!world.isClient() && player.getMainHandStack().isOf(BotDObjects.PAPER_AND_QUILL) && hand == Hand.MAIN_HAND){
			BlockPos blockPos = blockHitResult.getBlockPos();
			if(world.getBlockState(blockPos).isOf(Blocks.DEEPSLATE_TILES)){
				world.setBlockState(blockPos, BotDObjects.NECRO_TABLE.getDefaultState().with(FACING, player.getHorizontalFacing()));
				return ActionResult.CONSUME;
			}
		}
		return ActionResult.PASS;
	}

	private ActionResult createButcherTable(PlayerEntity player, World world, Hand hand, BlockHitResult blockHitResult) {
		return createDoubleBlock(BotDObjects.BUTCHER_KNIFE, Blocks.DARK_OAK_PLANKS, BotDObjects.BUTCHER_TABLE.getDefaultState(), player, world, hand, blockHitResult);
	}

	private ActionResult placeHook(PlayerEntity player, World world, Hand hand, BlockHitResult blockHitResult) {
		if(!world.isClient() && player.getMainHandStack().isOf(BotDObjects.HOOK) && hand == Hand.MAIN_HAND && world.getBlockState(blockHitResult.getBlockPos()).isOf(BotDObjects.ROPE)) {
			if(world.getBlockState(blockHitResult.getBlockPos()).get(RopeBlock.ROPE) == RopeBlock.Rope.BOTTOM){
				world.setBlockState(blockHitResult.getBlockPos(), BotDObjects.HOOK_BLOCK.getDefaultState().with(FACING, player.getHorizontalFacing()));
				if(!player.isCreative()){
					player.getMainHandStack().decrement(1);
				}
				return ActionResult.CONSUME;
			}

		}
		return ActionResult.PASS;
	}

	private ActionResult extendRope(PlayerEntity player, World world, Hand hand, BlockHitResult blockHitResult) {
		if(!world.isClient() && player.getMainHandStack().isOf(BotDObjects.ROPE.asItem()) && hand == Hand.MAIN_HAND && world.getBlockState(blockHitResult.getBlockPos()).isOf(BotDObjects.ROPE)){
			int y = blockHitResult.getBlockPos().getY() - 1;
			while (world.getBlockState(new BlockPos(blockHitResult.getBlockPos().getX(), y, blockHitResult.getBlockPos().getZ())).isOf(BotDObjects.ROPE)){
				y--;
			}
			BlockPos targetPos = new BlockPos(blockHitResult.getBlockPos().getX(), y, blockHitResult.getBlockPos().getZ());
			if(world.getBlockState(targetPos).isAir()){
				world.setBlockState(targetPos, BotDObjects.ROPE.getDefaultState().with(RopeBlock.ROPE, RopeBlock.Rope.BOTTOM));
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
			IHauler.of(player).ifPresent(hauler -> {
				if(!hauler.getCorpseEntity().isEmpty()){
					NbtCompound nbtCompound = hauler.getCorpseEntity();
					if(nbtCompound.contains(Constants.Nbt.CORPSE_ENTITY)){
						EntityType.getEntityFromNbt(nbtCompound.getCompound(Constants.Nbt.CORPSE_ENTITY), world).ifPresent(storedEntity -> {
							BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());


							CorpseEntity corpseEntity = BotDEntityTypes.CORPSE_ENTITY.create(serverWorld);
							if (corpseEntity != null && storedEntity instanceof LivingEntity storedLivingEntity) {
								corpseEntity.setCorpseEntity(storedLivingEntity);
								corpseEntity.refreshPositionAndAngles(pos, 0, 0);
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

	private void onButcheredEntity(LivingEntity livingEntity, BlockPos blockPos, DamageSource source) {
		if(source.getAttacker() instanceof PlayerEntity player && (player.getMainHandStack().isOf(BotDObjects.BUTCHER_KNIFE) || player.getMainHandStack().isOf(BotDObjects.BLOODY_BUTCHER_KNIFE) || EnchantmentHelper.getLevel(BotDEnchantments.BUTCHERING, player.getMainHandStack()) != 0)){
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
}
