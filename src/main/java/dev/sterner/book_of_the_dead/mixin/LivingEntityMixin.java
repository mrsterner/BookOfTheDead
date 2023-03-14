package dev.sterner.book_of_the_dead.mixin;

import dev.sterner.book_of_the_dead.api.BotDApi;
import dev.sterner.book_of_the_dead.api.event.OnEntityDeathEvent;
import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.CorpseDataComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = LivingEntity.class, priority = 1001)
public abstract class LivingEntityMixin extends Entity {

	@Shadow
	public int deathTime;

	@Shadow
	protected abstract void initDataTracker();

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "onDeath", at = @At("HEAD"))
	private void book_of_the_dead$PreOnDeath(DamageSource source, CallbackInfo ci){
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		OnEntityDeathEvent.START.invoker().start(livingEntity, livingEntity.getBlockPos(), source);
	}

	@Inject(method = "onDeath", at = @At("TAIL"))
	private void book_of_the_dead$postOnDeath(DamageSource source, CallbackInfo ci){
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

	@Inject(method = "updatePostDeath", at = @At("HEAD"), cancellable = true)
	protected void book_of_the_dead$updatePostDeath(CallbackInfo callbackInfo) {
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		Optional<CorpseDataComponent> component = BotDComponents.CORPSE_COMPONENT.maybeGet(livingEntity);
		if(component.isPresent()){
			boolean isCorpse = component.get().isCorpse;
			if((isCorpse || BotDApi.isButchering(livingEntity))){
				if(livingEntity instanceof PlayerEntity){
					component.get().isCorpse(true);
				}
				if (livingEntity instanceof MobEntity){
					component.get().isCorpse(true);
					++livingEntity.deathTime;
					if (livingEntity.deathTime == 1) {
						if (livingEntity.isOnFire()){
							livingEntity.extinguish();
						}
						if (livingEntity.getVehicle() != null){
							livingEntity.stopRiding();
						}
					}
					if (livingEntity.deathTime >= 20) {
						Box corpseBox = new Box(livingEntity.getX() - (livingEntity.getWidth() / 2.0F), livingEntity.getY() - (livingEntity.getWidth() / 2.0F), livingEntity.getZ() - (livingEntity.getWidth() / 2.0F), livingEntity.getX() + (livingEntity.getWidth() / 2F), livingEntity.getY() + (livingEntity.getWidth() / 2F), livingEntity.getZ() + (livingEntity.getWidth() / 2F));
						livingEntity.setBoundingBox(corpseBox.offset(livingEntity.getRotationVector(0F, livingEntity.bodyYaw).rotateY(- 30.0F)));
					}
					callbackInfo.cancel();
				}
			}
		}
	}
}
