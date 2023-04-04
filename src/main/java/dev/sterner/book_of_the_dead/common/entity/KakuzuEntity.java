package dev.sterner.book_of_the_dead.common.entity;

import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.PlayerDataComponent;
import dev.sterner.book_of_the_dead.common.entity.brain.KakuzuBrain;
import dev.sterner.book_of_the_dead.common.registry.BotDMemoryModuleTypes;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;


public class KakuzuEntity extends PathAwareEntity implements SmartBrainOwner<KakuzuEntity> {
	private static final byte IDLE = 0;
	private static final byte ATTACK = 1;
	private static final byte ENTER_OWNER = 2;

	public static final TrackedData<Integer> VARIANT = DataTracker.registerData(KakuzuEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public static final TrackedData<Optional<UUID>> OWNER = DataTracker.registerData(KakuzuEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
	public static final TrackedData<Byte> MODE = DataTracker.registerData(KakuzuEntity.class, TrackedDataHandlerRegistry.BYTE);

	public final Predicate<LivingEntity> TARGET_FILTER = entity -> entity instanceof MobEntity mob && mob.getTarget() != null && mob.getTarget().getUuid().equals(this.getOwner());

	public KakuzuEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
		this.moveControl = new FlightMoveControl(this, 20, true);
		this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
	}

	public void setVariant(int variant) {
		this.getDataTracker().set(VARIANT, variant);
	}

	public int getVariant() {
		return this.getDataTracker().get(VARIANT);
	}

	public void setOwner(UUID uuid) {
		this.getDataTracker().set(OWNER, Optional.ofNullable(uuid));
	}

	@Nullable
	public UUID getOwner() {
		return this.getDataTracker().get(OWNER).orElse(null);
	}

	public static DefaultAttributeContainer.Builder createAttributes() {
		return MobEntity.createAttributes()
			.add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
			.add(EntityAttributes.GENERIC_FLYING_SPEED, 0.1F)
			.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1F)
			.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0)
			.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0);
	}

	@Override
	protected EntityNavigation createNavigation(World world) {
		BirdNavigation birdNavigation = new BirdNavigation(this, world);
		birdNavigation.setCanPathThroughDoors(false);
		birdNavigation.setCanSwim(true);
		birdNavigation.setCanEnterOpenDoors(true);
		return birdNavigation;
	}

	@Override
	protected ActionResult interactMob(PlayerEntity player, Hand hand) {
		if (this.getOwner() != null && this.getOwner().equals(player.getUuid())) {
			PlayerDataComponent component = BotDComponents.PLAYER_COMPONENT.get(player);
			if (component.getKakuzu() < 3 && component.getDispatchedKakuzuMinions() > 0) {
				component.increaseKakuzuBuffLevel();
				component.decreaseDispatchedMinionBuffLevel();
				this.discard();
			}
		}
		return super.interactMob(player, hand);
	}

	@Override
	public void travel(Vec3d movementInput) {
		if (this.isLogicalSideForUpdatingMovement()) {
			if (this.isTouchingWater()) {
				this.updateVelocity(0.02F, movementInput);
				this.move(MovementType.SELF, this.getVelocity());
				this.setVelocity(this.getVelocity().multiply(0.8F));
			} else if (this.isInLava()) {
				this.updateVelocity(0.02F, movementInput);
				this.move(MovementType.SELF, this.getVelocity());
				this.setVelocity(this.getVelocity().multiply(0.5));
			} else {
				this.updateVelocity(this.getMovementSpeed(), movementInput);
				this.move(MovementType.SELF, this.getVelocity());
				this.setVelocity(this.getVelocity().multiply(0.91F));
			}
		}

		this.updateLimbs(false);
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		Entity entity = source.getAttacker();
		if (entity instanceof PlayerEntity playerEntity) {
			Optional<PlayerEntity> optional = this.getBrain().getOptionalMemory(BotDMemoryModuleTypes.OWNER_PLAYER);
			if (optional.isPresent() && playerEntity.equals(optional.get())) {
				return false;
			}
		}

		return super.damage(source, amount);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(VARIANT, 1);
		dataTracker.startTracking(OWNER, Optional.empty());
		dataTracker.startTracking(MODE, (byte) 0b0000_0000);
	}

	protected void setModeFlag(int index, boolean value) {
		byte b = this.dataTracker.get(MODE);
		if (value) {
			this.dataTracker.set(MODE, (byte) (b | 1 << index));
		} else {
			this.dataTracker.set(MODE, (byte) (b & ~(1 << index)));
		}
	}

	protected boolean getModeFlag(int index) {
		return (this.dataTracker.get(MODE) & 1 << index) != 0;
	}

	public boolean isIdleFlag() {
		return getModeFlag(IDLE);
	}

	public void setIdleFlag(boolean idle) {
		setModeFlag(IDLE, idle);
	}

	public boolean isAttackingFlag() {
		return getModeFlag(ATTACK);
	}

	public void setAttackingFlag(boolean attacking) {
		setModeFlag(ATTACK, attacking);
	}

	public boolean isEnterOwnerFlag() {
		return getModeFlag(ENTER_OWNER);
	}

	public void setEnterOwnerFlag(boolean enterOwner) {
		setModeFlag(ENTER_OWNER, enterOwner);
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		setVariant(nbt.getInt(Constants.Nbt.VARIANT));
		if (nbt.contains(Constants.Nbt.OWNER)) {
			setOwner(nbt.getUuid(Constants.Nbt.OWNER));
		}
		dataTracker.set(MODE, (nbt.getByte(Constants.Nbt.MODE)));
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putInt(Constants.Nbt.VARIANT, getVariant());
		if (getOwner() != null) {
			nbt.putUuid(Constants.Nbt.OWNER, getOwner());
		}
		nbt.putByte(Constants.Nbt.MODE, dataTracker.get(MODE));
	}

	@Override
	protected Brain.Profile<?> createBrainProfile() {
		return new SmartBrainProvider<>(this);
	}

	@Override
	protected void mobTick() {
		tickBrain(this);
	}

	@Override
	public List<ExtendedSensor<KakuzuEntity>> getSensors() {
		return KakuzuBrain.getSensors();
	}

	@Override
	public BrainActivityGroup<KakuzuEntity> getCoreTasks() {
		return KakuzuBrain.getCoreTasks();
	}

	@Override
	public BrainActivityGroup<KakuzuEntity> getIdleTasks() {
		return KakuzuBrain.getIdleTasks(this);
	}

	@Override
	public BrainActivityGroup<KakuzuEntity> getFightTasks() {
		return KakuzuBrain.getFightTasks(this);
	}

	@Override
	protected void initGoals() {
	}

	@Override
	protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
	}
}
