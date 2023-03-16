package dev.sterner.book_of_the_dead.client.renderer.block;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.api.enums.HorizontalDoubleBlockHalf;
import dev.sterner.book_of_the_dead.api.block.HorizontalDoubleBlock;
import dev.sterner.book_of_the_dead.api.interfaces.IHauler;
import dev.sterner.book_of_the_dead.common.block.ButcherBlock;
import dev.sterner.book_of_the_dead.common.block.NecroTableBlock;
import dev.sterner.book_of_the_dead.common.block.entity.ButcherTableBlockEntity;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.registry.BotDParticleTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDSpriteIdentifiers;
import dev.sterner.book_of_the_dead.common.util.Constants;
import dev.sterner.book_of_the_dead.common.util.RenderUtils;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.impl.client.indigo.renderer.helper.ColorHelper;
import net.fabricmc.fabric.impl.renderer.RendererAccessImpl;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.joml.Matrix4f;

import java.util.Properties;

import static dev.sterner.book_of_the_dead.api.block.HorizontalDoubleBlock.FACING;

public class ButcherTableBlockEntityRenderer implements BlockEntityRenderer<ButcherTableBlockEntity> {
	private static final float[] HEIGHT = {0, 0.15f, 0.25f, 0.35f};
	public static final int BLOOD_COLOR = 0xff0000;
	private final Identifier TEXTURE = Constants.id("textures/entity/butcher_table.png");
	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Constants.id("butcher_table"), "main");
	private final ModelPart table;
	private final ModelPart bucket;
	private final ModelPart stol;
	private final ModelPart blood;
	private final ModelPart filth;
	private final EntityRenderDispatcher dispatcher;


	public ButcherTableBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		this.dispatcher = ctx.getEntityRendererDispatcher();
		ModelPart modelPart = ctx.getLayerModelPart(LAYER_LOCATION);
		this.table = modelPart.getChild("table");
		this.bucket = modelPart.getChild("bucket");
		this.stol = modelPart.getChild("stol");
		this.blood = modelPart.getChild("blood");
		this.filth = modelPart.getChild("filth");
	}


	@Override
	public void render(ButcherTableBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		renderEntityOnTable(entity, tickDelta, matrices, vertexConsumers, light, overlay);
		matrices.pop();

		matrices.push();
		switch (entity.getCachedState().get(FACING)){
			case NORTH -> matrices.translate(0.35,0.05,0.6);
			case SOUTH -> matrices.translate(-0.35,0.05,-0.6);
			case WEST -> matrices.translate(0.6,0.05,-0.35);
			case EAST -> matrices.translate(-0.6,0.05,0.35);
		}
		matrices.translate(0, HEIGHT[entity.bloodLevel], 0);
		renderBloodLevel(entity, matrices, vertexConsumers, light, overlay);
		matrices.pop();

		World world = entity.getWorld();
		if(world != null){
			BlockState blockState = entity.getCachedState();
			renderBubbleEffect(world, entity, entity.getPos());
			if(blockState.get(HorizontalDoubleBlock.HHALF) == HorizontalDoubleBlockHalf.RIGHT){
				matrices.push();
				float f = blockState.get(NecroTableBlock.FACING).asRotation();
				Direction direction = blockState.get(NecroTableBlock.FACING);
				matrices.multiply(Axis.Z_POSITIVE.rotationDegrees(180f));
				matrices.translate(0.5, 0.5, 0.5);
				matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(-f));
				matrices.translate(-0.5, -0.5, -0.5);

				if(direction == Direction.SOUTH){
					matrices.translate(-0.5,-1.5,0.5);
				}else if(direction == Direction.WEST){
					matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(180));
					matrices.translate(-0.5,-1.5,-1.5);
				}else if(direction == Direction.NORTH){
					matrices.translate(1.5,-1.5,0.5);
				}else if(direction == Direction.EAST){
					matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(180));
					matrices.translate(-0.5,-1.5,0.5);
				}
				var buffer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));
				render(matrices, buffer, light, overlay);
				if(entity.getFilthLevel() > 2){
					filth.render(matrices, buffer, light, overlay,1, 1, 1, 1);
				}else if(entity.getFilthLevel() > 0){
					blood.render(matrices, buffer, light, overlay,1, 1, 1, 1);
				}

				matrices.pop();
			}
		}
	}

	private void renderBloodLevel(ButcherTableBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if(entity.getCachedState().get(HorizontalDoubleBlock.HHALF) == HorizontalDoubleBlockHalf.RIGHT && entity.bloodLevel > 0){
			Sprite sprite = BotDSpriteIdentifiers.BLOOD.getSprite();

			float sizeFactor = 0.25F;
			float maxV = (sprite.getMaxV() - sprite.getMinV()) * sizeFactor;
			float minV = (sprite.getMaxV() - sprite.getMinV()) * (1 - sizeFactor);
			int red = (BLOOD_COLOR >> 16) & 0xFF;
			int green = (BLOOD_COLOR >> 8) & 0xFF;
			int blue = BLOOD_COLOR & 0xFF;

			Matrix4f mat = matrices.peek().getModel();
			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getTranslucent());
			vertexConsumer.vertex(mat, sizeFactor, 0, 1 - sizeFactor).color(red, green, blue, 255).uv(sprite.getMinU(), sprite.getMinV() + maxV).light(light).overlay(overlay).normal(1, 1, 1).next();
			vertexConsumer.vertex(mat, 1 - sizeFactor, 0, 1 - sizeFactor).color(red, green, blue, 255).uv(sprite.getMaxU(), sprite.getMinV() + maxV).light(light).overlay(overlay).normal(1, 1, 1).next();
			vertexConsumer.vertex(mat, 1 - sizeFactor, 0, sizeFactor).color(red, green, blue, 255).uv(sprite.getMaxU(), sprite.getMinV() + minV).light(light).overlay(overlay).normal(1, 1, 1).next();
			vertexConsumer.vertex(mat, sizeFactor, 0, sizeFactor).color(red, green, blue, 255).uv(sprite.getMinU(), sprite.getMinV() + minV).light(light).overlay(overlay).normal(1, 1, 1).next();
		}
	}

	private void renderBubbleEffect(World world, ButcherTableBlockEntity entity, BlockPos pos) {
		Direction targetDirection = world.getBlockState(pos).get(FACING).rotateClockwise(Direction.Axis.Y).getOpposite();
		BlockPos neighbourPos = pos.offset(targetDirection);


		if(entity.latter > 0){
			float width = 0.5f;
			world.addParticle((ParticleEffect) BotDParticleTypes.SOAP_BUBBLE,
					pos.getX() + 0.5 + MathHelper.nextDouble(world.random, -width, width),
					pos.getY() + 1.1,
					pos.getZ() + 0.5 + MathHelper.nextDouble(world.random, -width, width),
					0,
					0,
					0);

			world.addParticle((ParticleEffect) BotDParticleTypes.SOAP_BUBBLE,
					neighbourPos.getX() + 0.5 + MathHelper.nextDouble(world.random, -width, width),
					neighbourPos.getY() + 1.1,
					neighbourPos.getZ() + 0.5 + MathHelper.nextDouble(world.random, -width, width),
					0,
					0,
					0);
		}
	}

	private void renderEntityOnTable(ButcherTableBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		IHauler.of(entity).ifPresent(hauler -> {
			NbtCompound renderedEntity = hauler.getCorpseEntity();
			if(renderedEntity != null && !renderedEntity.isEmpty() && entity.getWorld() != null){
				EntityType.getEntityFromNbt(renderedEntity, entity.getWorld()).ifPresent(type -> {
					if(type instanceof LivingEntity livingEntity){
						livingEntity.hurtTime = 0;
						livingEntity.deathTime = 0;
						livingEntity.bodyYaw = 0;
						livingEntity.setPitch(20);
						livingEntity.prevPitch = 20;
						livingEntity.headYaw = 0;
						dispatcher.setRenderShadows(false);
						setupTransforms(matrices, entity.getWorld().getBlockState(entity.getPos()));
						if(livingEntity instanceof AnimalEntity){
							matrices.translate(0.75,0.1,-1.0);
							matrices.multiply(Axis.X_POSITIVE.rotationDegrees(-90));
							matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(90));
						}
						matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(getRot(entity) + 90));
						matrices.multiply(Axis.X_POSITIVE.rotationDegrees(-90));

						EntityRenderer<? super LivingEntity> entityRenderer = dispatcher.getRenderer(livingEntity);
						if(entityRenderer instanceof LivingEntityRenderer<?,?> livingEntityRenderer){
							handleVisibilities(livingEntityRenderer, hauler);
						}
						dispatcher.render(livingEntity, 0,0,0,0, tickDelta, matrices, vertexConsumers, light);
						if(entityRenderer instanceof LivingEntityRenderer<?,?> livingEntityRenderer){
							resetVisibility(livingEntityRenderer);
						}

					}
				});


			}
		});
	}

	private void setupTransforms(MatrixStack matrices, BlockState blockState) {
		Direction dir = blockState.get(HorizontalFacingBlock.FACING);
		switch (dir) {
			case NORTH -> matrices.translate(-1.0,1.15,0.5);
			case SOUTH -> matrices.translate(2.0,1.15,0.5);
			case EAST -> matrices.translate(0.5,1.15,-1.0);
			case WEST -> matrices.translate(0.5,1.15,2.0);
		}
	}

	private void resetVisibility(LivingEntityRenderer<?,?> livingEntityRenderer) {
		EntityModel<? extends LivingEntity> model = livingEntityRenderer.getModel();
		if(model instanceof QuadrupedEntityModel<?> quadrupedEntityModel){
			quadrupedEntityModel.head.visible = true;
			quadrupedEntityModel.rightHindLeg.visible = true;
			quadrupedEntityModel.leftHindLeg.visible = true;
			quadrupedEntityModel.rightFrontLeg.visible = true;
			quadrupedEntityModel.leftFrontLeg.visible = true;
		} else if(model instanceof BipedEntityModel<?> bipedEntityModel){
			bipedEntityModel.head.visible = true;
			bipedEntityModel.rightLeg.visible = true;
			bipedEntityModel.leftLeg.visible = true;
			bipedEntityModel.rightArm.visible = true;
			bipedEntityModel.leftArm.visible = true;

		} else if(model instanceof VillagerResemblingModel<?> villagerResemblingModel){
			villagerResemblingModel.getHead().visible = true;
			villagerResemblingModel.getPart().getChild(EntityModelPartNames.RIGHT_LEG).visible = true;
			villagerResemblingModel.getPart().getChild(EntityModelPartNames.LEFT_LEG).visible = true;
			villagerResemblingModel.getPart().getChild(EntityModelPartNames.ARMS).visible = true;
		}
	}

	private void handleVisibilities(LivingEntityRenderer<?,?> livingEntityRenderer, IHauler entity) {
		EntityModel<? extends LivingEntity> model = livingEntityRenderer.getModel();
		if(model instanceof QuadrupedEntityModel<?> quadrupedEntityModel){
			quadrupedEntityModel.head.visible = entity.getHeadVisible();
			quadrupedEntityModel.rightHindLeg.visible = entity.getRLegVisible();
			quadrupedEntityModel.leftHindLeg.visible = entity.getLLegVisible();
			quadrupedEntityModel.rightFrontLeg.visible = entity.getRArmVisible();
			quadrupedEntityModel.leftFrontLeg.visible = entity.getLArmVisible();
		} else if(model instanceof BipedEntityModel<?> bipedEntityModel){
			bipedEntityModel.head.visible = entity.getHeadVisible();
			bipedEntityModel.rightLeg.visible = entity.getRLegVisible();
			bipedEntityModel.leftLeg.visible = entity.getLLegVisible();
			bipedEntityModel.rightArm.visible = entity.getRArmVisible();
			bipedEntityModel.leftArm.visible = entity.getLArmVisible();

		} else if(model instanceof VillagerResemblingModel<?> villagerResemblingModel){
			villagerResemblingModel.getHead().visible = entity.getHeadVisible();
			villagerResemblingModel.getPart().getChild(EntityModelPartNames.RIGHT_LEG).visible = entity.getRLegVisible();
			villagerResemblingModel.getPart().getChild(EntityModelPartNames.LEFT_LEG).visible = entity.getLLegVisible();
			villagerResemblingModel.getPart().getChild(EntityModelPartNames.ARMS).visible = entity.getRArmVisible() || entity.getLArmVisible();
		}
	}

	private void render(MatrixStack matrices, VertexConsumer buffer, int light, int overlay) {
		table.render(matrices, buffer, light, overlay, 1, 1, 1, 1);
		bucket.render(matrices, buffer, light, overlay, 1, 1, 1, 1);
		stol.render(matrices, buffer, light, overlay,1, 1, 1, 1);
	}
	public int getRot(ButcherTableBlockEntity entity){
		World world = entity.getWorld();
		BlockPos blockPos = entity.getPos();
		if (world != null) {
			BlockState blockState = world.getBlockState(blockPos);
			if(blockState.isOf(BotDObjects.BUTCHER_TABLE)){
				return switch (blockState.get(FACING)){
					case EAST -> 90;
					case NORTH ->  180;
					case WEST ->  270;
					default ->  0;
				};
			}
		}
		return 0;
	}

	public static TexturedModelData createBodyLayer() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData table = modelPartData.addChild("table", ModelPartBuilder.create(), ModelTransform.pivot(-8.0F, 24.0F, 0.0F));

		ModelPartData plate = table.addChild("plate", ModelPartBuilder.create().uv(0, 0).cuboid(-16.0F, -16.0F, -10.0F, 32.0F, 4.0F, 20.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData cube_r1 = plate.addChild("cube_r1", ModelPartBuilder.create().uv(55, 83).cuboid(16.9F, -0.5F, 2.3F, 4.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(-6.5F, -16.0F, -5.8F, 0.0F, 0.3054F, 0.0F));
		ModelPartData legs = table.addChild("legs", ModelPartBuilder.create().uv(10, 74).cuboid(7.0F, -12.0F, -8.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)).uv(10, 74).cuboid(7.0F, -12.0F, 4.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)).uv(10, 74).cuboid(-11.0F, -12.0F, -8.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)).uv(10, 74).cuboid(-11.0F, -12.0F, 4.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData support = table.addChild("support", ModelPartBuilder.create().uv(10, 108).cuboid(-7.0F, -5.0F, 5.0F, 14.0F, 3.0F, 2.0F, new Dilation(0.0F)).uv(11, 105).cuboid(-7.0F, -5.0F, -7.0F, 14.0F, 3.0F, 2.0F, new Dilation(0.0F)).uv(15, 105).cuboid(8.0F, -5.0F, -4.0F, 2.0F, 3.0F, 8.0F, new Dilation(0.0F)).uv(17, 105).cuboid(-10.0F, -5.0F, -4.0F, 2.0F, 3.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData headStand = table.addChild("headStand", ModelPartBuilder.create().uv(28, 4).cuboid(8.0F, -18.0F, -4.0F, 3.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData cube_r2 = headStand.addChild("cube_r2", ModelPartBuilder.create().uv(31, 7).cuboid(-2.0F, -1.0F, -2.0F, 3.0F, 2.0F, 4.0F, new Dilation(-0.001F)), ModelTransform.of(10.0F, -17.0F, -4.0F, -0.6545F, 0.0F, 0.0F));
		ModelPartData cube_r3 = headStand.addChild("cube_r3", ModelPartBuilder.create().uv(32, 3).cuboid(-2.0F, -1.0F, -2.0F, 3.0F, 2.0F, 4.0F, new Dilation(-0.001F)), ModelTransform.of(10.0F, -17.0F, 4.0F, 0.6545F, 0.0F, 0.0F));
		ModelPartData bucket = modelPartData.addChild("bucket", ModelPartBuilder.create().uv(81, 80).cuboid(-4.5F, -4.7F, 2.5F, 9.0F, 8.0F, 2.0F, new Dilation(0.0F)).uv(81, 80).cuboid(-4.5F, -4.7F, -4.5F, 9.0F, 8.0F, 2.0F, new Dilation(0.0F)).uv(82, 74).cuboid(-2.5F, 2.3F, -2.5F, 5.0F, 1.0F, 5.0F, new Dilation(0.0F)).uv(76, 77).cuboid(-4.5F, -4.7F, -2.5F, 2.0F, 8.0F, 5.0F, new Dilation(0.0F)).uv(76, 77).cuboid(2.5F, -4.7F, -2.5F, 2.0F, 8.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(5.6F, 20.7F, -9.5F));
		ModelPartData stol = modelPartData.addChild("stol", ModelPartBuilder.create().uv(0, 60).cuboid(-6.0F, -5.4F, -6.0F, 12.0F, 2.0F, 12.0F, new Dilation(0.0F)).uv(18, 75).cuboid(2.0F, -3.4F, -4.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F)).uv(16, 73).cuboid(2.0F, -3.4F, 2.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F)).uv(13, 73).cuboid(-4.0F, -3.4F, 2.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F)).uv(16, 74).cuboid(-4.0F, -3.4F, -4.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-23.0F, 18.4F, -12.0F, 0.0F, -0.6545F, 0.0F));
		ModelPartData blood = modelPartData.addChild("blood", ModelPartBuilder.create().uv(24, 104).cuboid(-16.0F, -16.0F, -10.0F, 32.0F, 4.0F, 20.0F, new Dilation(0.1F)).uv(58, 108).cuboid(8.0F, -18.0F, -4.0F, 3.0F, 2.0F, 8.0F, new Dilation(0.1F)), ModelTransform.pivot(-8.0F, 24.0F, 0.0F));
		ModelPartData cube_r4 = blood.addChild("cube_r4", ModelPartBuilder.create().uv(65, 106).cuboid(-6.0F, -1.0F, -6.0F, 12.0F, 2.0F, 12.0F, new Dilation(0.1F)), ModelTransform.of(-15.0F, -10.0F, -12.0F, 0.0F, -0.6545F, 0.0F));
		ModelPartData filth = modelPartData.addChild("filth", ModelPartBuilder.create().uv(24, 30).cuboid(-24.0F, -16.0F, -10.0F, 32.0F, 4.0F, 20.0F, new Dilation(0.1F)).uv(83, 45).cuboid(0.0F, -18.0F, -4.0F, 3.0F, 2.0F, 8.0F, new Dilation(0.1F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		ModelPartData cube_r5 = filth.addChild("cube_r5", ModelPartBuilder.create().uv(31, 26).cuboid(-6.0F, -1.0F, -6.0F, 12.0F, 2.0F, 12.0F, new Dilation(0.1F)), ModelTransform.of(-23.0F, -10.0F, -12.0F, 0.0F, -0.6545F, 0.0F));

		return TexturedModelData.of(modelData, 128, 128);
	}

}
