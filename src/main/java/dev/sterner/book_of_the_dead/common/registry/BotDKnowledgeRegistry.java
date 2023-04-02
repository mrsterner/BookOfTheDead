package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.client.screen.book.Knowledge;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public interface BotDKnowledgeRegistry {
	Map<Identifier, Knowledge> KNOWLEDGE = new LinkedHashMap<>();

	Knowledge ALCHEMY = register("alchemy", Constants.id("textures/gui/alchemy/alchemy.png"));

	Knowledge CALCINATION = register("calcination", Constants.id("textures/gui/alchemy/calcination.png"));
	Knowledge DISSOLUTION = register("dissolution", Constants.id("textures/gui/alchemy/dissolution.png"));
	Knowledge SEPARATION = register("separation", Constants.id("textures/gui/alchemy/separation.png"));
	Knowledge CONJUNCTION = register("conjunction", Constants.id("textures/gui/alchemy/conjunction.png"));
	Knowledge FERMENTATION = register("fermentation", Constants.id("textures/gui/alchemy/fermentation.png"));
	Knowledge DISTILLATION = register("distillation", Constants.id("textures/gui/alchemy/distillation.png"));
	Knowledge COAGULATION = register("coagulation", Constants.id("textures/gui/alchemy/coagulation.png"));
	Knowledge PHILOSOPHER = register("philosopher", Constants.id("textures/gui/alchemy/philosopher.png"));

	Knowledge BRIMSTONE = register("brimstone", Constants.id("textures/gui/alchemy/brimstone.png"));
	Knowledge ASH = register("ash", Constants.id("textures/gui/alchemy/ash.png"));
	Knowledge MELT = register("melt", Constants.id("textures/gui/alchemy/melt.png"));
	Knowledge ROT = register("rot", Constants.id("textures/gui/alchemy/rot.png"));
	Knowledge CADUCEUS = register("caduceus", Constants.id("textures/gui/alchemy/caduceus.png"));

	Knowledge SOUL = register("soul", Constants.id("textures/gui/alchemy/soul.png"));
	Knowledge ESSENCE = register("essence", Constants.id("textures/gui/alchemy/essence.png"));
	Knowledge FUSION = register("fusion", Constants.id("textures/gui/alchemy/fusion.png"));
	Knowledge MULTIPLICATION = register("multiplication", Constants.id("textures/gui/alchemy/multiplication.png"));
	Knowledge LIFE = register("life", Constants.id("textures/gui/alchemy/life.png"));

	Knowledge VOID = register("void", Constants.id("textures/gui/alchemy/void.png"));
	Knowledge AMALGAM = register("amalgam", Constants.id("textures/gui/alchemy/amalgam.png"));
	Knowledge DISPOSITION = register("disposition", Constants.id("textures/gui/alchemy/disposition.png"));
	Knowledge BALANCE = register("balance", Constants.id("textures/gui/alchemy/balance.png"));
	Knowledge PROJECTION = register("projection", Constants.id("textures/gui/alchemy/projection.png"));

	static <T extends Knowledge> T register(String id, Identifier icon) {
		Knowledge knowledge = new Knowledge(id, icon);
		KNOWLEDGE.put(Constants.id(id), knowledge);
		return (T) knowledge;
	}

	static void init() {
		KNOWLEDGE.forEach((id, knowledge) -> Registry.register(BotDRegistries.KNOWLEDGE, id, knowledge));
	}
}
