package dev.sterner.book_of_the_dead.common.block;

import dev.sterner.book_of_the_dead.common.block.entity.JarBlockEntity;
import dev.sterner.book_of_the_dead.common.registry.BotDBlockEntityTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.util.BotDUtils;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static dev.sterner.book_of_the_dead.common.block.entity.JarBlockEntity.*;
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
			ItemStack stack = player.getMainHandStack();
			 if(world.getBlockEntity(pos) instanceof JarBlockEntity jarBlockEntity){
				 System.out.println(jarBlockEntity.liquidType);
				 if(stack.isEmpty()){
					 if(player.isSneaking()){
						 world.setBlockState(pos, state.with(OPEN, !state.get(OPEN)));
						 world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.PLAYERS, 1, 0.5f);
					 }else if(jarBlockEntity.hasBrain){
						 ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(BotDObjects.BRAIN));
						 jarBlockEntity.hasBrain = false;
					 }
					 jarBlockEntity.markDirty();
					 return ActionResult.CONSUME;
				 }else if(stack.isOf(Items.GLASS_BOTTLE)){
					if(jarBlockEntity.liquidAmount >= 25 && !jarBlockEntity.getLiquidType(EMPTY)){
						Item outStack = switch (jarBlockEntity.liquidType){
							case BLOOD -> BotDObjects.BOTTLE_OF_BLOOD;
							case ACID -> BotDObjects.SULFURIC_ACID;
							default -> Items.POTION;
						};
						jarBlockEntity.liquidAmount = jarBlockEntity.liquidAmount - 25;
						handleBottle(jarBlockEntity, player, hand, outStack);
						if(jarBlockEntity.liquidAmount <= 0){
							jarBlockEntity.setLiquidType(EMPTY);
							jarBlockEntity.markDirty();
						}

					}
				 }else if(stack.isOf(BotDObjects.BOTTLE_OF_BLOOD)){
					 System.out.println("Blood: " + jarBlockEntity.getLiquidType(EMPTY));
					 if((jarBlockEntity.getLiquidType(BLOOD) || jarBlockEntity.getLiquidType(EMPTY))){
						 if(jarBlockEntity.liquidAmount + 25 <= MAX_LIQUID){
							 jarBlockEntity.setLiquidType(BLOOD);
							 jarBlockEntity.liquidAmount = jarBlockEntity.liquidAmount + 25;
							 handleBottle(jarBlockEntity, player, hand, Items.GLASS_BOTTLE);
							 return ActionResult.CONSUME;
						 }
					 }
				 }else if(stack.isOf(BotDObjects.SULFURIC_ACID)){
					 if((jarBlockEntity.getLiquidType(ACID) || jarBlockEntity.getLiquidType(EMPTY))){
						 if(jarBlockEntity.liquidAmount + 25 <= MAX_LIQUID){
							 jarBlockEntity.setLiquidType(ACID);
							 jarBlockEntity.liquidAmount = jarBlockEntity.liquidAmount + 25;
							 handleBottle(jarBlockEntity, player, hand, Items.GLASS_BOTTLE);
							 return ActionResult.CONSUME;
						 }
					 }
				 }else if(stack.isOf(Items.POTION) && PotionUtil.getPotion(stack) == Potions.WATER){
					 if((jarBlockEntity.getLiquidType(WATER) || jarBlockEntity.getLiquidType(EMPTY))){
						 if(jarBlockEntity.liquidAmount + 25 <= MAX_LIQUID){
							 jarBlockEntity.setLiquidType(WATER);
							 jarBlockEntity.liquidAmount = jarBlockEntity.liquidAmount + 25;
							 handleBottle(jarBlockEntity, player, hand, Items.GLASS_BOTTLE);
							 return ActionResult.CONSUME;
						 }
					 }
				 }else if (stack.isOf(BotDObjects.BRAIN.asItem()) || stack.isOf(BotDObjects.EYE)) {
					 if(jarBlockEntity.liquidAmount == MAX_LIQUID && jarBlockEntity.getLiquidType(WATER)){
						 if(!jarBlockEntity.hasBrain){
							 jarBlockEntity.hasBrain = true;
							 player.getMainHandStack().decrement(1);
							 jarBlockEntity.markDirty();
							 return ActionResult.CONSUME;
						 }
					 }
				}
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	public void handleBottle(JarBlockEntity jarBlockEntity, PlayerEntity player, Hand hand, Item item){
		player.world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.PLAYERS, 1, 0.5f);
		BotDUtils.addItemToInventoryAndConsume(player, hand, item.getDefaultStack());
		jarBlockEntity.markDirty();
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof JarBlockEntity jarBlockEntity) {
			if (!world.isClient) {
				ItemStack itemStack = new ItemStack(this);
				jarBlockEntity.writeNbtToStack(itemStack);
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
		world.getBlockEntity(pos, BotDBlockEntityTypes.JAR).ifPresent((blockEntity) -> {
			blockEntity.writeNbtToStack(itemStack);
		});
		return itemStack;
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
