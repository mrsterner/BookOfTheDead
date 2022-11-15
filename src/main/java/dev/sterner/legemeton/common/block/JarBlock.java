package dev.sterner.legemeton.common.block;

import dev.sterner.legemeton.common.block.entity.JarBlockEntity;
import dev.sterner.legemeton.common.registry.LegemetonBlockEntityTypes;
import dev.sterner.legemeton.common.registry.LegemetonObjects;
import dev.sterner.legemeton.common.util.LegemetonUtils;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.block.ShulkerBoxBlock.CONTENTS;

public class JarBlock extends BlockWithEntity {
	protected static final VoxelShape OPEN_SHAPE = VoxelShapes.union(
			createCuboidShape(4,0,4,12,11,5),
			createCuboidShape(4,0,4,5,11,12),
			createCuboidShape(4, 0, 11, 12, 11, 12),
			createCuboidShape(11, 0, 5, 12 ,11, 12),
			createCuboidShape(5,11,5,11,12,6),
			createCuboidShape(5,11,5,6,12,11),
			createCuboidShape(10, 11, 5, 11, 12, 11),
			createCuboidShape(5, 11, 10, 11 ,12, 11),
			createCuboidShape(4.5,12,4.5,11.5,14,5.5),
			createCuboidShape(4.5,12,4.5,5.5,14,11.5),
			createCuboidShape(4.5,12,10.5,11.5,14,11.5),
			createCuboidShape(10.5,12,4.5,11.5,14,11.5)
	);

	protected static final VoxelShape CLOSED_SHAPE = VoxelShapes.union(createCuboidShape(5, 14, 5, 11, 16, 11));
	public static final BooleanProperty OPEN = BooleanProperty.of("open");

	public JarBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(OPEN,false));
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return (tickerWorld, pos, tickerState, blockEntity) -> JarBlockEntity.tick(tickerWorld, pos, tickerState, (JarBlockEntity) blockEntity);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if(!world.isClient() && hand == Hand.MAIN_HAND){
			if(player.getMainHandStack().isEmpty()){
				world.setBlockState(pos, state.with(OPEN, !state.get(OPEN)));
				world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.PLAYERS, 1, 0.5f);
			}else if(world.getBlockEntity(pos) instanceof JarBlockEntity jarBlockEntity){
				if(player.getMainHandStack().isOf(Items.GLASS_BOTTLE)){
					if(jarBlockEntity.bloodAmount >= 25){
						handleBottle(jarBlockEntity, player, hand, LegemetonObjects.BOTTLE_OF_BLOOD, -25);
						return ActionResult.CONSUME;
					}
				}else if(player.getMainHandStack().isOf(LegemetonObjects.BOTTLE_OF_BLOOD)){
					if(jarBlockEntity.bloodAmount + 25 <= 100){
						handleBottle(jarBlockEntity, player, hand, Items.GLASS_BOTTLE, 25);
						return ActionResult.CONSUME;
					}
				}
			}
		}

		return super.onUse(state, world, pos, player, hand, hit);
	}

	public void handleBottle(JarBlockEntity jarBlockEntity, PlayerEntity player, Hand hand, Item item, int tooAdd){
		jarBlockEntity.bloodAmount = jarBlockEntity.bloodAmount + tooAdd;
		jarBlockEntity.markDirty();
		player.world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.PLAYERS, 1, 0.5f);
		LegemetonUtils.addItemToInventoryAndConsume(player, hand, item.getDefaultStack());
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof JarBlockEntity jarBlockEntity) {
			if (!world.isClient && player.isCreative() && !jarBlockEntity.isEmpty()) {
				ItemStack itemStack = new ItemStack(this);
				blockEntity.writeNbtToStack(itemStack);
				ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, itemStack);
				itemEntity.setToDefaultPickupDelay();
				world.spawnEntity(itemEntity);
			}
		}
		super.onBreak(world, pos, state, player);
	}

	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		ItemStack itemStack = super.getPickStack(world, pos, state);
		world.getBlockEntity(pos, LegemetonBlockEntityTypes.JAR).ifPresent((blockEntity) -> {
			blockEntity.writeNbtToStack(itemStack);
		});
		return itemStack;
	}

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, net.minecraft.loot.context.LootContext.Builder builder) {
		BlockEntity blockEntity = builder.getNullable(LootContextParameters.BLOCK_ENTITY);
		if (blockEntity instanceof JarBlockEntity jarBlockEntity) {
			builder = builder.putDrop(CONTENTS, (context, consumer) -> {
				for(int i = 0; i < jarBlockEntity.size(); ++i) {
					consumer.accept(jarBlockEntity.getStack(i));
				}
			});
		}
		return super.getDroppedStacks(state, builder);
	}


	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new JarBlockEntity(pos, state);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(OPEN);
	}


	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return state.get(OPEN) ? OPEN_SHAPE : VoxelShapes.union(CLOSED_SHAPE, OPEN_SHAPE);
	}
}
