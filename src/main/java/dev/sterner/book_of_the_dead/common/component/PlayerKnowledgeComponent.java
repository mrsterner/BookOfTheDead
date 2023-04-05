package dev.sterner.book_of_the_dead.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.sterner.book_of_the_dead.api.KnowledgeData;
import dev.sterner.book_of_the_dead.api.Knowledge;
import dev.sterner.book_of_the_dead.common.registry.BotDRegistries;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerKnowledgeComponent implements AutoSyncedComponent {
	private final PlayerEntity player;

	private boolean isAlchemist = false;
	private final Set<KnowledgeData> knowledgeData = new HashSet<>();

	public PlayerKnowledgeComponent(PlayerEntity player) {
		this.player = player;
	}

	public Set<KnowledgeData> getKnowledgeData() {
		return knowledgeData;
	}

	public void increaseKnowledgePoints(Knowledge knowledge, int amount) {
		for (KnowledgeData kd : knowledgeData) {
			if (kd.knowledge().equals(knowledge)) {
				int currentPoints = kd.points();
				int newPoints = currentPoints + amount;
				setKnowledgePoint(kd, newPoints);
				break; // stop iterating after finding a match
			}
		}
	}

	private void setKnowledgePoint(KnowledgeData kd, int newPoints) {
		if (knowledgeData.contains(kd)) {
			knowledgeData.remove(kd);
			knowledgeData.add(new KnowledgeData(kd.knowledge(), newPoints));
		}
		BotDComponents.KNOWLEDGE_COMPONENT.sync(player);
	}

	public void setKnowledgePoint(Knowledge knowledge, int points) {
		for (KnowledgeData kd : knowledgeData) {
			if (kd.knowledge().equals(knowledge)) {
				knowledgeData.remove(kd);
				knowledgeData.add(new KnowledgeData(knowledge, points));
				break;
			}
		}
		BotDComponents.KNOWLEDGE_COMPONENT.sync(player);
	}

	public void addKnowledge(Knowledge knowledge) {
		boolean canAddKnowledge = true;
		List<Knowledge> k = getKnowledgeData().stream().map(KnowledgeData::knowledge).toList();
		for (Knowledge child : knowledge.children) {
			if (!k.contains(child)) {
				canAddKnowledge = false;
				break;
			}
		}
		if (canAddKnowledge) {
			getKnowledgeData().add(new KnowledgeData(knowledge, 0));
		}
		BotDComponents.KNOWLEDGE_COMPONENT.sync(this.player);
	}

	@Override
	public boolean shouldSyncWith(ServerPlayerEntity player) {
		return player == this.player; // only sync with the provider itself
	}

	@Override
	public void readFromNbt(@NotNull NbtCompound nbt) {
		getKnowledgeData().clear();

		NbtList nbtList = nbt.getList(Constants.Nbt.KNOWLEDGE_DATA, NbtCompound.COMPOUND_TYPE);
		List<KnowledgeData> knowledgeDataList = new ArrayList<>();

		for (int i = 0; i < nbtList.size(); ++i) {
			NbtCompound nbtCompound = nbtList.getCompound(i);
			Identifier id = Constants.id(nbtCompound.getString(Constants.Nbt.KNOWLEDGE));
			if (BotDRegistries.KNOWLEDGE.getIds().contains(id)) {
				Knowledge knowledge = BotDRegistries.KNOWLEDGE.get(id);
				System.out.println("DataK: " + knowledge.identifier);
				int points = nbtCompound.getInt(Constants.Nbt.POINTS);
				knowledgeDataList.add(new KnowledgeData(knowledge, points));
			}
		}

		getKnowledgeData().addAll(knowledgeDataList);
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound nbt) {
		NbtList knowledgeList = new NbtList();
		for (KnowledgeData knowledgeData : getKnowledgeData()) {
			NbtCompound nbtCompound = new NbtCompound();
			nbtCompound.putString(Constants.Nbt.KNOWLEDGE, knowledgeData.knowledge().identifier);
			nbtCompound.putInt(Constants.Nbt.POINTS, knowledgeData.points());
			knowledgeList.add(nbtCompound);
		}
		nbt.put(Constants.Nbt.KNOWLEDGE_DATA, knowledgeList);
	}

	public boolean isAlchemist() {
		return isAlchemist;
	}

	public void setAlchemist(boolean alchemist) {
		isAlchemist = alchemist;
		BotDComponents.KNOWLEDGE_COMPONENT.sync(player);
	}
}
