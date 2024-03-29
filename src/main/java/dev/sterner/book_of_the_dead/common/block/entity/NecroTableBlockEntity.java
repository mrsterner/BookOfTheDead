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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.*;

public class NecroTableBlockEntity extends BaseBlockEntity {
	public List<PedestalInfo> pedestalToActivate = new ArrayList<>();
	public List<LivingEntity> sacrificeCache = new ArrayList<>();

	public boolean isNecroTable = false;
	public boolean hasBotD = false;
	public boolean hasEmeraldTablet = false;
	public BlockPos ritualPos = null;
	public List<BlockPos> pedestalPosList = new ArrayList<>();
	public BasicNecrotableRitual currentBasicNecrotableRitual = null;
	public RitualRecipe ritualRecipe = null;
	public UUID userUuid = null;

	//Logic
	private boolean loaded = false;
	public int timer = -20;
	public boolean shouldRun = false;
	public int clientTime = 0;

	public NecroTableBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.NECRO, pos, state);
	}

	public void tick(World world, BlockPos pos, BlockState state) {
		if (!isNecroTable || (world == null || world.isClient())) {
			return;
		}

		if (!loaded) {
			reset();
			markDirty();
			loaded = true;
		}

		if (!shouldRun) {
			return;
		}

		collectPedestalBlockPos(world, ritualPos);
		sendRitualPosition(world);

		if (ritualRecipe == null || currentBasicNecrotableRitual == null) {
			ritualRecipe = BotDRecipeTypes.getRiteRecipe(this);
			markDirty();

			if (ritualRecipe != null && checkValidSacrifices(ritualRecipe, world)) {
				currentBasicNecrotableRitual = ritualRecipe.ritual();
			}

			if (ritualRecipe == null) {
				reset();
			}
		} else if (!checkTier()) {
			reset();
		} else {

			int craftingTime = ritualRecipe.inputs() != null ? ritualRecipe.inputs().stream().filter(ingredient -> !ingredient.isEmpty()).toList().size() * 20 * 4 + 20 * 2 : 0;
			int sacrificeTime = ritualRecipe.sacrifices() != null ? ritualRecipe.sacrifices().size() * 20 * 3 + 20 * 2 : 0;

			if (currentBasicNecrotableRitual.ritualManager.userUuid == null) {
				currentBasicNecrotableRitual.ritualManager.userUuid = userUuid;
			}

			timer++;

			if (timer >= ritualRecipe.duration() + Math.max(craftingTime, sacrificeTime)) {
				currentBasicNecrotableRitual.onStopped(world, ritualPos, this);
				reset();
			} else if (timer >= 0) {
				currentBasicNecrotableRitual.tick(world, ritualPos, this);
			}
		}

		clientTime++;
		markDirty();
	}

	public ActionResult onUse(World world, BlockState state, BlockPos pos, PlayerEntity player, Hand hand) {
		if (world != null && hand == Hand.MAIN_HAND) {
			ItemStack handStack = player.getMainHandStack();
			if (!isNecroTable) {
				if (handStack.isOf(BotDObjects.PAPER_AND_QUILL)) {
					isNecroTable = true;
					markDirty();
					return ActionResult.CONSUME;
				}
				return ActionResult.PASS;
			}

			if (handStack.isOf(Items.FLINT_AND_STEEL)) {
				world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
				world.setBlockState(pos, state.with(Properties.LIT, Boolean.TRUE), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
				world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
				handStack.damage(1, player, p -> p.sendToolBreakStatus(hand));
				return ActionResult.CONSUME;
			} else if (!shouldRun) {
				if (handStack.isEmpty()) {
					if (player.isSneaking()) {
						if (hasBotD) {
							player.setStackInHand(hand, BotDObjects.BOOK_OF_THE_DEAD.asItem().getDefaultStack());
							hasBotD = false;
							playItemSound(world, pos);
						} else if (hasEmeraldTablet) {
							player.setStackInHand(hand, BotDObjects.EMERALD_TABLET.asItem().getDefaultStack());
							hasEmeraldTablet = false;
							playItemSound(world, pos);
						}
					} else {
						Direction dir = state.get(NecroTableBlock.FACING);
						ritualPos = pos.offset(dir, 4);
						shouldRun = true;
					}
					markDirty();
					return ActionResult.CONSUME;
				} else if (handStack.isOf(BotDObjects.BOOK_OF_THE_DEAD.asItem()) && !hasBotD) {
					hasBotD = true;
					handStack.decrement(1);
					playItemSound(world, pos);
					markDirty();
					return ActionResult.CONSUME;
				} else if (handStack.isOf(BotDObjects.EMERALD_TABLET.asItem()) && !hasEmeraldTablet) {
					hasEmeraldTablet = true;
					handStack.decrement(1);
					playItemSound(world, pos);
					markDirty();
					return ActionResult.CONSUME;
				}

				userUuid = player.getUuid();
				markDirty();
			}
		}
		return ActionResult.PASS;
	}

	private void collectPedestalBlockPos(World world, BlockPos pos) {
		int r = 5;
		Set<BlockPos> pedestalPositions = new HashSet<>();
		BlockPos.iterate(pos.add(-r, -r, -r), pos.add(r, r, r)).forEach(lookPos -> {
			BlockState state = world.getBlockState(lookPos);
			if (state.isOf(BotDObjects.PEDESTAL) && !pedestalPosList.contains(lookPos)) {
				pedestalPositions.add(lookPos.toImmutable());
			}
		});
		pedestalPosList.addAll(pedestalPositions);
	}

	public void sendRitualPosition(World world) {
		for (BlockPos pos : pedestalPosList) {
			if (world.getBlockState(pos).isOf(BotDObjects.PEDESTAL) && world.getBlockEntity(pos) instanceof PedestalBlockEntity pedestalBlockEntity) {
				pedestalBlockEntity.ritualCenter = new Vec3d(this.ritualPos.getX(), this.ritualPos.getY(), this.ritualPos.getZ());
				pedestalBlockEntity.markDirty();
			}
		}
	}

	public List<PedestalInfo> getPedestalInfo(World world) {
		List<PedestalInfo> pairs = new ArrayList<>();
		for (BlockPos pos : pedestalPosList) {
			if (world.getBlockState(pos).isOf(BotDObjects.PEDESTAL) && world.getBlockEntity(pos) instanceof PedestalBlockEntity pedestalBlockEntity) {
				pairs.add(new PedestalInfo(pedestalBlockEntity.getStack(), pos));
			}
		}
		return pairs;
	}

	private boolean checkValidSacrifices(RitualRecipe ritual, World world) {
		if (ritual.sacrifices() != null && ritual.sacrifices().isEmpty()) {
			return true;
		}

		int size = 8;

		List<LivingEntity> livingEntityList = world.getEntitiesByClass(LivingEntity.class, new Box(this.pos).expand(size), Entity::isAlive);
		List<EntityType<?>> entityTypeList = Lists.newArrayList(livingEntityList.stream().map(Entity::getType).toList());
		List<EntityType<?>> ritualSacrifices = ritual.sacrifices();

		return ritualSacrifices != null && new HashSet<>(entityTypeList).containsAll(ritualSacrifices);
	}

	public boolean checkTier() {
		if (ritualRecipe.requireEmeraldTablet() && !hasEmeraldTablet) {
			return false;
		}

		return !ritualRecipe.requireBotD() || hasBotD;
	}

	public void reset() {
		currentBasicNecrotableRitual = null;
		timer = -20;
		shouldRun = false;
		pedestalToActivate.clear();
		sacrificeCache.clear();
		ritualRecipe = null;
		clientTime = 0;
		userUuid = null;
		pedestalPosList.clear();
		markDirty();
	}

	public void playItemSound(World world, BlockPos pos) {
		world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.5f, ((world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.hasBotD = nbt.getBoolean(Constants.Nbt.HAS_LEGEMETON);
		this.hasEmeraldTablet = nbt.getBoolean(Constants.Nbt.HAS_EMERALD_TABLET);
		this.isNecroTable = nbt.getBoolean(Constants.Nbt.IS_NECRO);
		this.timer = nbt.getInt(Constants.Nbt.TIMER);
		this.clientTime = nbt.getInt(Constants.Nbt.CLIENT_TIMER);
		this.shouldRun = nbt.getBoolean(Constants.Nbt.SHOULD_RUN);

		if (nbt.contains(Constants.Nbt.UUID)) {
			this.userUuid = nbt.getUuid(Constants.Nbt.UUID);
		}

		if (nbt.contains(Constants.Nbt.NECRO_RITUAL)) {
			this.currentBasicNecrotableRitual = BotDRegistries.NECROTABLE_RITUALS.get(new Identifier(nbt.getString(Constants.Nbt.NECRO_RITUAL)));
		}

		if (nbt.contains(Constants.Nbt.RITUAL_POS)) {
			this.ritualPos = NbtHelper.toBlockPos(nbt.getCompound(Constants.Nbt.RITUAL_POS));
		}

		if (world != null) {
			Optional<RitualRecipe> optional = world.getRecipeManager().listAllOfType(BotDRecipeTypes.RITUAL_RECIPE_TYPE).stream().filter(ritualRecipe1 -> ritualRecipe1.id().equals(new Identifier(nbt.getString(Constants.Nbt.RITUAL_RECIPE)))).findFirst();
			optional.ifPresent(recipe -> ritualRecipe = recipe);
		}

		markDirty();
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putBoolean(Constants.Nbt.HAS_LEGEMETON, this.hasBotD);
		nbt.putBoolean(Constants.Nbt.HAS_EMERALD_TABLET, this.hasEmeraldTablet);
		nbt.putBoolean(Constants.Nbt.IS_NECRO, this.isNecroTable);
		nbt.putInt(Constants.Nbt.TIMER, this.timer);
		nbt.putInt(Constants.Nbt.CLIENT_TIMER, this.clientTime);
		nbt.putBoolean(Constants.Nbt.SHOULD_RUN, this.shouldRun);

		if (this.userUuid != null) {
			nbt.putUuid(Constants.Nbt.UUID, this.userUuid);
		}

		if (this.ritualPos != null) {
			nbt.put(Constants.Nbt.RITUAL_POS, NbtHelper.fromBlockPos(this.ritualPos));
		}

		if (currentBasicNecrotableRitual != null) {
			Identifier id = BotDRegistries.NECROTABLE_RITUALS.getId(currentBasicNecrotableRitual);
			if (id != null) {
				nbt.putString(Constants.Nbt.NECRO_RITUAL, id.toString());
			}
		}

		if (ritualRecipe != null) {
			nbt.putString(Constants.Nbt.RITUAL_RECIPE, this.ritualRecipe.id().toString());
		}
	}
}
