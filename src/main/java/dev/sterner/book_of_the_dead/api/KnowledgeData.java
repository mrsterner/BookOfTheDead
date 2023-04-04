package dev.sterner.book_of_the_dead.api;

import java.util.Objects;

public record KnowledgeData(Knowledge knowledge, int points) {
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		KnowledgeData that = (KnowledgeData) obj;
		return Objects.equals(knowledge, that.knowledge) &&
			points == that.points;
	}

	@Override
	public int hashCode() {
		return Objects.hash(knowledge, points);
	}
}
