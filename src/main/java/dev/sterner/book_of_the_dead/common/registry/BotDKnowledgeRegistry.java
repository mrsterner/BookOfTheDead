package dev.sterner.book_of_the_dead.common.registry;

import dev.sterner.book_of_the_dead.api.Knowledge;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface BotDKnowledgeRegistry {
	Map<Identifier, Knowledge> KNOWLEDGE = new LinkedHashMap<>();

	Knowledge ALCHEMY = register("alchemy", Constants.id("textures/gui/knowledge/alchemy.png"), List.of());

	Knowledge CALCINATION = register("calcination", Constants.id("textures/gui/knowledge/calcination.png"), List.of(ALCHEMY));
	Knowledge DISSOLUTION = register("dissolution", Constants.id("textures/gui/knowledge/dissolution.png"), List.of(ALCHEMY));
	Knowledge SEPARATION = register("separation", Constants.id("textures/gui/knowledge/separation.png"), List.of(DISSOLUTION, CALCINATION));
	Knowledge CONJUNCTION = register("conjunction", Constants.id("textures/gui/knowledge/conjunction.png"), List.of(SEPARATION));
	Knowledge FERMENTATION = register("fermentation", Constants.id("textures/gui/knowledge/fermentation.png"), List.of(SEPARATION));
	Knowledge DISTILLATION = register("distillation", Constants.id("textures/gui/knowledge/distillation.png"), List.of(CONJUNCTION, FERMENTATION));
	Knowledge COAGULATION = register("coagulation", Constants.id("textures/gui/knowledge/coagulation.png"), List.of(DISTILLATION));
	Knowledge PHILOSOPHER = register("philosopher", Constants.id("textures/gui/knowledge/philosopher.png"), List.of(COAGULATION));

	Knowledge BRIMSTONE = register("brimstone", Constants.id("textures/gui/knowledge/brimstone.png"), List.of(ALCHEMY));
	Knowledge ASH = register("ash", Constants.id("textures/gui/knowledge/ash.png"), List.of(BRIMSTONE));
	Knowledge MELT = register("melt", Constants.id("textures/gui/knowledge/melt.png"), List.of(ASH));
	Knowledge ROT = register("rot", Constants.id("textures/gui/knowledge/rot.png"), List.of(MELT));
	Knowledge CADUCEUS = register("caduceus", Constants.id("textures/gui/knowledge/caduceus.png"), List.of(ROT));

	Knowledge SOUL = register("soul", Constants.id("textures/gui/knowledge/soul.png"), List.of(ALCHEMY));
	Knowledge ESSENCE = register("essence", Constants.id("textures/gui/knowledge/essence.png"), List.of(SOUL));
	Knowledge FUSION = register("fusion", Constants.id("textures/gui/knowledge/fusion.png"), List.of(ESSENCE));
	Knowledge MULTIPLICATION = register("multiplication", Constants.id("textures/gui/knowledge/multiplication.png"), List.of(FUSION));
	Knowledge LIFE = register("life", Constants.id("textures/gui/knowledge/life.png"), List.of(MULTIPLICATION));

	Knowledge VOID = register("void", Constants.id("textures/gui/knowledge/void.png"), List.of(ALCHEMY));
	Knowledge AMALGAM = register("amalgam", Constants.id("textures/gui/knowledge/amalgam.png"), List.of(ALCHEMY));
	Knowledge DISPOSITION = register("disposition", Constants.id("textures/gui/knowledge/disposition.png"), List.of(VOID, AMALGAM));
	Knowledge BALANCE = register("balance", Constants.id("textures/gui/knowledge/balance.png"), List.of(DISPOSITION));
	Knowledge PROJECTION = register("projection", Constants.id("textures/gui/knowledge/projection.png"), List.of(BALANCE));

	static <T extends Knowledge> T register(String id, Identifier icon, List<Knowledge> children) {
		Knowledge knowledge = new Knowledge(id, icon, children);
		KNOWLEDGE.put(Constants.id(id), knowledge);
		return (T) knowledge;
	}

	static void init() {
		KNOWLEDGE.forEach((id, knowledge) -> Registry.register(BotDRegistries.KNOWLEDGE, id, knowledge));
	}
}
