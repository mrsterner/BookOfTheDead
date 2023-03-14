package dev.sterner.book_of_the_dead.mixin;

import dev.sterner.book_of_the_dead.api.interfaces.IHauler;
import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.CorpseDataComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.BodyControl;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {

	@Shadow
	@Final
	@Mutable
	private BodyControl bodyControl;

	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
		this.bodyControl = new BodyControl(null);
	}

	@Inject(method = "turnHead", at = @At("HEAD"), cancellable = true)
	public void book_of_the_dead$turnHead(float bodyRotation, float headRotation, CallbackInfoReturnable<Float> info) {
		if (this.deathTime > 0) {
			info.setReturnValue(0.0F);
		}
	}

	@Inject(method = "interact", at = @At("HEAD"), cancellable = true)
	public void book_of_the_dead$interact(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		MobEntity mob = (MobEntity) (Object)this;
		Optional<CorpseDataComponent> component = BotDComponents.CORPSE_COMPONENT.maybeGet(mob);
		if (this.deathTime > 20 && component.isPresent() && component.get().isCorpse) {
			if(!world.isClient() && player.isSneaking() && player.getMainHandStack().isEmpty()){
				MobEntity mobEntity = (MobEntity) (Object) this;
				IHauler.of(player).ifPresent(hauler -> {
					if(hauler.getCorpseEntity().isEmpty()){
						BlockPos pos = mobEntity.getBlockPos();
						world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_HONEY_BLOCK_BREAK, SoundCategory.PLAYERS, 2,1);
						hauler.setCorpseEntity(mobEntity);
						mobEntity.remove(Entity.RemovalReason.DISCARDED);
					}
				});
				cir.setReturnValue(ActionResult.CONSUME);
			}
			cir.setReturnValue(ActionResult.PASS);
		}
	}
}
