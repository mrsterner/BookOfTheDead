package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.api.NecrotableRitual;
import dev.sterner.book_of_the_dead.common.ritual.SummonUndeadRitual;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class BotDRituals {
	private static final Map<NecrotableRitual, Identifier> NECROTABLE_RITUAL = new LinkedHashMap<>();

	public static final NecrotableRitual SUMMON_ZOMBIE = register("summon_zombie", new SummonUndeadRitual(new Identifier("summon_zombie"), new Identifier(""), new Identifier(""), 20 * 4));

	private static <T extends NecrotableRitual> T register(String name, T necroRitual) {
		NECROTABLE_RITUAL.put(necroRitual, Constants.id(name));
		return necroRitual;
	}

	public static void init(){
		NECROTABLE_RITUAL.keySet().forEach(ritual -> Registry.register(BotDRegistries.NECROTABLE_RITUALS, NECROTABLE_RITUAL.get(ritual), ritual));
	}
}
