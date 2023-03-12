package dev.sterner.book_of_the_dead.common.block.entity;

import dev.sterner.book_of_the_dead.api.NecrotableRitual;
import dev.sterner.book_of_the_dead.api.block.entity.BaseBlockEntity;
import dev.sterner.book_of_the_dead.common.recipe.RitualRecipe;
import dev.sterner.book_of_the_dead.common.registry.*;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class RitualBlockEntity extends BaseBlockEntity {
	public static final List<BlockPos> PEDESTAL_POS_LIST;
	public NecrotableRitual currentNecrotableRitual = null;
	public RitualRecipe ritualRecipe = null;

	//NecroTableInfo
	public boolean hasBotD = false;
	public boolean hasEmeraldTablet = false;
	public UUID user = null;

	//Logic
	private boolean loaded = false;
	public int timer = 0;
	public long age = 0;
	public boolean startGate = true;
	public boolean shouldRun = false;
	public int clientTime = 0;

	public RitualBlockEntity(BlockPos pos, BlockState state) {
		super(BotDBlockEntityTypes.RITUAL, pos, state);
	}

	public ActionResult onUse(World world, BlockState state, BlockPos pos, PlayerEntity player, Hand hand) {
		if (world != null) {
			return ActionResult.CONSUME;
		}
		return ActionResult.PASS;
	}

	public void sendRitualPosition(World world){
		for(BlockPos pos : PEDESTAL_POS_LIST){
			BlockPos specificPos = pos.add(getPos());
			if(world.getBlockState(specificPos).isOf(BotDObjects.PEDESTAL) && world.getBlockEntity(specificPos) instanceof PedestalBlockEntity pedestalBlockEntity){
				pedestalBlockEntity.ritualCenter = getPos();
				pedestalBlockEntity.markDirty();
			}
		}
	}

	public List<Pair<ItemStack, BlockPos>> getPedestalInfo(World world){
		List<Pair<ItemStack, BlockPos>> pairs = new ArrayList<>();
		for(BlockPos pos : PEDESTAL_POS_LIST){
			BlockPos specificPos = pos.add(getPos());
			if(world.getBlockState(specificPos).isOf(BotDObjects.PEDESTAL) && world.getBlockEntity(specificPos) instanceof PedestalBlockEntity pedestalBlockEntity){
				pairs.add(new Pair<>(pedestalBlockEntity.getStack(), pos));
			}
		}
		return pairs;
	}

	public static void tick(World world, BlockPos pos, BlockState blockState, RitualBlockEntity blockEntity) {
		if (world != null && !world.isClient()) {
			if (!blockEntity.loaded) {
				blockEntity.markDirty();
				blockEntity.loaded = true;
			}
			blockEntity.age++;
			if(blockEntity.shouldRun){
				blockEntity.sendRitualPosition(world);
				SimpleInventory tempInv = new SimpleInventory(8);
				List<ItemStack> pedestalItemList = blockEntity.getPedestalInfo(world).stream().map(Pair::getLeft).toList();
				for(ItemStack itemStack : pedestalItemList){
					if(!itemStack.isEmpty()){
						tempInv.addStack(itemStack.copy());
					}
				}

				if(blockEntity.ritualRecipe == null || blockEntity.currentNecrotableRitual == null){
					blockEntity.ritualRecipe = BotDRecipeTypes.getRiteRecipe(blockEntity);
					if(blockEntity.ritualRecipe != null){
						blockEntity.currentNecrotableRitual = blockEntity.ritualRecipe.ritual;
					}

				}else if(blockEntity.checkTier(blockEntity)){
					blockEntity.currentNecrotableRitual.recipe = blockEntity.ritualRecipe;
					if(blockEntity.user != null){
						blockEntity.currentNecrotableRitual.user = blockEntity.user;
					}
					if (blockEntity.startGate) {
						blockEntity.currentNecrotableRitual.onStart(world, pos, blockEntity);
						blockEntity.startGate = false;
					}
					blockEntity.timer++;
					if (blockEntity.timer >= 20) {
						blockEntity.currentNecrotableRitual.tick(world, pos, blockEntity);
					}
					if (blockEntity.timer >= blockEntity.currentNecrotableRitual.recipe.getDuration() + 20) {
						blockEntity.currentNecrotableRitual.onStopped(world, pos, blockEntity);
						blockEntity.reset(blockEntity);
					}
				}else{
					blockEntity.reset(blockEntity);
				}
			}
		}
		if(world != null){
			if(blockEntity.shouldRun){
				if(world.isClient()){
					blockEntity.clientTime++;
				}
			}
		}
	}

	public boolean checkTier(RitualBlockEntity blockEntity){
		if(!blockEntity.ritualRecipe.requireEmeraldTablet && !blockEntity.ritualRecipe.requireBotD){
			return true;
		}
		if(blockEntity.ritualRecipe.requireBotD && blockEntity.hasBotD && blockEntity.ritualRecipe.requireEmeraldTablet && blockEntity.hasEmeraldTablet){
			return true;
		}
		if(blockEntity.ritualRecipe.requireBotD && blockEntity.hasBotD){
			return true;
		}
		if(blockEntity.ritualRecipe.requireEmeraldTablet && blockEntity.hasEmeraldTablet){
			return true;
		}
		return false;
	}

	public void reset(RitualBlockEntity blockEntity){
		blockEntity.currentNecrotableRitual = null;
		blockEntity.user = null;
		blockEntity.timer = 0;
		blockEntity.startGate = true;
		blockEntity.shouldRun = false;
		blockEntity.markDirty();
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		currentNecrotableRitual = BotDRegistries.NECROTABLE_RITUALS.get(new Identifier(nbt.getString(Constants.Nbt.NECRO_RITUAL)));
		if(world != null){
			Optional<RitualRecipe> optional = world.getRecipeManager().listAllOfType(BotDRecipeTypes.RITUAL_RECIPE_TYPE).stream()
					.filter(ritualRecipe1 -> ritualRecipe1.id.equals(new Identifier(nbt.getString(Constants.Nbt.RITUAL_RECIPE)))).findFirst();
			optional.ifPresent(recipe -> ritualRecipe = recipe);
		}
		this.timer = nbt.getInt(Constants.Nbt.TIMER);
		this.clientTime = nbt.getInt(Constants.Nbt.CLIENT_TIMER);
		this.age = nbt.getLong(Constants.Nbt.AGE);
		if (nbt.contains(Constants.Nbt.PLAYER_UUID)) {
			user = nbt.getUuid(Constants.Nbt.PLAYER_UUID);
		} else {
			user = null;
		}
		this.startGate = nbt.getBoolean(Constants.Nbt.START);
		this.shouldRun = nbt.getBoolean(Constants.Nbt.SHOULD_RUN);
		this.hasBotD = nbt.getBoolean(Constants.Nbt.HAS_LEGEMETON);
		this.hasEmeraldTablet = nbt.getBoolean(Constants.Nbt.HAS_EMERALD_TABLET);
		markDirty();
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (currentNecrotableRitual != null) {
			nbt.putString(Constants.Nbt.NECRO_RITUAL, BotDRegistries.NECROTABLE_RITUALS.getId(currentNecrotableRitual).toString());
		}
		if(ritualRecipe != null){
			nbt.putString(Constants.Nbt.RITUAL_RECIPE, this.ritualRecipe.id.toString());
		}
		nbt.putInt(Constants.Nbt.TIMER, this.timer);
		nbt.putInt(Constants.Nbt.CLIENT_TIMER, this.clientTime);
		nbt.putLong(Constants.Nbt.AGE, this.age);
		if (user != null) {
			nbt.putUuid(Constants.Nbt.PLAYER_UUID, user);
		}
		nbt.putBoolean(Constants.Nbt.START, this.startGate);
		nbt.putBoolean(Constants.Nbt.SHOULD_RUN, this.shouldRun);
		nbt.putBoolean(Constants.Nbt.HAS_LEGEMETON, this.hasBotD);
		nbt.putBoolean(Constants.Nbt.HAS_EMERALD_TABLET, this.hasEmeraldTablet);
	}

	@Override
	public void markDirty() {
		super.markDirty();
		if (world != null && !world.isClient) {
			world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
			toUpdatePacket();
		}
	}

	static {
		PEDESTAL_POS_LIST = List.of(
				new BlockPos(3, 0, 0),
				new BlockPos(0, 0, 3),
				new BlockPos(-3, 0, 0),
				new BlockPos(0, 0, -3),

				new BlockPos(2, 0, 2),
				new BlockPos(2, 0, -2),
				new BlockPos(-2, 0, -2),
				new BlockPos(-2, 0, 2)
		);
	}


}
