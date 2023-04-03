package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.client.screen.book.Knowledge;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public interface BotDKnowledgeRegistry {
	Map<Identifier, Knowledge> KNOWLEDGE = new LinkedHashMap<>();

	Knowledge ALCHEMY = register("alchemy", Constants.id("textures/gui/knowledge/alchemy.png"));

	Knowledge CALCINATION = register("calcination", Constants.id("textures/gui/knowledge/calcination.png"));
	Knowledge DISSOLUTION = register("dissolution", Constants.id("textures/gui/knowledge/dissolution.png"));
	Knowledge SEPARATION = register("separation", Constants.id("textures/gui/knowledge/separation.png"));
	Knowledge CONJUNCTION = register("conjunction", Constants.id("textures/gui/knowledge/conjunction.png"));
	Knowledge FERMENTATION = register("fermentation", Constants.id("textures/gui/knowledge/fermentation.png"));
	Knowledge DISTILLATION = register("distillation", Constants.id("textures/gui/knowledge/distillation.png"));
	Knowledge COAGULATION = register("coagulation", Constants.id("textures/gui/knowledge/coagulation.png"));
	Knowledge PHILOSOPHER = register("philosopher", Constants.id("textures/gui/knowledge/philosopher.png"));

	Knowledge BRIMSTONE = register("brimstone", Constants.id("textures/gui/knowledge/brimstone.png"));
	Knowledge ASH = register("ash", Constants.id("textures/gui/knowledge/ash.png"));
	Knowledge MELT = register("melt", Constants.id("textures/gui/knowledge/melt.png"));
	Knowledge ROT = register("rot", Constants.id("textures/gui/knowledge/rot.png"));
	Knowledge CADUCEUS = register("caduceus", Constants.id("textures/gui/knowledge/caduceus.png"));

	Knowledge SOUL = register("soul", Constants.id("textures/gui/knowledge/soul.png"));
	Knowledge ESSENCE = register("essence", Constants.id("textures/gui/knowledge/essence.png"));
	Knowledge FUSION = register("fusion", Constants.id("textures/gui/knowledge/fusion.png"));
	Knowledge MULTIPLICATION = register("multiplication", Constants.id("textures/gui/knowledge/multiplication.png"));
	Knowledge LIFE = register("life", Constants.id("textures/gui/knowledge/life.png"));

	Knowledge VOID = register("void", Constants.id("textures/gui/knowledge/void.png"));
	Knowledge AMALGAM = register("amalgam", Constants.id("textures/gui/knowledge/amalgam.png"));
	Knowledge DISPOSITION = register("disposition", Constants.id("textures/gui/knowledge/disposition.png"));
	Knowledge BALANCE = register("balance", Constants.id("textures/gui/knowledge/balance.png"));
	Knowledge PROJECTION = register("projection", Constants.id("textures/gui/knowledge/projection.png"));

	static <T extends Knowledge> T register(String id, Identifier icon) {
		Knowledge knowledge = new Knowledge(id, icon);
		KNOWLEDGE.put(Constants.id(id), knowledge);
		return (T) knowledge;
	}

	static void init() {
		KNOWLEDGE.forEach((id, knowledge) -> Registry.register(BotDRegistries.KNOWLEDGE, id, knowledge));
	}
}
