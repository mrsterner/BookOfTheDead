package dev.sterner.book_of_the_dead.client.renderer.block;

import dev.sterner.book_of_the_dead.common.block.NecroTableBlock;
import dev.sterner.book_of_the_dead.common.block.entity.RetortFlaskBlockEntity;
import dev.sterner.book_of_the_dead.common.registry.BotDSpriteIdentifiers;
import dev.sterner.book_of_the_dead.common.util.Constants;
import dev.sterner.book_of_the_dead.common.util.RenderUtils;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.impl.client.indigo.renderer.helper.ColorHelper;
import net.fabricmc.fabric.impl.renderer.RendererAccessImpl;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class RetortFlaskBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
	private static final float EDGE_SIZE = 1f / 8f;
	private static final float INNER_SIZE = 1f - (EDGE_SIZE * 2f);
	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Constants.id("retort"), "main");
	private final Identifier TEXTURE = Constants.id("textures/block/retort_flask.png");
	private final ModelPart main;

	public RetortFlaskBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		ModelPart modelPart = ctx.getLayerModelPart(LAYER_LOCATION);
		this.main = modelPart.getChild("main");
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = entity.getWorld();
		if (world != null) {
			BlockState blockState = entity.getCachedState();
			if (entity instanceof RetortFlaskBlockEntity retortFlaskBlockEntity) {
				if (retortFlaskBlockEntity.hasLiquid) {
					matrices.push();
					float g = 0.5F;
					matrices.scale(g, g, g);
					matrices.translate(0.5, 0.3, 0.5);
					renderFluid(matrices, vertexConsumers, light, overlay, 0.75f, retortFlaskBlockEntity.color);
					matrices.pop();
				}


				matrices.push();
				float f = blockState.get(NecroTableBlock.FACING).asRotation();
				matrices.translate(0.5, 0.5, 0.5);
				matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(-f));
				matrices.translate(0, 1, 0);
				matrices.multiply(Axis.Z_POSITIVE.rotationDegrees(180f));
				main.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)), light, overlay, 1, 1, 1, 1);
				matrices.pop();
			}
		}
	}

	private void renderFluid(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float percent, int color) {
		if (RendererAccessImpl.INSTANCE.getRenderer() != null) {
			percent = Math.min(1, percent);
			matrices.push();
			MeshBuilder builder = RendererAccessImpl.INSTANCE.getRenderer().meshBuilder();
			Sprite sprite = BotDSpriteIdentifiers.WATER.getSprite();
			int newColor = ColorHelper.swapRedBlueIfNeeded(color);
			RenderUtils.emitFluidFace(builder.getEmitter(), sprite, newColor, Direction.UP, 1f, (1f - percent), EDGE_SIZE, INNER_SIZE);
			RenderUtils.emitFluidFace(builder.getEmitter(), sprite, newColor, Direction.DOWN, 1f, 0f, EDGE_SIZE, INNER_SIZE);
			RenderUtils.emitFluidFace(builder.getEmitter(), sprite, newColor, Direction.NORTH, percent, 0f, EDGE_SIZE, INNER_SIZE);
			RenderUtils.emitFluidFace(builder.getEmitter(), sprite, newColor, Direction.EAST, percent, 0f, EDGE_SIZE, INNER_SIZE);
			RenderUtils.emitFluidFace(builder.getEmitter(), sprite, newColor, Direction.SOUTH, percent, 0f, EDGE_SIZE, INNER_SIZE);
			RenderUtils.emitFluidFace(builder.getEmitter(), sprite, newColor, Direction.WEST, percent, 0f, EDGE_SIZE, INNER_SIZE);

			RenderUtils.renderMesh(builder.build(), matrices, vertexConsumers.getBuffer(RenderLayer.getTranslucent()), light, overlay);

			matrices.pop();
		}
	}

	public static TexturedModelData createBodyLayer() {
		ModelData meshdefinition = new ModelData();
		ModelPartData modelPartData = meshdefinition.getRoot();

		ModelPartData main = modelPartData.addChild("main", ModelPartBuilder.create().uv(7, 21).cuboid(-4.0F, -3.0F, -4.0F, 8.0F, 1.0F, 1.0F, new Dilation(0.0F)).uv(8, 16).cuboid(3.0F, -3.0F, -3.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F)).uv(8, 16).cuboid(-4.0F, -3.0F, -3.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F)).uv(7, 21).cuboid(-4.0F, -3.0F, 3.0F, 8.0F, 1.0F, 1.0F, new Dilation(0.0F)).uv(0, 0).cuboid(-3.5F, -8.01F, -3.5F, 7.0F, 7.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 22.0F, 0.0F));
		main.addChild("cube_r1", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-3.0F, -2.0F, 3.0F, 0.1309F, 0.0F, 0.1309F));
		main.addChild("cube_r2", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-3.0F, -2.0F, -3.0F, -0.1309F, 0.0F, 0.1309F));
		main.addChild("cube_r3", ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -2.0F, 3.0F, 0.1309F, 0.0F, -0.1309F));
		main.addChild("cube_r4", ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -2.0F, -3.0F, -0.1309F, 0.0F, -0.1309F));
		main.addChild("cube_r5", ModelPartBuilder.create().uv(4, 5).cuboid(-1.0F, -1.0F, -5.0F, 2.0F, 2.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -7.0F, -3.0F, 0.48F, 0.0F, 0.0F));
		return TexturedModelData.of(meshdefinition, 32, 32);
	}
}
