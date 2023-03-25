package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.common.rituals.BasicNecrotableRitual;
import dev.sterner.book_of_the_dead.common.rituals.EntanglementNecrotableRitual;
import dev.sterner.book_of_the_dead.common.rituals.KakuzuNecrotableRitual;
import dev.sterner.book_of_the_dead.common.rituals.LichdomNecrotableRitual;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public interface BotDRituals {
	Map<BasicNecrotableRitual, Identifier> NECROTABLE_RITUAL = new LinkedHashMap<>();

	BasicNecrotableRitual BASIC = register("basic", new BasicNecrotableRitual(new Identifier("basic")));
	BasicNecrotableRitual KAKUZU = register("kakuzu", new KakuzuNecrotableRitual(new Identifier("kakuzu")));
	BasicNecrotableRitual LICHDOM = register("lichdom", new LichdomNecrotableRitual(new Identifier("lichdom")));
	BasicNecrotableRitual ENTANGLEMENT = register("entanglement", new EntanglementNecrotableRitual(new Identifier("entanglement")));

	static <T extends BasicNecrotableRitual> T register(String name, T necroRitual) {
		NECROTABLE_RITUAL.put(necroRitual, Constants.id(name));
		return necroRitual;
	}

	static void init(){
		NECROTABLE_RITUAL.keySet().forEach(ritual -> Registry.register(BotDRegistries.NECROTABLE_RITUALS, NECROTABLE_RITUAL.get(ritual), ritual));
	}
}
