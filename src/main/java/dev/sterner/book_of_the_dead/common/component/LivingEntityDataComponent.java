package dev.sterner.book_of_the_dead.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.sterner.book_of_the_dead.common.component.player.PlayerDataComponent;
import dev.sterner.book_of_the_dead.common.util.BotDUtils;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class LivingEntityDataComponent implements AutoSyncedComponent {
	public final float[] ENTANGLE_STRENGTH = {0.05f, 0.10f, 0.15f, 0.20f, 0.25f, 0.30f, 0.35f, 0.40f, 0.45f, 0.50f, 0.55f, 0.60f, 0.65f, 0.70f, 0.75f, 0.80f};
	public LivingEntity livingEntity;
	private float morphine$accumulatedDamage = 0;
	private float adrenaline$bonusDamage = 0;
	private int entangledEntityId = 0;
	private Vec3d ritualPos;

	public LivingEntityDataComponent(LivingEntity livingEntity) {
		this.livingEntity = livingEntity;
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		setMorphine$accumulatedDamage(nbt.getFloat(Constants.Nbt.MORPHINE));
		setAdrenaline$bonusDamage(nbt.getFloat(Constants.Nbt.ADRENALINE));
		setEntangledEntityId(nbt.getInt(Constants.Nbt.ENTANGLED));
		if (nbt.contains(Constants.Nbt.RITUAL_POS)) {
			setRitualPos(BotDUtils.toVec3d(nbt.getCompound(Constants.Nbt.RITUAL_POS)));
		}
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putFloat(Constants.Nbt.MORPHINE, getMorphine$accumulatedDamage());
		nbt.putFloat(Constants.Nbt.ADRENALINE, getAdrenaline$bonusDamage());
		nbt.putInt(Constants.Nbt.ENTANGLED, getEntangledEntityId());
		if (getRitualPos() != null) {
			nbt.put(Constants.Nbt.RITUAL_POS, BotDUtils.fromVec3d(getRitualPos()));
		}
	}

	public float getEntangleStrength(LivingEntity source, LivingEntity target, boolean isSource) {
		int i = 6;
		List<TagKey<EntityType<?>>> list = List.of(Constants.Tags.SOUL_WEAK, Constants.Tags.SOUL_REGULAR, Constants.Tags.SOUL_STRONG);

		RegistryKey<World> sourceDim = source.getWorld().getRegistryKey();
		RegistryKey<World> targetDim = target.getWorld().getRegistryKey();

		if (sourceDim != null && targetDim != null && !sourceDim.equals(targetDim)) {
			i -= 2;
		}

		if (source instanceof PlayerEntity player && target instanceof TameableEntity tameable) {
			i = entangleTameablePlayer(i, player, tameable);
		} else if (target instanceof PlayerEntity player && source instanceof TameableEntity tameable) {
			i = entangleTameablePlayer(i, player, tameable);
		}

		if (source instanceof PlayerEntity player && target instanceof HostileEntity hostileEntity) {
			i = entangleHostilePlayer(i, player, hostileEntity);
		} else if (target instanceof PlayerEntity player && source instanceof HostileEntity hostileEntity) {
			i = entangleHostilePlayer(i, player, hostileEntity);
		}

		if (source instanceof PlayerEntity playerEntity && target instanceof VillagerEntity villagerEntity) {
			i = entangleVillagerPlayer(i, playerEntity, villagerEntity);
		} else if (target instanceof PlayerEntity playerEntity && source instanceof VillagerEntity villagerEntity) {
			i = entangleVillagerPlayer(i, playerEntity, villagerEntity);
		}

		for (TagKey<EntityType<?>> tag : list) {
			if (tag.equals(Constants.Tags.SOUL_WEAK)) {
				if (source.getType().isIn(tag) || target.getType().isIn(tag)) {
					i++;
				}
			} else if (tag.equals(Constants.Tags.SOUL_REGULAR)) {
				if (source.getType().isIn(tag) || target.getType().isIn(tag)) {
					i += 2;
				}
			} else if (tag.equals(Constants.Tags.SOUL_STRONG)) {
				if (source.getType().isIn(tag) || target.getType().isIn(tag)) {
					i += 3;
				}
			}
		}

		float strength = ENTANGLE_STRENGTH[MathHelper.clamp(i, 0, ENTANGLE_STRENGTH.length)];

		return isSource ? 1 - strength : strength;
	}

	public int entangleVillagerPlayer(int i, PlayerEntity player, VillagerEntity villagerEntity) {
		int r = villagerEntity.getReputation(player);
		if (r <= -100) {
			i -= 2;
		} else if (r > 50) {
			i += 2;
		}

		return i;
	}

	public int entangleHostilePlayer(int i, PlayerEntity player, HostileEntity hostileEntity) {
		LivingEntity livingEntity1 = hostileEntity.getTarget();
		PlayerDataComponent c = BotDComponents.PLAYER_COMPONENT.get(player);
		if (c.getLich() && hostileEntity.isUndead()) {
			i += 3;
		} else if (livingEntity1 != null && livingEntity1.getUuid() == player.getUuid()) {
			i -= 3;
		}

		return i;
	}

	public int entangleTameablePlayer(int i, PlayerEntity player, TameableEntity tameable) {
		UUID uuid = tameable.getOwnerUuid();
		if (uuid != null && uuid == player.getUuid()) {
			i += 3;
		}

		return i;
	}

	public int getEntangledEntityId() {
		return entangledEntityId;
	}

	public void setEntangledEntityId(int entangledEntityId) {
		this.entangledEntityId = entangledEntityId;
		syncAbility();
	}

	public float getMorphine$accumulatedDamage() {
		return morphine$accumulatedDamage;
	}

	public void setMorphine$accumulatedDamage(float morphine$accumulatedDamage) {
		this.morphine$accumulatedDamage = morphine$accumulatedDamage;
		syncAbility();
	}

	public void increaseMorphine$accumulatedDamage(float damage) {
		this.setMorphine$accumulatedDamage(getMorphine$accumulatedDamage() + damage);
	}

	public float getAdrenaline$bonusDamage() {
		return adrenaline$bonusDamage;
	}

	public void setAdrenaline$bonusDamage(float adrenaline$bonusDamage) {
		this.adrenaline$bonusDamage = adrenaline$bonusDamage;
		syncAbility();
	}

	public void increaseAdrenaline$bonusDamage(float damage) {
		setAdrenaline$bonusDamage(getAdrenaline$bonusDamage() + damage);
	}

	public Vec3d getRitualPos() {
		return ritualPos;
	}

	public void setRitualPos(Vec3d ritualPos) {
		this.ritualPos = ritualPos;
		syncAbility();
	}

	private void syncAbility() {
		BotDComponents.LIVING_COMPONENT.sync(this.livingEntity);
	}
}
