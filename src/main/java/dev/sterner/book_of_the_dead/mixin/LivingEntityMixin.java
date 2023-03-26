package dev.sterner.book_of_the_dead.mixin;

import dev.sterner.book_of_the_dead.api.BotDApi;
import dev.sterner.book_of_the_dead.api.event.OnEntityDeathEvent;
import dev.sterner.book_of_the_dead.common.component.*;
import dev.sterner.book_of_the_dead.common.registry.BotDStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.quiltmc.qsl.entity.effect.api.QuiltLivingEntityStatusEffectExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(value = LivingEntity.class, priority = 1001)
public abstract class LivingEntityMixin extends Entity implements QuiltLivingEntityStatusEffectExtensions {

	@Unique
	public List<StatusEffect> statusEffectList = Registries.STATUS_EFFECT.stream().filter(s -> !s.getType().equals(StatusEffectType.HARMFUL)).toList();

	@Shadow
	public int deathTime;

	@Shadow
	protected abstract void initDataTracker();

	@Shadow
	public abstract Identifier getLootTable();

	@Shadow
	protected abstract LootContext.Builder getLootContextBuilder(boolean causedByPlayer, DamageSource source);

	@Shadow
	public abstract void remove(RemovalReason reason);

	@Unique
	private DamageSource damageSource;

	@Unique
	private boolean shouldDie = false;


	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "onDeath", at = @At("HEAD"))
	private void book_of_the_dead$PreOnDeath(DamageSource source, CallbackInfo ci) {
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		OnEntityDeathEvent.START.invoker().start(livingEntity, livingEntity.getBlockPos(), source);
	}

	@Inject(method = "onDeath", at = @At("TAIL"))
	private void book_of_the_dead$postOnDeath(DamageSource source, CallbackInfo ci) {
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		OnEntityDeathEvent.END.invoker().end(livingEntity, livingEntity.getBlockPos(), source);
	}

	@Inject(method = "tickMovement", at = @At("HEAD"), cancellable = true)
	private void book_of_the_dead$tickMovement(CallbackInfo callbackInfo) {
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		if (this.deathTime >= 20 && livingEntity instanceof MobEntity) {
			Box box = this.getBoundingBox();
			if (this.world.containsFluid(box.offset(0.0D, box.getYLength(), 0.0D))) {
				this.setPos(this.getX(), this.getY() + 0.05D, this.getZ());
			}
			callbackInfo.cancel();
		}
	}

	@Inject(method = "damage", at = @At("HEAD"))
	private void book_of_the_dead$damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		Optional<CorpseDataComponent> component = BotDComponents.CORPSE_COMPONENT.maybeGet(livingEntity);
		if (component.isPresent()) {
			if (component.get().isCorpse) {
				this.shouldDie = true;
			}
		}
	}

	@Inject(method = "updatePostDeath", at = @At("HEAD"), cancellable = true)
	protected void book_of_the_dead$updatePostDeath(CallbackInfo callbackInfo) {
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		Optional<CorpseDataComponent> component = BotDComponents.CORPSE_COMPONENT.maybeGet(livingEntity);
		if (component.isPresent()) {
			boolean isCorpse = component.get().isCorpse;
			if ((isCorpse || BotDApi.isButchering(livingEntity))) {
				if (livingEntity instanceof PlayerEntity) {
					component.get().isCorpse(true);
				}
				if (livingEntity instanceof MobEntity mob) {
					component.get().isCorpse(true);
					++livingEntity.deathTime;
					if (livingEntity.deathTime == 1) {
						if (livingEntity.isOnFire()) {
							livingEntity.extinguish();
						}
						if (livingEntity.getVehicle() != null) {
							livingEntity.stopRiding();
						}
					}
					if (livingEntity.deathTime >= 20) {
						Box corpseBox = new Box(livingEntity.getX() - (livingEntity.getWidth() / 2.0F), livingEntity.getY() - (livingEntity.getWidth() / 2.0F), livingEntity.getZ() - (livingEntity.getWidth() / 2.0F), livingEntity.getX() + (livingEntity.getWidth() / 2F), livingEntity.getY() + (livingEntity.getWidth() / 2F), livingEntity.getZ() + (livingEntity.getWidth() / 2F));
						livingEntity.setBoundingBox(corpseBox.offset(livingEntity.getRotationVector(0F, livingEntity.bodyYaw).rotateY(-30.0F)));
					}
					if (livingEntity.deathTime < 20 * 60 * 5 && !this.shouldDie) {
						callbackInfo.cancel();
					} else {
						MinecraftServer server = this.world.getServer();
						if (server != null && this.damageSource != null) {
							Identifier identifier = this.getLootTable();
							LootTable lootTable = this.world.getServer().getLootManager().getTable(identifier);
							LootContext.Builder builder = this.getLootContextBuilder(true, this.damageSource);
							lootTable.generateLoot(builder.build(LootContextTypes.ENTITY), this::dropStack);
						}

					}
				}
			}
		}
	}

	@Inject(method = "dropLoot", at = @At(value = "HEAD"), cancellable = true)
	private void book_of_the_dead$dropLoot(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		Optional<CorpseDataComponent> component = BotDComponents.CORPSE_COMPONENT.maybeGet(livingEntity);
		if (component.isPresent()) {
			boolean isCorpse = component.get().isCorpse;
			if ((isCorpse || BotDApi.isButchering(livingEntity))) {
				this.damageSource = source;
				ci.cancel();
			}
		}
	}

	@Inject(method = "applyDamage", at = @At("HEAD"), cancellable = true)
	protected void book_of_the_dead$morphine(DamageSource source, float amount, CallbackInfo callbackInfo) {
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		if (livingEntity.hasStatusEffect(BotDStatusEffects.MORPHINE) && amount > 0.1f) {
			if (!livingEntity.isInvulnerableTo(source)) {
				LivingEntityDataComponent component = BotDComponents.LIVING_COMPONENT.get(livingEntity);
				component.increaseMorphine$accumulatedDamage(amount);
				callbackInfo.cancel();
			}
		}
	}

	@ModifyVariable(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"), argsOnly = true)
	private StatusEffectInstance book_of_the_dead$convertHarmfulEffect(StatusEffectInstance effect) {
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		if (livingEntity instanceof PlayerEntity player && effect.getEffectType().getType().equals(StatusEffectType.HARMFUL)) {
			PlayerDataComponent component = BotDComponents.PLAYER_COMPONENT.get(player);
			if (component.getStatusEffectConversionBuffModifier() > PlayerAbilityData.STATUS_EFFECT_CONVERSION[0]) {
				if (player.getWorld().random.nextFloat() < component.getStatusEffectConversionBuffModifier()) {
					var statusEffect = statusEffectList.get(player.getRandom().nextInt(statusEffectList.size()));
					return new StatusEffectInstance(statusEffect, effect.getDuration(), effect.getAmplifier());
				}
			}
		}
		return effect;
	}

	@ModifyVariable(method = "applyArmorToDamage", at = @At("HEAD"), argsOnly = true)
	private float book_of_the_dead$handleEntanglement(float amount, DamageSource source) {
		if (world instanceof ServerWorld serverWorld) {
			if (amount > 0) {
				LivingEntity livingEntity = LivingEntity.class.cast(this);
				LivingEntityDataComponent component = BotDComponents.LIVING_COMPONENT.get(livingEntity);
				if (component.getEntangledEntityId() != 0) {
					Entity targetEntity = livingEntity.world.getEntityById(component.getEntangledEntityId());
					if (targetEntity instanceof LivingEntity target) {
						if (!serverWorld.isChunkLoaded(target.getChunkPos().x, target.getChunkPos().z)) {
							serverWorld.setChunkForced(target.getChunkPos().x, target.getChunkPos().z, true);
						}
						target.damage(source, amount * component.getEntangleStrength(livingEntity, target, false));
						amount *= component.getEntangleStrength(livingEntity, target, true);
					}
				}
			}
		}
		return amount;
	}

	@Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At("RETURN"))
	private void book_of_the_dead$handleEntanglement(StatusEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) {
			if (world instanceof ServerWorld serverWorld) {
				LivingEntity livingEntity = LivingEntity.class.cast(this);
				LivingEntityDataComponent component = BotDComponents.LIVING_COMPONENT.get(livingEntity);
				if (component.getEntangledEntityId() != 0) {
					var targetEntity = livingEntity.world.getEntityById(component.getEntangledEntityId());
					if (targetEntity instanceof LivingEntity livingTarget) {
						if (!serverWorld.isChunkLoaded(livingTarget.getChunkPos().x, livingTarget.getChunkPos().z)) {
							serverWorld.setChunkForced(livingTarget.getChunkPos().x, livingTarget.getChunkPos().z, true);
						}
						livingTarget.addStatusEffect(effect, source);
					}
				}
			}
		}
	}
}
