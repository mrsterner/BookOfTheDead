package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.api.NecrotableRitual;
import dev.sterner.book_of_the_dead.common.ritual.*;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public interface BotDRituals {
	Map<NecrotableRitual, Identifier> NECROTABLE_RITUAL = new LinkedHashMap<>();

	NecrotableRitual CREATE_ITEM = register("create_item", new CreateAndConsumeItemRitual(new Identifier("create_item")));
	NecrotableRitual STATUS_EFFECT = register("status_effect", new StatusEffectRitual(new Identifier("status_effect")));

	static <T extends NecrotableRitual> T register(String name, T necroRitual) {
		NECROTABLE_RITUAL.put(necroRitual, Constants.id(name));
		return necroRitual;
	}

	static void init(){
		NECROTABLE_RITUAL.keySet().forEach(ritual -> Registry.register(BotDRegistries.NECROTABLE_RITUALS, NECROTABLE_RITUAL.get(ritual), ritual));
	}
}
