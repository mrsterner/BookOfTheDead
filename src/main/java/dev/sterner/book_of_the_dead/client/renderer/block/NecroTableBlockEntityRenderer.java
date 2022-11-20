package dev.sterner.book_of_the_dead.client.renderer.block;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.BotDClient;
import dev.sterner.book_of_the_dead.api.NecrotableRitual;
import dev.sterner.book_of_the_dead.api.enums.HorizontalDoubleBlockHalf;
import dev.sterner.book_of_the_dead.client.model.LargeCircleEntityModel;
import dev.sterner.book_of_the_dead.client.renderer.renderlayer.BotDRenderLayer;
import dev.sterner.book_of_the_dead.api.block.HorizontalDoubleBlock;
import dev.sterner.book_of_the_dead.common.block.NecroTableBlock;
import dev.sterner.book_of_the_dead.common.block.entity.NecroTableBlockEntity;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

import java.util.List;

public class NecroTableBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Constants.id("necro_model"), "main");
	private final Identifier TEXTURE = Constants.id("textures/entity/necro_table.png");
	private final Identifier CIRCLE_TEXTURE = Constants.id("textures/entity/circle.png");
	private final LargeCircleEntityModel jarEntityModel =  new LargeCircleEntityModel<>(LargeCircleEntityModel.createBodyLayer().createModel());
	private final EntityRenderDispatcher dispatcher;
	private final ModelPart base;
	private final ModelPart book;
	private final ModelPart candle;
	private final ModelPart slate;
	private final ModelPart cruse;
	private final ModelPart paper;
	private final ModelPart cicle;
	private final ModelPart ink;
	private final ModelPart tablet;

	public NecroTableBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		ModelPart modelPart = ctx.getLayerModelPart(LAYER_LOCATION);
		this.dispatcher = ctx.getEntityRendererDispatcher();
		this.base = modelPart.getChild("base");
		this.book = modelPart.getChild("book");
		this.candle = modelPart.getChild("candle");
		this.slate = modelPart.getChild("slate");
		this.cruse = modelPart.getChild("cruse");
		this.paper = modelPart.getChild("paper");
		this.cicle = modelPart.getChild("cicle");
		this.ink = modelPart.getChild("ink");
		this.tablet = modelPart.getChild("tablet");
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = entity.getWorld();
		if(world != null){
			BlockState blockState = entity.getCachedState();
			if(entity instanceof NecroTableBlockEntity necroTableBlockEntity && blockState.get(HorizontalDoubleBlock.HHALF) == HorizontalDoubleBlockHalf.RIGHT){
				matrices.push();
				renderSummonEntity(necroTableBlockEntity, blockState, tickDelta, matrices, vertexConsumers, light, overlay);
				matrices.pop();

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


				if(necroTableBlockEntity.hasEmeraldTablet || true){
					renderTablet(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)), light, overlay);
				}
				if(necroTableBlockEntity.hasBotD || true){
					renderBook(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)), light, overlay);
				}
				render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)), light, overlay);
				matrices.pop();

				matrices.push();
				matrices.translate(1,-1.25,3.5);
				final float rotationModifier = 4F;
				double ticks = (BotDClient.ClientTickHandler.ticksInGame + tickDelta) * 0.5;
				float deg =  (float) (ticks / rotationModifier % 360F);
				matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.sin(deg) / (float) Math.PI));
				matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(MathHelper.cos(deg) / (float) Math.PI));
				renderCircleLarge(matrices, vertexConsumers.getBuffer(BotDRenderLayer.get(CIRCLE_TEXTURE)), light, overlay);

				matrices.pop();
			}
		}
	}

	private void renderSummonEntity(NecroTableBlockEntity necroTableBlockEntity, BlockState blockState, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if(necroTableBlockEntity.currentNecrotableRitual != null && !necroTableBlockEntity.currentNecrotableRitual.summons.isEmpty()){
			if(necroTableBlockEntity.getWorld() != null){
				NecrotableRitual ritual = necroTableBlockEntity.currentNecrotableRitual;
				//BlockPos ritualCenter = ritual.ritualCenter;
				BlockPos ritualCenter = necroTableBlockEntity.getPos();
				List<Entity> entityList = ritual.summons;
				if(!entityList.isEmpty()){
					World world = necroTableBlockEntity.getWorld();
					double ticks = (BotDClient.ClientTickHandler.ticksInGame + tickDelta) * 0.5;
					for(Entity entity : entityList){
						if(entity instanceof LivingEntity livingEntity){
							double offsetInCircle = world.getRandom().nextDouble();
							float offsetInGround = livingEntity.getHeight();
							float f = ((float)ticks + tickDelta - 1.0F) / 20.0F * 5F;
							f = MathHelper.sqrt(f);
							if (f > 1.0F) {
								f = 1.0F;
							}

							System.out.println(offsetInGround +" : " + f);
							livingEntity.headYaw = 0;
							matrices.translate(0,MathHelper.sin((float)ticks),0);
							matrices.translate(0,offsetInGround - f * offsetInGround ,0);
							dispatcher.render(livingEntity, 0, 0, 0, 0 ,tickDelta, matrices, vertexConsumers, light);
						}

					}

				}


			}

		}
	}

	private void renderCircleLarge(MatrixStack matrices, VertexConsumer buffer, int light, int overlay) {
		jarEntityModel.render(matrices, buffer, light, overlay, 1,1,1,1);
	}


	public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay) {
		base.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
		candle.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
		slate.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
		cruse.render(matrixStack, vertexConsumer, packedLight, packedOverlay,1, 1, 1, 1);
		paper.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
		ink.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
	}

	public void renderTablet(MatrixStack matrixStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay) {
		tablet.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
	}

	public void renderCircle(MatrixStack matrixStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay) {
		cicle.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
	}

	public void renderBook(MatrixStack matrixStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay) {
		book.render(matrixStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);
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
		ModelPartData cicle = ModelPartData.addChild("cicle", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 19.0F, 0.0F));
		ModelPartData small = cicle.addChild("small", ModelPartBuilder.create().uv(82, 110).cuboid(-13.0F, -23.0F, -1.0F, 10.0F, 0.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData big = cicle.addChild("big", ModelPartBuilder.create().uv(62, 89).cuboid(-16.0F, -20.0F, -4.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData ink = ModelPartData.addChild("ink", ModelPartBuilder.create().uv(80, 61).cuboid(1.0F, -19.0F, -8.0F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)).uv(90, 65).cuboid(1.5F, -20.0F, -7.5F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		ModelPartData cube_r3 = ink.addChild("cube_r3", ModelPartBuilder.create().uv(98, 60).cuboid(-0.5F, -2.5F, 0.0F, 7.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.5F, -22.5F, -6.5F, 0.0F, -0.4363F, 0.0F));
		ModelPartData tablet = ModelPartData.addChild("tablet", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		ModelPartData cube_r4 = tablet.addChild("cube_r4", ModelPartBuilder.create().uv(98, 75).cuboid(-2.0F, 0.5F, -1.5F, 6.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(-21.0F, -17.5F, 3.5F, 0.0F, 0.2618F, 0.0F));

		return TexturedModelData.of(meshdefinition, 128, 128);
	}


}
