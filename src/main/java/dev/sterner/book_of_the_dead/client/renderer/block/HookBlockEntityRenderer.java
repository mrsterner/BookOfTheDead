package dev.sterner.book_of_the_dead.client.renderer.block;

import dev.sterner.book_of_the_dead.api.interfaces.IHauler;
import dev.sterner.book_of_the_dead.common.block.entity.HookBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Axis;
import net.minecraft.world.World;

public class HookBlockEntityRenderer implements BlockEntityRenderer<HookBlockEntity> {
	private final EntityRenderDispatcher dispatcher;

	public HookBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		this.dispatcher = ctx.getEntityRendererDispatcher();
	}

	public float getRot(BlockEntity entity) {
		World world = entity.getWorld();
		if (world != null) {
			BlockState blockState = world.getBlockState(entity.getPos());
			if (blockState.getBlock() instanceof HorizontalFacingBlock) {
				return blockState.get(HorizontalFacingBlock.FACING).asRotation();
			}
		}
		return 0;
	}

	@Override
	public void render(HookBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		IHauler.of(entity).ifPresent(hauler -> {
			NbtCompound renderedEntity = hauler.getCorpseEntity();
			if (renderedEntity != null && !renderedEntity.isEmpty()) {
				EntityType.getEntityFromNbt(renderedEntity, entity.getWorld()).ifPresent(type -> {
					if (type instanceof LivingEntity livingEntity) {
						livingEntity.hurtTime = 0;
						livingEntity.deathTime = 0;
						livingEntity.bodyYaw = 0;
						livingEntity.setPitch(20);
						livingEntity.prevPitch = 20;
						livingEntity.headYaw = 0;
						dispatcher.setRenderShadows(false);
						matrices.translate(0.5, -livingEntity.getHeight() * 0.5, 0.5);
						matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(-getRot(entity)));
						matrices.translate(0, 0, 0.2);
						dispatcher.render(livingEntity, 0, 0, 0, 0, tickDelta, matrices, vertexConsumers, light);
					}
				});
			}
		});
		matrices.pop();
	}
}
