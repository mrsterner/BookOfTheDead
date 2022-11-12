package dev.sterner.legemeton.common.asm;

import com.chocohead.mm.api.ClassTinkerers;
import org.quiltmc.loader.api.MappingResolver;
import org.quiltmc.loader.api.QuiltLoader;

public class LegemetonEarlyRiser implements Runnable {


	@Override
	public void run() {
		final MappingResolver mappings = QuiltLoader.getMappingResolver();
		final String enchantmentTarget = mappings.mapClassName("intermediary", "net.minecraft.class_1886");
		//enchantmentTarget
		ClassTinkerers.enumBuilder(enchantmentTarget, new Class[0])
				.addEnumSubclass("AXE", "dev.sterner.legemeton.common.enchantment.ButcheringEnchantmentTarget")
				.build();
	}
}
