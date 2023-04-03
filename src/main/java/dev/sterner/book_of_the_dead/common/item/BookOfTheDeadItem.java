package dev.sterner.book_of_the_dead.common.item;

import dev.sterner.book_of_the_dead.client.screen.BookOfTheDeadScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BookOfTheDeadItem extends Item {
	private final Identifier id;

	public BookOfTheDeadItem(Identifier id, Settings settings) {
		super(settings);
		this.id = id;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		if (world.isClient) {
			BookOfTheDeadScreen.openScreen(player);
			player.swingHand(hand);
			return TypedActionResult.success(player.getStackInHand(hand));
		}
		return TypedActionResult.success(player.getStackInHand(hand));
	}
}
