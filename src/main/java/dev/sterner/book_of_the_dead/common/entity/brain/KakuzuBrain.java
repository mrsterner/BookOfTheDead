package dev.sterner.book_of_the_dead.common.entity.brain;

import dev.sterner.book_of_the_dead.common.entity.KakuzuEntity;
import dev.sterner.book_of_the_dead.common.entity.brain.sensor.KakuzuSpecificSensor;
import dev.sterner.book_of_the_dead.common.entity.brain.task.EnterPlayerOnDemandTask;
import dev.sterner.book_of_the_dead.common.entity.brain.task.StrollFlyTask;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.VisibleLivingEntitiesCache;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AllayBrain;
import net.minecraft.entity.passive.AllayEntity;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;

import java.util.List;
import java.util.Optional;

public class KakuzuBrain {
	public KakuzuBrain() {
	}

	public static List<ExtendedSensor<KakuzuEntity>> getSensors() {
		return ObjectArrayList.of(
				new NearbyPlayersSensor<>(),
				new NearbyLivingEntitySensor<>(),
				new HurtBySensor<>(),
				new KakuzuSpecificSensor()
		);
	}

	public static BrainActivityGroup<KakuzuEntity> getCoreTasks() {
		return BrainActivityGroup.coreTasks(
				new EnterPlayerOnDemandTask(),
				new WalkTask(2.5F),
				new LookAroundTask(45, 90),
				new WanderAroundTask()
		);
	}

	public static BrainActivityGroup<KakuzuEntity> getIdleTasks(KakuzuEntity kakuzu) {
		return BrainActivityGroup.idleTasks(
				new FirstApplicableBehaviour<>(
						new SetPlayerLookTarget<>()),
				new OneRandomBehaviour<>(
						new StrollFlyTask<>().dontAvoidWater()
				)
		);
	}

	public static BrainActivityGroup<KakuzuEntity> getFightTasks(KakuzuEntity kakuzu) {
		return BrainActivityGroup.fightTasks(
				new InvalidateAttackTarget<>()
		);
	}

	public static Optional<? extends LivingEntity> getAttackTarget(KakuzuEntity kakuzu) {
		Brain<?> brain = kakuzu.getBrain();
		Optional<LivingEntity> optional = LookTargetUtil.getEntity(kakuzu, MemoryModuleType.ANGRY_AT);
		if (optional.isPresent() && Sensor.testAttackableTargetPredicateIgnoreVisibility(kakuzu, optional.get())) {
			return optional;
		}
		if (brain.hasMemoryModule(MemoryModuleType.VISIBLE_MOBS)) {
			Optional<VisibleLivingEntitiesCache> visibleLivingEntitiesCache = kakuzu.getBrain().getOptionalMemory(MemoryModuleType.VISIBLE_MOBS);
			if (visibleLivingEntitiesCache.isPresent()) {
				return visibleLivingEntitiesCache.get().findClosest(kakuzu.TARGET_FILTER);
			}
		}
		return Optional.empty();
	}
}
