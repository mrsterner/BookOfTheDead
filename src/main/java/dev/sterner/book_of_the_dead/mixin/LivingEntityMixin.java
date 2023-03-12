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
	public float bodyYaw;

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void book_of_the_dead$init(EntityType entityType, World world, CallbackInfo ci){

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
			if (livingEntity instanceof MobEntity && (isCorpse || BotDApi.isButchering(livingEntity))){
				component.get().isCorpse(true);
				++this.deathTime;
				if (this.deathTime == 1) {
					if (this.isOnFire())
						this.extinguish();
					if (this.getVehicle() != null)
						this.stopRiding();
				}
				if (this.deathTime >= 20) {
					Box corpseBox = new Box(this.getX() - (this.getWidth() / 2.0F), this.getY() - (this.getWidth() / 2.0F), this.getZ() - (this.getWidth() / 2.0F), this.getX() + (this.getWidth() / 2F), this.getY() + (this.getWidth() / 2F), this.getZ() + (this.getWidth() / 2F));
					this.setBoundingBox(corpseBox.offset(this.getRotationVector(0F, this.bodyYaw).rotateY(- 30.0F)));
				}
				callbackInfo.cancel();
			}
		}
	}
}
