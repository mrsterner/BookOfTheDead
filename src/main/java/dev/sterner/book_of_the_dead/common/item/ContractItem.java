package dev.sterner.book_of_the_dead.common.item;

import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.util.Constants;
import dev.sterner.book_of_the_dead.common.util.TextUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ContractItem extends Item {
	public ContractItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (!world.isClient() && hand == Hand.MAIN_HAND) {
			ItemStack itemStack = user.getMainHandStack();
			NbtCompound nbt = new NbtCompound();
			nbt.putString(Constants.Nbt.NAME, user.getEntityName());
			nbt.putUuid(Constants.Nbt.UUID, user.getUuid());
			itemStack.getOrCreateNbt().put(Constants.Nbt.CONTRACT, nbt);
		}
		return super.use(world, user, hand);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (stack.hasNbt() && stack.getOrCreateNbt().contains(Constants.Nbt.CONTRACT)) {
			NbtCompound nbt = stack.getSubNbt(Constants.Nbt.CONTRACT);
			if (nbt != null) {
				String name = nbt.getString(Constants.Nbt.NAME);
				String formattedName = TextUtils.capitalizeString(name);
				tooltip.add(Text.literal(formattedName).setStyle(Style.EMPTY.withColor(0xAC0014)));
			}

		}
		super.appendTooltip(stack, world, tooltip, context);
	}

	@Nullable
	public static LivingEntity getLivingFromContractNbt(World world, ItemStack contract) {
		if (contract.isOf(BotDObjects.CONTRACT) && contract.hasNbt() && contract.getOrCreateNbt().contains(Constants.Nbt.CONTRACT)) {
			NbtCompound nbt = contract.getSubNbt(Constants.Nbt.CONTRACT);
			if (nbt != null) {
				int id = nbt.getInt(Constants.Nbt.ID);
				Entity entity = world.getEntityById(id);
				if (entity instanceof LivingEntity living) {
					return living;
				}
			}
		}
		return null;
	}

	public static int getIdFromContractNbt(ItemStack contract) {
		if (contract.isOf(BotDObjects.CONTRACT) && contract.hasNbt() && contract.getOrCreateNbt().contains(Constants.Nbt.CONTRACT)) {
			NbtCompound nbt = contract.getSubNbt(Constants.Nbt.CONTRACT);
			if (nbt != null) {
				return nbt.getInt(Constants.Nbt.ID);
			}
		}
		return 0;
	}
}
