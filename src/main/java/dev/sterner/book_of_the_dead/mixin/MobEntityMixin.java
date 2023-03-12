package dev.sterner.book_of_the_dead.mixin;

import dev.sterner.book_of_the_dead.api.interfaces.IHauler;
import dev.sterner.book_of_the_dead.common.component.BotDComponents;
import dev.sterner.book_of_the_dead.common.component.CorpseDataComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "interact", at = @At("HEAD"), cancellable = true)
	public void interactAt(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		MobEntity mob = (MobEntity) (Object)this;
		Optional<CorpseDataComponent> component = BotDComponents.CORPSE_COMPONENT.maybeGet(mob);
		if (this.deathTime > 20 && component.isPresent() && component.get().isCorpse) {
			if(!world.isClient() && player.isSneaking() && player.getMainHandStack().isEmpty()){
				MobEntity mobEntity = (MobEntity) (Object) this;
				IHauler.of(player).ifPresent(hauler -> {
					if(hauler.getCorpseEntity().isEmpty()){
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
