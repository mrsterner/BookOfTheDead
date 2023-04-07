package dev.sterner.book_of_the_dead.client.registry;

import dev.sterner.book_of_the_dead.client.renderer.entity.*;
import dev.sterner.book_of_the_dead.common.registry.BotDEntityTypes;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public interface BotDEntityRenderers {

	static void init() {
		EntityRendererRegistry.register(BotDEntityTypes.OLD_MAN_ENTITY, OldManEntityRenderer::new);
		EntityRendererRegistry.register(BotDEntityTypes.BLOOD_SLIME_ENTITY, BloodSlimeEntityRenderer::new);
		EntityRendererRegistry.register(BotDEntityTypes.PLAYER_CORPSE_ENTITY, PlayerCorpseEntityRenderer::new);
		EntityRendererRegistry.register(BotDEntityTypes.KAKUZU_ENTITY, KakuzuEntityRenderer::new);
		EntityRendererRegistry.register(BotDEntityTypes.FLOATING_ITEM_ENTITY, FloatingItemEntityRenderer::new);
		EntityRendererRegistry.register(BotDEntityTypes.BLOOD_PARTICLE_ENTITY, BloodParticleEntityRenderer::new);
	}
}
