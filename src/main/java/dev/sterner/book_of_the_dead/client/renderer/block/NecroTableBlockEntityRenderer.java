package dev.sterner.book_of_the_dead.client.renderer.block;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.client.event.ClientTickHandler;
import dev.sterner.book_of_the_dead.client.model.NecroTableBlockEntityModel;
import dev.sterner.book_of_the_dead.client.registry.BotDRenderLayer;
import dev.sterner.book_of_the_dead.common.block.NecroTableBlock;
import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import dev.sterner.book_of_the_dead.common.recipe.RitualRecipe;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.joml.Matrix4f;
import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public class NecroTableBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T>, BuiltinItemRendererRegistry.DynamicItemRenderer {
	private final Identifier TEXTURE = Constants.id("textures/entity/necro_table.png");
	private final Identifier TEXTURE_TABLE = Constants.id("textures/entity/table.png");
	private final NecroTableBlockEntityModel model = new NecroTableBlockEntityModel(NecroTableBlockEntityModel.createBodyLayer().createModel());
	private float alpha = 0;

	@Override
	public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE_TABLE)), light, overlay, false);
		matrices.pop();
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = entity.getWorld();
		if (world != null && world.isClient) {
			BlockState blockState = entity.getCachedState();
			if (entity instanceof NecroTableBlockEntity necroTableBlockEntity) {


				matrices.push();
				float f = blockState.get(NecroTableBlock.FACING).asRotation();
				Direction direction = blockState.get(NecroTableBlock.FACING);
				matrices.multiply(Axis.Z_POSITIVE.rotationDegrees(180f));
				matrices.translate(0.5, 0.5, 0.5);
				matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(-f));
				matrices.translate(-0.5, -0.5, -0.5);

				if (direction == Direction.SOUTH) {
					matrices.translate(0, -1.5, 0.5);
				} else if (direction == Direction.WEST) {
					matrices.translate(0, -1.5, 1.5);
					matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(180));
				} else if (direction == Direction.NORTH) {
					matrices.translate(2, -1.5, 0.5);
				} else if (direction == Direction.EAST) {
					matrices.translate(0, -1.5, -0.5);
					matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(180));
				}

				if (necroTableBlockEntity.hasEmeraldTablet) {
					renderTablet(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)), light, overlay);
				}
				if (necroTableBlockEntity.hasBotD) {
					renderBook(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)), light, overlay);
				}
				render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(necroTableBlockEntity.isNecroTable ? TEXTURE : TEXTURE_TABLE)), light, overlay, necroTableBlockEntity.isNecroTable);
				matrices.pop();


				matrices.push();
				matrices.translate(0.5, 0.15, 0.5);

				switch (necroTableBlockEntity.getCachedState().get(NecroTableBlock.FACING)) {
					case NORTH -> matrices.translate(0, 0, -4);
					case SOUTH -> matrices.translate(0, 0, 4);
					case EAST -> matrices.translate(4, 0, 0);
					case WEST -> matrices.translate(-4, 0, 0);
				}

				final float rotationModifier = 4F;
				double ticks = ClientTickHandler.ticksInGame + tickDelta * 0.5;
				float deg = (float) (ticks / rotationModifier % 360F);
				matrices.multiply(Axis.Z_POSITIVE.rotationDegrees(MathHelper.sin(deg) / (float) Math.PI));
				matrices.multiply(Axis.X_POSITIVE.rotationDegrees(MathHelper.cos(deg) / (float) Math.PI));
				matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(entity.getCachedState().get(HorizontalFacingBlock.FACING).asRotation()));

				if (necroTableBlockEntity.shouldRun) {
					alpha = ((float) necroTableBlockEntity.clientTime + tickDelta - 1.0F) / 20.0F * 1.6F;
					alpha = MathHelper.sqrt(alpha);
					if (alpha > 1.0F) {
						alpha = 1.0F;
					}
				} else {
					if (alpha < 0.02) {
						alpha = 0;
					} else {
						alpha = alpha - 0.01f;
					}
				}

				Matrix4f mat = matrices.peek().getModel();
				RitualRecipe recipe = necroTableBlockEntity.ritualRecipe;
				Identifier texture;
				if (recipe != null) {
					texture = recipe.texture();
					VertexConsumer vertexConsumer = vertexConsumers.getBuffer(BotDRenderLayer.GLOWING_LAYER.apply(texture));
					vertexConsumer.vertex(mat, -2.5F, 0, 2.5F).color(1f, 1f, 1f, alpha).uv(0, 1).overlay(overlay).light(light).normal(0, 1, 0).next();
					vertexConsumer.vertex(mat, 2.5F, 0, 2.5F).color(1f, 1f, 1f, alpha).uv(1, 1).overlay(overlay).light(light).normal(0, 1, 0).next();
					vertexConsumer.vertex(mat, 2.5F, 0, -2.5F).color(1f, 1f, 1f, alpha).uv(1, 0).overlay(overlay).light(light).normal(0, 1, 0).next();
					vertexConsumer.vertex(mat, -2.5F, 0, -2.5F).color(1f, 1f, 1f, alpha).uv(0, 0).overlay(overlay).light(light).normal(0, 1, 0).next();
				}

				matrices.pop();
			}
		}
	}

	public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, boolean isNecro) {
		model.base.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
		if (isNecro) {
			model.candle.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
			model.slate.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
			model.cruse.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
			model.paper.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
			model.ink.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
		}
	}

	public void renderTablet(MatrixStack matrixStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay) {
		model.tablet.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
	}

	public void renderBook(MatrixStack matrixStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay) {
		model.book.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
	}


}
