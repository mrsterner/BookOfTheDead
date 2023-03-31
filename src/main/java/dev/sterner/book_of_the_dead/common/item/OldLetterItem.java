package dev.sterner.book_of_the_dead.common.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OldLetterItem extends Item {
	public OldLetterItem(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.translatable("tooltip.book_of_the_dead.from_archive"));
		tooltip.add(Text.translatable("tooltip.book_of_the_dead.old_friend").formatted(Formatting.ITALIC));
		super.appendTooltip(stack, world, tooltip, context);
	}
}
