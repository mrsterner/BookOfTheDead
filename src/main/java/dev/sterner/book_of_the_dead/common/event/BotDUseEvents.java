package dev.sterner.book_of_the_dead.common.event;

import dev.sterner.book_of_the_dead.api.block.HorizontalDoubleBlock;
import dev.sterner.book_of_the_dead.api.enums.HorizontalDoubleBlockHalf;
import dev.sterner.book_of_the_dead.api.interfaces.IHauler;
import dev.sterner.book_of_the_dead.common.block.RopeBlock;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

import static dev.sterner.book_of_the_dead.common.block.NecroTableBlock.LIT;
import static net.minecraft.block.HorizontalFacingBlock.FACING;

public class BotDUseEvents {


	public static void init() {
		UseBlockCallback.EVENT.register(BotDUseEvents::placeCorpse);
		UseBlockCallback.EVENT.register(BotDUseEvents::extendRope);
		UseBlockCallback.EVENT.register(BotDUseEvents::placeHook);
		UseBlockCallback.EVENT.register(BotDUseEvents::placeMetalHook);
		UseBlockCallback.EVENT.register(BotDUseEvents::createNecroTable);
		UseBlockCallback.EVENT.register(BotDUseEvents::createButcherTable);
		UseBlockCallback.EVENT.register(BotDUseEvents::createPedestal);
	}

	/**
	 * Adds a BlockStateParticle to a blockPos
	 *
	 * @param world      world
	 * @param blockPos   pos to spawn particle on
	 * @param blockState state for the particle to use
	 */
	private static void addParticle(World world, BlockPos blockPos, BlockState blockState) {
		world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState),
				blockPos.getX() + ((double) world.random.nextFloat() - 0.5D),
				blockPos.getY() + 0.1D,
				blockPos.getZ() + ((double) world.random.nextFloat() - 0.5D),
				4.0D * ((double) world.random.nextFloat() - 0.5D),
				0.5D,
				((double) world.random.nextFloat() - 0.5D) * 4.0D);
	}

	/**
	 * Used to convert Deepslate Tile Wall to a Pedestal
	 *
	 * @param player         player who is converting
	 * @param world          world
	 * @param hand           hand
	 * @param blockHitResult to get the pos of the block to convert
	 * @return CONSUME if conversion was successful
	 */
	private static ActionResult createPedestal(PlayerEntity player, World world, Hand hand, BlockHitResult blockHitResult) {
		if (!world.isClient() && hand == Hand.MAIN_HAND && player instanceof ServerPlayerEntity serverPlayerEntity) {
			BlockPos blockPos = blockHitResult.getBlockPos();
			if (player.getMainHandStack().isOf(BotDObjects.CARPENTER_TOOLS)) {
				if (world.getBlockState(blockPos).isOf(Blocks.DEEPSLATE_TILE_WALL)) {
					BlockState state = BotDObjects.PEDESTAL.getDefaultState();
					player.getMainHandStack().damage(1, world.random, serverPlayerEntity);
					world.breakBlock(blockPos, false);
					addParticle(world, blockPos, state);

					world.setBlockState(blockPos, state);
					return ActionResult.CONSUME;
				}
			}
		}
		return ActionResult.PASS;
	}

	/**
	 * Helper method to convert two blocks to two other blocks with the {@link HorizontalDoubleBlockHalf} property with a consumable tool
	 *
	 * @param useItem        the required and consumed item for the conversion
	 * @param targetBlock    the block to convert
	 * @param resultState    the state to convert to
	 * @param player         player who is converting
	 * @param world          world
	 * @param hand           hand
	 * @param blockHitResult to get the pos of the targetBlock
	 * @return CONSUME if conversion was successful
	 */
	private static ActionResult createDoubleBlock(Item useItem, Block targetBlock, BlockState resultState, PlayerEntity player, World world, Hand hand, BlockHitResult blockHitResult) {
		if (!world.isClient() && player.getMainHandStack().isOf(useItem) && hand == Hand.MAIN_HAND) {
			BlockPos blockPos = blockHitResult.getBlockPos();
			if (world.getBlockState(blockPos).isOf(targetBlock)) {
				BlockPos left = blockPos.offset(player.getHorizontalFacing().rotateYCounterclockwise());
				BlockPos right = blockPos.offset(player.getHorizontalFacing().rotateYClockwise());

				if (world.getBlockState(left).isOf(targetBlock)) {
					world.setBlockState(blockPos, resultState.with(HorizontalDoubleBlock.HHALF, HorizontalDoubleBlockHalf.RIGHT).with(FACING, player.getHorizontalFacing()));
					world.setBlockState(left, resultState.with(HorizontalDoubleBlock.HHALF, HorizontalDoubleBlockHalf.LEFT).with(FACING, player.getHorizontalFacing()));
				} else if (world.getBlockState(right).isOf(targetBlock)) {
					world.setBlockState(blockPos, resultState.with(HorizontalDoubleBlock.HHALF, HorizontalDoubleBlockHalf.LEFT).with(FACING, player.getHorizontalFacing()));
					world.setBlockState(right, resultState.with(HorizontalDoubleBlock.HHALF, HorizontalDoubleBlockHalf.RIGHT).with(FACING, player.getHorizontalFacing()));
				} else {
					return ActionResult.PASS;
				}
				return ActionResult.CONSUME;
			}
		}
		return ActionResult.PASS;
	}

	/**
	 * Uses the Paper and Quill to convert a Deepslate Tile to a Necro Table
	 *
	 * @param player         player who is converting the block
	 * @param world          world
	 * @param hand           hand
	 * @param blockHitResult to get the position of the deepslate
	 * @return CONSUME if successfully converted deepslate to necro table
	 */
	private static ActionResult createNecroTable(PlayerEntity player, World world, Hand hand, BlockHitResult blockHitResult) {
		if (!world.isClient() && player.getMainHandStack().isOf(BotDObjects.CARPENTER_TOOLS) && hand == Hand.MAIN_HAND) {
			BlockPos blockPos = blockHitResult.getBlockPos();
			if (world.getBlockState(blockPos).isOf(Blocks.DEEPSLATE_TILES)) {
				BlockState state = BotDObjects.NECRO_TABLE.getDefaultState();
				world.breakBlock(blockPos, false);
				addParticle(world, blockPos, state);

				world.setBlockState(blockPos, state.with(FACING, player.getHorizontalFacing()).with(LIT, false));
				return ActionResult.CONSUME;
			}
		}
		return ActionResult.PASS;
	}

	/**
	 * Uses the Butcher Knife to convert two adjacent Dark Oak Planks
	 *
	 * @param player         player who is converting the planks
	 * @param world          world
	 * @param hand           hand
	 * @param blockHitResult to get the position of the planks
	 * @return CONSUME if planks successfully converted to a butcher table
	 */
	private static ActionResult createButcherTable(PlayerEntity player, World world, Hand hand, BlockHitResult blockHitResult) {
		return createDoubleBlock(BotDObjects.CARPENTER_TOOLS, Blocks.DARK_OAK_PLANKS, BotDObjects.BUTCHER_TABLE.getDefaultState(), player, world, hand, blockHitResult);
	}

	/**
	 * Used to place a hook block at a rope with a hook item
	 *
	 * @param player         player who is placing the hook
	 * @param world          world
	 * @param hand           hand
	 * @param blockHitResult to get the position of the rope
	 * @return CONSUME if successfully placed a hook
	 */
	private static ActionResult placeHook(PlayerEntity player, World world, Hand hand, BlockHitResult blockHitResult) {
		if (!world.isClient() && player.getMainHandStack().isOf(BotDObjects.HOOK) && hand == Hand.MAIN_HAND && world.getBlockState(blockHitResult.getBlockPos()).isOf(BotDObjects.ROPE)) {
			if (world.getBlockState(blockHitResult.getBlockPos()).get(RopeBlock.ROPE) == RopeBlock.Rope.BOTTOM) {
				world.setBlockState(blockHitResult.getBlockPos(), BotDObjects.HOOK_BLOCK.getDefaultState().with(FACING, player.getHorizontalFacing()));
				if (!player.isCreative()) {
					player.getMainHandStack().decrement(1);
				}
				return ActionResult.CONSUME;
			}

		}
		return ActionResult.PASS;
	}

	/**
	 * Used to place a metal hook block at a chain with a metal hook item
	 *
	 * @param player         player who is placing the hook
	 * @param world          world
	 * @param hand           hand
	 * @param blockHitResult to get the position of the chain
	 * @return CONSUME if successfully placed a metal hook
	 */
	private static ActionResult placeMetalHook(PlayerEntity player, World world, Hand hand, BlockHitResult blockHitResult) {
		if (!world.isClient() && player.getMainHandStack().isOf(BotDObjects.METAL_HOOK) && hand == Hand.MAIN_HAND && world.getBlockState(blockHitResult.getBlockPos()).isOf(Blocks.CHAIN)) {
			if (world.getBlockState(blockHitResult.getBlockPos().down()).isOf(Blocks.AIR)) {
				world.setBlockState(blockHitResult.getBlockPos(), BotDObjects.METAL_HOOK_BLOCK.getDefaultState().with(FACING, player.getHorizontalFacing()));
				if (!player.isCreative()) {
					player.getMainHandStack().decrement(1);
				}
				return ActionResult.CONSUME;
			}

		}
		return ActionResult.PASS;
	}

	/**
	 * Used to make a rope extend down if player is using a rope item on a rope block with air below
	 *
	 * @param player         player using the rope
	 * @param world          world
	 * @param hand           hand
	 * @param blockHitResult to get the pos of the rope
	 * @return CONSUME if the rope was successfully extended
	 */
	private static ActionResult extendRope(PlayerEntity player, World world, Hand hand, BlockHitResult blockHitResult) {
		if (!world.isClient() && player.getMainHandStack().isOf(BotDObjects.ROPE.asItem()) && hand == Hand.MAIN_HAND && world.getBlockState(blockHitResult.getBlockPos()).isOf(BotDObjects.ROPE)) {
			int y = blockHitResult.getBlockPos().getY() - 1;
			while (world.getBlockState(new BlockPos(blockHitResult.getBlockPos().getX(), y, blockHitResult.getBlockPos().getZ())).isOf(BotDObjects.ROPE)) {
				y--;
			}
			BlockPos targetPos = new BlockPos(blockHitResult.getBlockPos().getX(), y, blockHitResult.getBlockPos().getZ());
			if (world.getBlockState(targetPos).isAir()) {
				world.setBlockState(targetPos, BotDObjects.ROPE.getDefaultState().with(RopeBlock.ROPE, RopeBlock.Rope.BOTTOM));
				if (!player.isCreative()) {
					player.getMainHandStack().decrement(1);
				}
				return ActionResult.CONSUME;
			}
		}
		return ActionResult.PASS;
	}

	/**
	 * Places a carried corpse on the ground when shifting and has a empty hand
	 *
	 * @param player         player who places the stored corpse
	 * @param world          world
	 * @param hand           hand
	 * @param blockHitResult to get which block to place at
	 * @return CONSUME on successfully placing a corpse
	 */
	private static ActionResult placeCorpse(PlayerEntity player, World world, Hand hand, BlockHitResult blockHitResult) {
		if (world instanceof ServerWorld serverWorld && hand == Hand.MAIN_HAND && player.getMainHandStack().isEmpty() && player.isSneaking()) {
			Optional<IHauler> iHaulerOptional = IHauler.of(player);
			if (iHaulerOptional.isPresent()) {
				IHauler hauler = iHaulerOptional.get();
				if (!hauler.getCorpseEntity().isEmpty()) {
					NbtCompound nbtCompound = hauler.getCorpseEntity();

					Optional<Entity> optionalEntity = EntityType.getEntityFromNbt(nbtCompound, world);
					if (optionalEntity.isPresent() && optionalEntity.get() instanceof LivingEntity storedLivingEntity) {
						BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());

						storedLivingEntity.refreshPositionAndAngles(pos, 0, 0);
						storedLivingEntity.teleport(pos.getX(), pos.getY(), pos.getZ());
						serverWorld.spawnEntity(storedLivingEntity);

						serverWorld.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1, 1);

						//Reset Data
						hauler.clearCorpseData();
						return ActionResult.CONSUME;
					}

				}
			}
		}
		return ActionResult.PASS;
	}
}
