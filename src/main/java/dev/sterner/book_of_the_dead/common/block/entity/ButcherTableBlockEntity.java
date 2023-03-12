package dev.sterner.book_of_the_dead.common.block.entity;

import com.mojang.datafixers.util.Pair;
import dev.sterner.book_of_the_dead.api.block.entity.BaseButcherBlockEntity;
import dev.sterner.book_of_the_dead.api.interfaces.IBlockEntityInventory;
import dev.sterner.book_of_the_dead.api.interfaces.IHauler;
import dev.sterner.book_of_the_dead.common.registry.*;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class ButcherTableBlockEntity extends BaseButcherBlockEntity implements IBlockEntityInventory, IHauler {


	public ButcherTableBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.BUTCHER, pos, state);
	}

	public ActionResult onUse(World world, BlockState state, BlockPos pos, PlayerEntity player, Hand hand) {
		if(hand == Hand.MAIN_HAND){
			IHauler.of(player).ifPresent(hauler -> {
				if(hauler.getCorpseEntity() != null){
					NbtCompound nbtCompound = hauler.getCorpseEntity();
					if(!nbtCompound.isEmpty()){
						setCorpse(nbtCompound);
						hauler.clearCorpseData();
					}
				}
			});
			if(getCorpseEntity() != null && !getCorpseEntity().isEmpty()){

				refreshButcheringRecipe();

				if(player.isSneaking() && player.getMainHandStack().isEmpty() && (this.butcheringRecipe == null || this.outputs.get(0).isOf(this.butcheringRecipe.getOutputs().stream().map(Pair::getFirst).toList().get(0).getItem()))){
					IHauler.of(player).ifPresent(hauler ->{
						Optional<Entity> entity = EntityType.getEntityFromNbt(getCorpseEntity(), world);

						if(hauler.getCorpseEntity().isEmpty() && entity.isPresent() && entity.get() instanceof LivingEntity livingEntity){
							hauler.setCorpseEntity(livingEntity);
							clearCorpseData();
							markDirty();
							this.outputs = DefaultedList.ofSize(8, ItemStack.EMPTY);
							this.chances = DefaultedList.ofSize(8, 1.0f);
						}
					});
					this.resetRecipe = true;
				} else if(player.getMainHandStack().isOf(BotDObjects.BUTCHER_KNIFE)){
					player.swingHand(hand);
					player.swingHand(hand, true);
					world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_HONEY_BLOCK_BREAK, SoundCategory.PLAYERS, 2,1);
					world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 1,1);
					List<ItemStack> nonEmptyOutput = this.outputs.stream().filter(item -> !item.isEmpty() || !item.isOf(Items.AIR) || item.getCount() != 0).toList();
					List<Float> nonEmptyChance = this.chances.stream().filter(chance -> chance != 0).toList();
					if(nonEmptyOutput.size() > 0){
						if(nonEmptyChance.size() > 0){
							if(world.getRandom().nextFloat() < nonEmptyChance.get(0)){
								ItemScatterer.spawn(world, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, nonEmptyOutput.get(0));
								this.chances.set(0, 0F);
							}
						}else{
							ItemScatterer.spawn(world, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, nonEmptyOutput.get(0));
						}
						this.outputs.set(0, ItemStack.EMPTY);
						nonEmptyOutput = this.outputs.stream().filter(item -> !item.isEmpty() || !item.isOf(Items.AIR) || item.getCount() != 0).toList();
						if(nonEmptyOutput.isEmpty()){
							clearCorpseData();
							clear();
							this.setCorpse(new NbtCompound());
							butcheringRecipe = null;
							resetRecipe = true;
							markDirty();
						}
					}else {
						clearCorpseData();
						this.setCorpse(new NbtCompound());
						butcheringRecipe = null;
						resetRecipe = true;
						clear();
						markDirty();
					}
				}
			}
		}
		markDirty();
		return ActionResult.PASS;
	}
}
