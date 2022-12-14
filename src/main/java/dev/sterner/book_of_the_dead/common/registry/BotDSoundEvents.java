package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class BotDSoundEvents {
	private static final Map<SoundEvent, Identifier> SOUND_EVENTS = new LinkedHashMap<>();

	public static final SoundEvent MISC_ITEM_BEAM = register("misc.item.item_beam");

	private static SoundEvent register(String name) {
		Identifier id = Constants.id(name);
		SoundEvent soundEvent = new SoundEvent(id);
		SOUND_EVENTS.put(soundEvent, id);
		return soundEvent;
	}

	public static void init() {
		SOUND_EVENTS.keySet().forEach(soundEvent -> Registry.register(Registry.SOUND_EVENT, SOUND_EVENTS.get(soundEvent), soundEvent));
	}
}
