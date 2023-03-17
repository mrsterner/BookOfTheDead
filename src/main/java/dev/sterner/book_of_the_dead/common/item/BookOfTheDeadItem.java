package dev.sterner.book_of_the_dead.common.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.List;

public class BookOfTheDeadItem extends Item {
	private final Identifier id;
	public BookOfTheDeadItem(Identifier id, Settings settings) {
		super(settings);
		this.id = id;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		//Book book = getBook();
		if (player instanceof ServerPlayerEntity && !player.isSneaking()) {
			//PatchouliAPI.get().openBookGUI((ServerPlayerEntity) player, book.id);
			//SoundEvent sfx = PatchouliSounds.getSound(book.openSound, PatchouliSounds.BOOK_OPEN);
			//player.playSound(sfx, 1.0F, (float) (0.7D + Math.random() * 0.4D));
		}
		return TypedActionResult.success(player.getStackInHand(hand));
	}

	//public Book getBook() {
	//	return BookRegistry.INSTANCE.books.get(id);
	//}

	@ClientOnly
	public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
		super.appendTooltip(stack, worldIn, tooltip, flagIn);
		//Book book = getBook();
		//if (book != null && book.getContents() != null) {
		//	tooltip.add(book.getSubtitle().formatted(Formatting.GRAY));
		//}

	}
}
