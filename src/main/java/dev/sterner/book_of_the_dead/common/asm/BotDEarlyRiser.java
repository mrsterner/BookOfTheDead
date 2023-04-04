package dev.sterner.book_of_the_dead.common.asm;

import com.chocohead.mm.api.ClassTinkerers;
import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import org.quiltmc.loader.api.MappingResolver;
import org.quiltmc.loader.api.QuiltLoader;

public class BotDEarlyRiser implements Runnable {

	@Override
	public void run() {
		MixinExtrasBootstrap.init();

		final MappingResolver mappings = QuiltLoader.getMappingResolver();
		final String enchantmentTarget = mappings.mapClassName("intermediary", "net.minecraft.class_1886");
		//enchantmentTarget
		ClassTinkerers.enumBuilder(enchantmentTarget, new Class[0])
			.addEnumSubclass("AXE", "dev.sterner.book_of_the_dead.common.enchantment.ButcheringEnchantmentTarget")
			.build();
	}
}
