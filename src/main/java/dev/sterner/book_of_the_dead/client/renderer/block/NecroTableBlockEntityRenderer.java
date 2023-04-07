package dev.sterner.book_of_the_dead.client.renderer.block;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.client.event.ClientTickHandler;
import dev.sterner.book_of_the_dead.client.renderer.renderlayer.BotDRenderLayer;
import dev.sterner.book_of_the_dead.common.block.NecroTableBlock;
import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import dev.sterner.book_of_the_dead.common.recipe.RitualRecipe;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.joml.Matrix4f;
import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public class NecroTableBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Constants.id("necro_model"), "main");
	private final Identifier TEXTURE = Constants.id("textures/entity/necro_table.png");
	private final Identifier TEXTURE_TABLE = Constants.id("textures/entity/table.png");
	private float alpha = 0;
	private final ModelPart base;
	private final ModelPart book;
	private final ModelPart candle;
	private final ModelPart slate;
	private final ModelPart cruse;
	private final ModelPart paper;
	private final ModelPart ink;
	private final ModelPart tablet;

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
		base.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
		if (isNecro) {
			candle.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
			slate.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
			cruse.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
			paper.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
			ink.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
		}
	}

	public void renderTablet(MatrixStack matrixStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay) {
		tablet.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
	}

	public void renderBook(MatrixStack matrixStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay) {
		book.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
	}

	public NecroTableBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		ModelPart modelPart = ctx.getLayerModelPart(LAYER_LOCATION);
		this.base = modelPart.getChild("base");
		this.book = modelPart.getChild("book");
		this.candle = modelPart.getChild("candle");
		this.slate = modelPart.getChild("slate");
		this.cruse = modelPart.getChild("cruse");
		this.paper = modelPart.getChild("paper");
		this.ink = modelPart.getChild("ink");
		this.tablet = modelPart.getChild("tablet");
	}

	public static TexturedModelData createBodyLayer() {
		ModelData meshdefinition = new ModelData();
		ModelPartData ModelPartData = meshdefinition.getRoot();

		ModelPartData base = ModelPartData.addChild("base", ModelPartBuilder.create(), ModelTransform.pivot(-8.0F, 24.0F, 0.0F));
		ModelPartData foot = base.addChild("foot", ModelPartBuilder.create().uv(0, 23).cuboid(-12.0F, -3.0F, -8.0F, 24.0F, 3.0F, 16.0F, new Dilation(0.0F)).uv(0, 0).cuboid(-14.0F, -16.0F, -10.0F, 28.0F, 3.0F, 20.0F, new Dilation(0.0F)).uv(0, 59).cuboid(-10.0F, -10.0F, -6.0F, 20.0F, 7.0F, 12.0F, new Dilation(0.0F)).uv(0, 101).cuboid(-10.0F, -10.0F, -6.0F, 20.0F, 7.0F, 12.0F, new Dilation(0.001F)).uv(0, 42).cuboid(-11.0F, -13.0F, -7.0F, 22.0F, 3.0F, 14.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData book = ModelPartData.addChild("book", ModelPartBuilder.create().uv(52, 59).cuboid(-5.0F, 0.0F, -4.0F, 10.0F, 1.0F, 8.0F, new Dilation(0.0F)).uv(64, 68).cuboid(-4.0F, -1.0F, -3.0F, 8.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(-18.5F, 7.0F, -3.8F, 0.0F, -0.4363F, 0.0F));
		ModelPartData candle = ModelPartData.addChild("candle", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		ModelPartData candle1 = candle.addChild("candle1", ModelPartBuilder.create().uv(12, 0).cuboid(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F)).uv(9, 0).cuboid(0.0F, -7.0F, -0.5F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)).uv(0, 11).cuboid(-0.5F, -7.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -16.0F, 4.0F, 0.0F, -0.3054F, 0.0F));
		ModelPartData candle2 = candle.addChild("candle2", ModelPartBuilder.create().uv(0, 9).cuboid(-1.5F, -4.0F, -1.5F, 3.0F, 4.0F, 3.0F, new Dilation(0.0F)).uv(0, 9).cuboid(0.0F, -5.0F, -0.5F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)).uv(9, 9).cuboid(-0.5F, -5.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, -16.0F, 2.5F, 0.0F, 0.1309F, 0.0F));
		ModelPartData candle3 = candle.addChild("candle3", ModelPartBuilder.create().uv(12, 12).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)).uv(0, 8).cuboid(0.0F, -3.0F, -0.5F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)).uv(9, 2).cuboid(-0.5F, -3.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, -16.0F, 1.0F, 0.0F, -0.1309F, 0.0F));
		ModelPartData candle4 = candle.addChild("candle4", ModelPartBuilder.create().uv(0, 23).cuboid(-1.5F, -3.0F, -1.5F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)).uv(0, 1).cuboid(0.0F, -4.0F, -0.5F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)).uv(9, 0).cuboid(-0.5F, -4.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-14.5F, -16.0F, 4.5F, 0.0F, -0.3054F, 0.0F));
		ModelPartData candle5 = candle.addChild("candle5", ModelPartBuilder.create().uv(10, 8).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)).uv(0, 0).cuboid(0.0F, -3.0F, -0.5F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)).uv(0, 0).cuboid(-0.5F, -3.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-14.0F, -16.0F, 2.0F, 0.0F, -0.1309F, 0.0F));
		ModelPartData slate = ModelPartData.addChild("slate", ModelPartBuilder.create().uv(58, 42).cuboid(-12.5F, -17.0F, -1.0F, 9.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		ModelPartData cruse = ModelPartData.addChild("cruse", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -22.0F, 5.0F, 3.0F, 6.0F, 3.0F, new Dilation(0.0F)).uv(0, 16).cuboid(-1.5F, -23.0F, 5.5F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		ModelPartData paper = ModelPartData.addChild("paper", ModelPartBuilder.create().uv(64, 75).cuboid(-11.5F, -16.5F, -8.8F, 4.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		ModelPartData cube_r1 = paper.addChild("cube_r1", ModelPartBuilder.create().uv(64, 68).cuboid(0.0F, -0.5F, -2.0F, 4.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(-6.5F, -16.0F, -5.8F, 0.0F, 0.3054F, 0.0F));
		ModelPartData cube_r2 = paper.addChild("cube_r2", ModelPartBuilder.create().uv(64, 68).cuboid(-2.0F, -0.5F, -2.7F, 4.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(-7.5F, -15.8F, -4.8F, 0.0F, 0.0873F, 0.0F));
		ModelPartData ink = ModelPartData.addChild("ink", ModelPartBuilder.create().uv(80, 61).cuboid(1.0F, -19.0F, -8.0F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)).uv(90, 65).cuboid(1.5F, -20.0F, -7.5F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		ModelPartData cube_r3 = ink.addChild("cube_r3", ModelPartBuilder.create().uv(98, 60).cuboid(-0.5F, -2.5F, 0.0F, 7.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.5F, -22.5F, -6.5F, 0.0F, -0.4363F, 0.0F));
		ModelPartData tablet = ModelPartData.addChild("tablet", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		ModelPartData cube_r4 = tablet.addChild("cube_r4", ModelPartBuilder.create().uv(98, 75).cuboid(-2.0F, 0.5F, -1.5F, 6.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(-21.0F, -17.5F, 3.5F, 0.0F, 0.2618F, 0.0F));

		return TexturedModelData.of(meshdefinition, 128, 128);
	}
}
