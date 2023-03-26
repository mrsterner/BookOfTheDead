package dev.sterner.book_of_the_dead.common.block.entity;

import com.google.common.collect.Lists;
import dev.sterner.book_of_the_dead.api.PedestalInfo;
import dev.sterner.book_of_the_dead.api.block.entity.BaseBlockEntity;
import dev.sterner.book_of_the_dead.common.block.NecroTableBlock;
import dev.sterner.book_of_the_dead.common.recipe.RitualRecipe;
import dev.sterner.book_of_the_dead.common.registry.BotDBlockEntityTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.registry.BotDRecipeTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDRegistries;
import dev.sterner.book_of_the_dead.common.rituals.BasicNecrotableRitual;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;
import java.util.stream.Collectors;

public class NecroTableBlockEntity extends BaseBlockEntity {
	public List<PedestalInfo> pedestalToActivate = new ArrayList<>();

	public boolean hasBotD = false;
	public boolean hasEmeraldTablet = false;
	public BlockPos ritualPos = null;
	public List<BlockPos> PEDESTAL_POS_LIST = new ArrayList<>();
	public BasicNecrotableRitual currentBasicNecrotableRitual = null;
	public RitualRecipe ritualRecipe = null;

	//Logic
	private boolean loaded = false;
	public int timer = -20;
	public long age = 0;
	public boolean shouldRun = false;
	public int clientTime = 0;

	public NecroTableBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.NECRO, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState blockState, NecroTableBlockEntity blockEntity) {
		if (world != null && !world.isClient()) {
			if (!blockEntity.loaded) {
				blockEntity.markDirty();
				blockEntity.loaded = true;
			}
			blockEntity.age++;
			if (blockEntity.shouldRun) {
				blockEntity.collectPedestalBlockPos(world, pos);
				blockEntity.sendRitualPosition(world);

				if (blockEntity.ritualRecipe == null || blockEntity.currentBasicNecrotableRitual == null) {
					blockEntity.ritualRecipe = BotDRecipeTypes.getRiteRecipe(blockEntity);

					if (blockEntity.ritualRecipe != null) {
						if (blockEntity.checkValidSacrifices(blockEntity.ritualRecipe, world)) {
							blockEntity.currentBasicNecrotableRitual = blockEntity.ritualRecipe.ritual;
						}
					}

				} else if (blockEntity.checkTier(blockEntity)) {
					int craftingTime = blockEntity.ritualRecipe.inputs.stream().filter(ingredient -> !ingredient.isEmpty()).toList().size() * 20 * 4 + 20 * 2;
					blockEntity.timer++;
					if (blockEntity.timer >= 0) {
						blockEntity.currentBasicNecrotableRitual.tick(world, blockEntity.ritualPos, blockEntity);
					}
					if (blockEntity.timer >= blockEntity.ritualRecipe.getDuration() + craftingTime) {
						blockEntity.currentBasicNecrotableRitual.onStopped(world, blockEntity.ritualPos, blockEntity);
						blockEntity.reset(blockEntity);
					}
				} else {
					blockEntity.reset(blockEntity);
				}
			}
		}
		if (world != null) {
			if (blockEntity.shouldRun) {
				if (world.isClient()) {
					blockEntity.clientTime++;
				}
			}
		}
	}

	private void collectPedestalBlockPos(World world, BlockPos pos) {
		int r = 5;
		for(int x = -r; x < r; x++){
			for(int y = -r; y < r; y++){
				for(int z = -r; z < r; z++){
					BlockPos lookPos = pos.add(x,y,z);
					BlockState state = world.getBlockState(lookPos);
					if(state.isOf(BotDObjects.PEDESTAL)){
						if(!PEDESTAL_POS_LIST.contains(lookPos)){
							PEDESTAL_POS_LIST.add(lookPos);
						}
					}
				}
			}
		}
	}

	public ActionResult onUse(World world, BlockState state, BlockPos pos, PlayerEntity player, Hand hand) {
		if (world != null && world.getBlockEntity(pos) instanceof NecroTableBlockEntity necroTableBlockEntity) {
			if (player.getMainHandStack().isEmpty() && hand == Hand.MAIN_HAND) {
				if (player.isSneaking()) {
					if (necroTableBlockEntity.hasBotD) {
						player.setStackInHand(hand, BotDObjects.BOOK_OF_THE_DEAD.getDefaultStack());
						necroTableBlockEntity.hasBotD = false;
						this.playItemSound(world, pos);
					} else if (necroTableBlockEntity.hasEmeraldTablet) {
						player.setStackInHand(hand, BotDObjects.EMERALD_TABLET.getDefaultStack());
						necroTableBlockEntity.hasEmeraldTablet = false;
						this.playItemSound(world, pos);
					}
					necroTableBlockEntity.markDirty();
				} else {
					Direction dir = state.get(NecroTableBlock.FACING);
					necroTableBlockEntity.ritualPos = pos.offset(dir, 4);
					this.shouldRun = true;
					this.markDirty();
				}
			} else if (player.getMainHandStack().isOf(BotDObjects.BOOK_OF_THE_DEAD)) {
				necroTableBlockEntity.hasBotD = true;
				player.getMainHandStack().decrement(1);
				this.playItemSound(world, pos);
				necroTableBlockEntity.markDirty();
			} else if (player.getMainHandStack().isOf(BotDObjects.EMERALD_TABLET)) {
				necroTableBlockEntity.hasEmeraldTablet = true;
				player.getMainHandStack().decrement(1);
				this.playItemSound(world, pos);
				necroTableBlockEntity.markDirty();
			}
		}
		return ActionResult.PASS;
	}

	public void sendRitualPosition(World world) {
		for (BlockPos pos : PEDESTAL_POS_LIST) {
			if (world.getBlockState(pos).isOf(BotDObjects.PEDESTAL) && world.getBlockEntity(pos) instanceof PedestalBlockEntity pedestalBlockEntity) {
				pedestalBlockEntity.ritualCenter = new Vec3d(this.ritualPos.getX(), this.ritualPos.getY(), this.ritualPos.getZ());
				pedestalBlockEntity.markDirty();
			}
		}
	}

	public List<PedestalInfo> getPedestalInfo(World world) {
		List<PedestalInfo> pairs = new ArrayList<>();
		for (BlockPos pos : PEDESTAL_POS_LIST) {
			if (world.getBlockState(pos).isOf(BotDObjects.PEDESTAL) && world.getBlockEntity(pos) instanceof PedestalBlockEntity pedestalBlockEntity) {
				pairs.add(PedestalInfo.of(pedestalBlockEntity.getStack(), pos));
			}
		}
		return pairs;
	}

	private boolean checkValidSacrifices(RitualRecipe ritual, World world) {
		if (ritual.sacrifices != null && ritual.sacrifices.isEmpty()) {
			return true;
		}

		int size = 8;

		List<LivingEntity> livingEntityList = world.getEntitiesByClass(LivingEntity.class, new Box(this.pos).expand(size), Entity::isAlive);
		List<EntityType<?>> entityTypeList = Lists.newArrayList(livingEntityList.stream().map(Entity::getType).toList());
		List<EntityType<?>> ritualSacrifices = ritual.sacrifices;

		return ritualSacrifices != null && new HashSet<>(entityTypeList).containsAll(ritualSacrifices);
	}

	public boolean checkTier(NecroTableBlockEntity blockEntity) {
		boolean tablet = blockEntity.ritualRecipe.requireEmeraldTablet;
		boolean botD = blockEntity.ritualRecipe.requireBotD;
		boolean tabletMatch = tablet && blockEntity.hasEmeraldTablet;
		boolean botDMatch = botD && blockEntity.hasBotD;
		if (!tablet && !botD) {
			return true;
		}
		if (botDMatch && tabletMatch) {
			return true;
		}
		return botDMatch || tabletMatch;
	}

	public void reset(NecroTableBlockEntity blockEntity) {
		blockEntity.currentBasicNecrotableRitual = null;
		blockEntity.timer = -20;
		blockEntity.shouldRun = false;
		blockEntity.pedestalToActivate.clear();
		blockEntity.markDirty();
	}

	private void playItemSound(World world, BlockPos pos) {
		world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.5f, ((world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
	}

	@Override
	public void markDirty() {
		super.markDirty();
		if (world != null && !world.isClient) {
			world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
			toUpdatePacket();
		}
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.hasBotD = nbt.getBoolean(Constants.Nbt.HAS_LEGEMETON);
		this.hasEmeraldTablet = nbt.getBoolean(Constants.Nbt.HAS_EMERALD_TABLET);
		if (nbt.contains(Constants.Nbt.RITUAL_POS)) {
			this.ritualPos = NbtHelper.toBlockPos(nbt.getCompound(Constants.Nbt.RITUAL_POS));
		}

		currentBasicNecrotableRitual = BotDRegistries.NECROTABLE_RITUALS.get(new Identifier(nbt.getString(Constants.Nbt.NECRO_RITUAL)));
		if (world != null) {
			Optional<RitualRecipe> optional = world.getRecipeManager().listAllOfType(BotDRecipeTypes.RITUAL_RECIPE_TYPE).stream()
					.filter(ritualRecipe1 -> ritualRecipe1.id.equals(new Identifier(nbt.getString(Constants.Nbt.RITUAL_RECIPE)))).findFirst();
			optional.ifPresent(recipe -> ritualRecipe = recipe);
		}
		this.timer = nbt.getInt(Constants.Nbt.TIMER);
		this.clientTime = nbt.getInt(Constants.Nbt.CLIENT_TIMER);
		this.age = nbt.getLong(Constants.Nbt.AGE);
		this.shouldRun = nbt.getBoolean(Constants.Nbt.SHOULD_RUN);

		markDirty();
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putBoolean(Constants.Nbt.HAS_LEGEMETON, this.hasBotD);
		nbt.putBoolean(Constants.Nbt.HAS_EMERALD_TABLET, this.hasEmeraldTablet);
		if (this.ritualPos != null) {
			nbt.put(Constants.Nbt.RITUAL_POS, NbtHelper.fromBlockPos(this.ritualPos));
		}

		if (currentBasicNecrotableRitual != null) {
			nbt.putString(Constants.Nbt.NECRO_RITUAL, BotDRegistries.NECROTABLE_RITUALS.getId(currentBasicNecrotableRitual).toString());
		}
		if (ritualRecipe != null) {
			nbt.putString(Constants.Nbt.RITUAL_RECIPE, this.ritualRecipe.id.toString());
		}
		nbt.putInt(Constants.Nbt.TIMER, this.timer);
		nbt.putInt(Constants.Nbt.CLIENT_TIMER, this.clientTime);
		nbt.putLong(Constants.Nbt.AGE, this.age);
		nbt.putBoolean(Constants.Nbt.SHOULD_RUN, this.shouldRun);
	}
}
