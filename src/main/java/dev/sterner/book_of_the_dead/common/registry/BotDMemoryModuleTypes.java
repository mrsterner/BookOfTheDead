package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.Optional;

public interface BotDMemoryModuleTypes {
	MemoryModuleType<PlayerEntity> OWNER_PLAYER = register("owner_player");
	MemoryModuleType<Boolean> SHOULD_FOLLOW_OWNER = register("should_follow_owner");

	static <U> MemoryModuleType<U> register(String id) {
		return Registry.register(Registries.MEMORY_MODULE_TYPE, Constants.id(id), new MemoryModuleType<>(Optional.empty()));
	}

	static void init() {

	}
}
