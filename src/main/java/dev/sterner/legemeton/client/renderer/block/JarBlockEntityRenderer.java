package dev.sterner.legemeton.client.renderer.block;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.legemeton.client.model.JarEntityModel;
import dev.sterner.legemeton.common.block.JarBlock;
import dev.sterner.legemeton.common.block.entity.JarBlockEntity;
import dev.sterner.legemeton.common.registry.LegemetonObjects;
import dev.sterner.legemeton.common.registry.LegemetonSpriteIdentifiers;
import dev.sterner.legemeton.common.util.Constants;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.impl.client.indigo.renderer.helper.ColorHelper;
import net.fabricmc.fabric.impl.renderer.RendererAccessImpl;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JarBlockEntityRenderer implements BlockEntityRenderer<JarBlockEntity>, BuiltinItemRendererRegistry.DynamicItemRenderer {
	private static final float EDGE_SIZE = 1f / 8f;
	private static final float INNER_SIZE = 1f - (EDGE_SIZE * 2f);
	public static final int BLOOD_COLOR = 0xff0000;
	private final JarEntityModel jarEntityModel;
	private final Identifier TEXTURE = Constants.id("textures/block/jar.png");

	public JarBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		jarEntityModel = new JarEntityModel<>(ctx.getLayerModelPart(JarEntityModel.LAYER_LOCATION));
	}

	@Override
	public void render(JarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		float g = 0.5F;
		matrices.scale(g,g,g);
		matrices.translate(0.5, 0.05, 0.5);
		renderFluid(matrices, vertexConsumers, light, overlay, entity, entity.bloodAmount / 100F);
		matrices.pop();


		matrices.push();
		float f = 0.5F;
		matrices.translate(f,3 * f + 0.0001,f);
		matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
		matrices.translate(0,1,0);
		if(entity.getWorld() != null && entity.getWorld().getBlockState(entity.getPos()).isOf(LegemetonObjects.JAR) && entity.getWorld().getBlockState(entity.getPos()).get(JarBlock.OPEN)){
			jarEntityModel.renderNoCap(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)),light, overlay, 1,1,1,1);
		}else{
			jarEntityModel.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)),light, overlay, 1,1,1,1);
		}
		matrices.pop();
	}

	@Override
	public void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

	}

	private void renderFluid(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, @Nullable JarBlockEntity entity, float percent) {
		if (RendererAccessImpl.INSTANCE.getRenderer() != null) {
			percent = Math.min(1, percent);
			matrices.push();
			Sprite sprite;
			MeshBuilder builder = RendererAccessImpl.INSTANCE.getRenderer().meshBuilder();
			int newColor;
			if (entity != null) {
				newColor = ColorHelper.swapRedBlueIfNeeded(BLOOD_COLOR);
				sprite = LegemetonSpriteIdentifiers.BLOOD.getSprite();
					emitFluidFace(builder.getEmitter(), sprite, newColor, Direction.UP, 1f, (1f - percent), EDGE_SIZE, INNER_SIZE);
					emitFluidFace(builder.getEmitter(), sprite, newColor, Direction.DOWN, 1f, 0f, EDGE_SIZE, INNER_SIZE);
					emitFluidFace(builder.getEmitter(), sprite, newColor, Direction.NORTH, percent, 0f, EDGE_SIZE, INNER_SIZE);
					emitFluidFace(builder.getEmitter(), sprite, newColor, Direction.EAST, percent, 0f, EDGE_SIZE, INNER_SIZE);
					emitFluidFace(builder.getEmitter(), sprite, newColor, Direction.SOUTH, percent, 0f, EDGE_SIZE, INNER_SIZE);
					emitFluidFace(builder.getEmitter(), sprite, newColor, Direction.WEST, percent, 0f, EDGE_SIZE, INNER_SIZE);

				renderMesh(builder.build(), matrices, vertexConsumers.getBuffer(RenderLayer.getTranslucent()), light, overlay);
			}
			matrices.pop();
		}
	}

	public static void renderMesh(Mesh mesh, MatrixStack matrices, VertexConsumer consumer, int light, int overlay) {
		for (List<BakedQuad> bakedQuads : ModelHelper.toQuadLists(mesh)) {
			for (BakedQuad bq : bakedQuads) {
				consumer.complexBakedQuad(matrices.peek(), bq, new float[]{1f, 1f, 1f, 1f}, 1f, 1f, 1f, new int[]{light, light, light, light}, overlay, true);
			}
		}
	}

	public static void emitFluidFace(QuadEmitter emitter, Sprite sprite, int color, Direction direction, float height, float depth, float EDGE_SIZE, float INNER_SIZE) {
		emitter.square(direction, EDGE_SIZE, EDGE_SIZE, (1f - EDGE_SIZE), (EDGE_SIZE + (height * INNER_SIZE)), EDGE_SIZE + (depth * INNER_SIZE));
		emitter.spriteBake(0, sprite, MutableQuadView.BAKE_ROTATE_NONE);
		emitter.spriteColor(0, color, color, color, color);
		emitter.sprite(0, 0, sprite.getMinU() + EDGE_SIZE * (sprite.getMaxU() - sprite.getMinU()), sprite.getMinV() + (1f - (EDGE_SIZE + (height * INNER_SIZE))) * (sprite.getMaxV() - sprite.getMinV()));
		emitter.sprite(1, 0, sprite.getMinU() + EDGE_SIZE * (sprite.getMaxU() - sprite.getMinU()), sprite.getMinV() + (1f - EDGE_SIZE) * (sprite.getMaxV() - sprite.getMinV()));
		emitter.sprite(2, 0, sprite.getMinU() + (1f - EDGE_SIZE) * (sprite.getMaxU() - sprite.getMinU()), sprite.getMinV() + (1f - EDGE_SIZE) * (sprite.getMaxV() - sprite.getMinV()));
		emitter.sprite(3, 0, sprite.getMinU() + (1f - EDGE_SIZE) * (sprite.getMaxU() - sprite.getMinU()), sprite.getMinV() + (1f - (EDGE_SIZE + (height * INNER_SIZE))) * (sprite.getMaxV() - sprite.getMinV()));
		emitter.emit();
	}
}
