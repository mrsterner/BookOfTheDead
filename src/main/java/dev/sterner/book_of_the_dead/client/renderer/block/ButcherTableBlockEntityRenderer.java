package dev.sterner.book_of_the_dead.client.renderer.block;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.BotDClient;
import dev.sterner.book_of_the_dead.api.enums.HorizontalDoubleBlockHalf;
import dev.sterner.book_of_the_dead.client.renderer.renderlayer.BotDRenderLayer;
import dev.sterner.book_of_the_dead.common.block.NecroTableBlock;
import dev.sterner.book_of_the_dead.common.block.entity.ButcherTableBlockEntity;
import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class ButcherTableBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
	private final Identifier TEXTURE = Constants.id("textures/entity/butcher_table.png");
	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Constants.id("butcher_table"), "main");
	private final ModelPart table;
	private final ModelPart bucket;
	private final ModelPart stol;

	public ButcherTableBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		ModelPart modelPart = ctx.getLayerModelPart(LAYER_LOCATION);
		this.table = modelPart.getChild("table");
		this.bucket = modelPart.getChild("bucket");
		this.stol = modelPart.getChild("stol");
	}


	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = entity.getWorld();
		if(world != null){
			BlockState blockState = entity.getCachedState();
			if(entity instanceof ButcherTableBlockEntity butcherTableBlockEntity && blockState.get(NecroTableBlock.HHALF) == HorizontalDoubleBlockHalf.RIGHT){
				matrices.push();
				float f = blockState.get(NecroTableBlock.FACING).asRotation();
				Direction direction = blockState.get(NecroTableBlock.FACING);
				matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180f));
				matrices.translate(0.5, 0.5, 0.5);
				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-f));
				matrices.translate(-0.5, -0.5, -0.5);

				if(direction == Direction.SOUTH){
					matrices.translate(-0.5,-1.5,0.5);
				}else if(direction == Direction.WEST){
					matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
					matrices.translate(-0.5,-1.5,-1.5);
				}else if(direction == Direction.NORTH){
					matrices.translate(1.5,-1.5,0.5);
				}else if(direction == Direction.EAST){
					matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
					matrices.translate(-0.5,-1.5,0.5);
				}
				render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)), light, overlay);
				matrices.pop();
			}
		}
	}

	private void render(MatrixStack matrices, VertexConsumer buffer, int light, int overlay) {
		table.render(matrices, buffer, light, overlay, 1, 1, 1, 1);
		bucket.render(matrices, buffer, light, overlay, 1, 1, 1, 1);
		stol.render(matrices, buffer, light, overlay,1, 1, 1, 1);
	}


	public static TexturedModelData createBodyLayer() {
		ModelData meshdefinition = new ModelData();
		ModelPartData partdefinition = meshdefinition.getRoot();
		ModelPartData table = partdefinition.addChild("table", ModelPartBuilder.create(), ModelTransform.pivot(-8.0F, 24.0F, 0.0F));

		ModelPartData plate = table.addChild("plate", ModelPartBuilder.create().uv(0, 0).cuboid(-16.0F, -16.0F, -10.0F, 32.0F, 4.0F, 20.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r1 = plate.addChild("cube_r1", ModelPartBuilder.create().uv(55, 83).cuboid(16.9F, -0.5F, 2.3F, 4.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(-6.5F, -16.0F, -5.8F, 0.0F, 0.3054F, 0.0F));

		ModelPartData legs = table.addChild("legs", ModelPartBuilder.create().uv(16, 42).cuboid(7.0F, -12.0F, -8.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(28, 30).cuboid(7.0F, -12.0F, 4.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(0, 34).cuboid(-11.0F, -12.0F, -8.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(0, 0).cuboid(-11.0F, -12.0F, 4.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData support = table.addChild("support", ModelPartBuilder.create().uv(0, 29).cuboid(-7.0F, -5.0F, 5.0F, 14.0F, 3.0F, 2.0F, new Dilation(0.0F))
				.uv(0, 24).cuboid(-7.0F, -5.0F, -7.0F, 14.0F, 3.0F, 2.0F, new Dilation(0.0F))
				.uv(44, 24).cuboid(8.0F, -5.0F, -4.0F, 2.0F, 3.0F, 8.0F, new Dilation(0.0F))
				.uv(36, 38).cuboid(-10.0F, -5.0F, -4.0F, 2.0F, 3.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData headStand = table.addChild("headStand", ModelPartBuilder.create().uv(28, 4).cuboid(8.0F, -18.0F, -4.0F, 3.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r2 = headStand.addChild("cube_r2", ModelPartBuilder.create().uv(31, 7).cuboid(-2.0F, -1.0F, -2.0F, 3.0F, 2.0F, 4.0F, new Dilation(-0.001F)), ModelTransform.of(10.0F, -17.0F, -4.0F, -0.6545F, 0.0F, 0.0F));

		ModelPartData cube_r3 = headStand.addChild("cube_r3", ModelPartBuilder.create().uv(32, 3).cuboid(-2.0F, -1.0F, -2.0F, 3.0F, 2.0F, 4.0F, new Dilation(-0.001F)), ModelTransform.of(10.0F, -17.0F, 4.0F, 0.6545F, 0.0F, 0.0F));

		ModelPartData bucket = partdefinition.addChild("bucket", ModelPartBuilder.create().uv(81, 80).cuboid(-4.5F, -4.7F, 2.5F, 9.0F, 8.0F, 2.0F, new Dilation(0.0F))
				.uv(81, 80).cuboid(-4.5F, -4.7F, -4.5F, 9.0F, 8.0F, 2.0F, new Dilation(0.0F))
				.uv(82, 74).cuboid(-2.5F, 2.3F, -2.5F, 5.0F, 1.0F, 5.0F, new Dilation(0.0F))
				.uv(76, 77).cuboid(-4.5F, -4.7F, -2.5F, 2.0F, 8.0F, 5.0F, new Dilation(0.0F))
				.uv(76, 77).cuboid(2.5F, -4.7F, -2.5F, 2.0F, 8.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(5.6F, 20.7F, -9.5F));

		ModelPartData stol = partdefinition.addChild("stol", ModelPartBuilder.create().uv(66, 52).cuboid(-6.0F, -5.4F, -6.0F, 12.0F, 2.0F, 12.0F, new Dilation(0.0F))
				.uv(32, 49).cuboid(2.0F, -3.4F, -4.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F))
				.uv(32, 49).cuboid(2.0F, -3.4F, 2.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F))
				.uv(32, 49).cuboid(-4.0F, -3.4F, 2.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F))
				.uv(32, 49).cuboid(-4.0F, -3.4F, -4.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-23.0F, 18.4F, -12.0F, 0.0F, -0.6545F, 0.0F));

		return TexturedModelData.of(meshdefinition, 128, 128);
	}

}
