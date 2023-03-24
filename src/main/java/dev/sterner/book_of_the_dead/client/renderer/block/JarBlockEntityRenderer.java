package dev.sterner.book_of_the_dead.client.renderer.block;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.BotDClient;
import dev.sterner.book_of_the_dead.client.model.BrainEntityModel;
import dev.sterner.book_of_the_dead.client.model.JarEntityModel;
import dev.sterner.book_of_the_dead.common.block.JarBlock;
import dev.sterner.book_of_the_dead.common.block.entity.JarBlockEntity;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.registry.BotDSpriteIdentifiers;
import dev.sterner.book_of_the_dead.common.util.Constants;
import dev.sterner.book_of_the_dead.common.util.RenderUtils;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.impl.client.indigo.renderer.helper.ColorHelper;
import net.fabricmc.fabric.impl.renderer.RendererAccessImpl;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import static dev.sterner.book_of_the_dead.common.block.entity.JarBlockEntity.*;

public class JarBlockEntityRenderer implements BlockEntityRenderer<JarBlockEntity>, BuiltinItemRendererRegistry.DynamicItemRenderer {
	private static final float EDGE_SIZE = 1f / 8f;
	private static final float INNER_SIZE = 1f - (EDGE_SIZE * 2f);

	public static final int BLOOD_COLOR = 0xff0000;
	public static final int WATER_COLOR = 0x3f76e4;

	private final JarEntityModel jarEntityModel = new JarEntityModel<>(JarEntityModel.createBodyLayer().createModel());
	private final BrainEntityModel brainEntityModel = new BrainEntityModel<>(BrainEntityModel.createBodyLayer().createModel());

	private final Identifier TEXTURE = Constants.id("textures/block/jar.png");
	private final Identifier TEXTURE_ITEM = Constants.id("textures/block/jar_item.png");



	@Override
	public void render(JarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if(entity.hasBrain){
			matrices.push();
			matrices.translate(0.5, -1.0, 0.5);
			float ticks = (BotDClient.ClientTickHandler.ticksInGame + tickDelta) * 0.05f % 360;
			matrices.translate(0,0.1 * MathHelper.sin(ticks),0);
			VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(BrainBlockEntityRenderer.TEXTURE));
			brainEntityModel.render(matrices, buffer, light, overlay, 1,1,1,1);
			matrices.pop();
		}
		if(entity.liquidAmount > 0){
			matrices.push();
			matrices.scale(0.55f, 0.75f, 0.55f);
			matrices.translate(0.4, -0.05, 0.4);
			renderFluid(matrices, vertexConsumers, light, overlay, entity,null, (entity.liquidAmount / 100F), entity.liquidType);
			matrices.pop();
		}

		matrices.push();
		float f = 0.5F;
		matrices.translate(f,3 * f + 0.0001,f);
		matrices.multiply(Axis.Z_POSITIVE.rotationDegrees(180));
		matrices.translate(0,1,0);
		if(entity.getWorld() != null && entity.getWorld().getBlockState(entity.getPos()).isOf(BotDObjects.JAR) && entity.getWorld().getBlockState(entity.getPos()).get(JarBlock.OPEN)){
			jarEntityModel.renderNoCap(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)),light, overlay, 1,1,1,1);
		}else{
			jarEntityModel.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)),light, overlay, 1,1,1,1);
		}
		matrices.pop();
	}

	@Override
	public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		NbtCompound nbtCompound = new NbtCompound();
		if(stack.getNbt() != null) {
			nbtCompound = stack.getNbt().getCompound("BlockEntityTag");
		}

		if(nbtCompound.contains(Constants.Nbt.BRAIN) && nbtCompound.getBoolean(Constants.Nbt.BRAIN)){
			matrices.push();
			matrices.scale(0.85f, 0.85f, 0.85f);
			matrices.translate(0, -1.55, 0);
			VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(BrainBlockEntityRenderer.TEXTURE));
			brainEntityModel.render(matrices, buffer, light, overlay, 1,1,1,1);
			matrices.pop();
		}

		if(nbtCompound.contains(Constants.Nbt.LIQUID_LEVEL) && nbtCompound.getInt(Constants.Nbt.LIQUID_LEVEL) > 0 && nbtCompound.contains(Constants.Nbt.LIQUID_TYPE) && nbtCompound.getByte(Constants.Nbt.LIQUID_TYPE) != EMPTY){
			matrices.scale(0.55f,0.75f,0.55f);
			matrices.translate(-0.5, -0.75, -0.5);
			renderFluid(matrices, vertexConsumers, light, overlay, null, stack,  nbtCompound.getInt(Constants.Nbt.LIQUID_LEVEL) / 100F, nbtCompound.getByte(Constants.Nbt.LIQUID_TYPE));
		}
		matrices.pop();



		matrices.push();
		matrices.multiply(Axis.Z_POSITIVE.rotationDegrees(180));
		VertexConsumer vertexConsumer = ItemRenderer.getItemGlintConsumer(vertexConsumers, jarEntityModel.getLayer(TEXTURE_ITEM), false, stack.hasGlint());
		jarEntityModel.render(matrices, vertexConsumer, light, overlay, 1, 1, 1, 1);
		matrices.pop();
	}

	private void renderFluid(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, @Nullable JarBlockEntity entity, @Nullable ItemStack itemStack, float percent, int liquidType) {
		if (RendererAccessImpl.INSTANCE.getRenderer() != null) {
			percent = Math.min(1, percent);
			matrices.push();
			Sprite sprite;
			MeshBuilder builder = RendererAccessImpl.INSTANCE.getRenderer().meshBuilder();
			int newColor;
			if (entity != null || itemStack != null) {
				if(liquidType == ACID){
					newColor = ColorHelper.swapRedBlueIfNeeded(0xfff999);
					sprite = BotDSpriteIdentifiers.WATER_ALPHA.getSprite();
				}else if(liquidType == BLOOD){
					newColor = ColorHelper.swapRedBlueIfNeeded(BLOOD_COLOR);
					sprite = BotDSpriteIdentifiers.BLOOD.getSprite();
				}else{
					newColor = ColorHelper.swapRedBlueIfNeeded(WATER_COLOR);
					sprite = BotDSpriteIdentifiers.WATER_ALPHA.getSprite();
				}

				RenderUtils.emitFluidFace(builder.getEmitter(), sprite, newColor, Direction.UP, 1f, (1f - percent), EDGE_SIZE, INNER_SIZE);
				RenderUtils.emitFluidFace(builder.getEmitter(), sprite, newColor, Direction.DOWN, 1f, 0f, EDGE_SIZE, INNER_SIZE);
				RenderUtils.emitFluidFace(builder.getEmitter(), sprite, newColor, Direction.NORTH, percent, 0f, EDGE_SIZE, INNER_SIZE);
				RenderUtils.emitFluidFace(builder.getEmitter(), sprite, newColor, Direction.EAST, percent, 0f, EDGE_SIZE, INNER_SIZE);
				RenderUtils.emitFluidFace(builder.getEmitter(), sprite, newColor, Direction.SOUTH, percent, 0f, EDGE_SIZE, INNER_SIZE);
				RenderUtils.emitFluidFace(builder.getEmitter(), sprite, newColor, Direction.WEST, percent, 0f, EDGE_SIZE, INNER_SIZE);

				RenderUtils.renderMesh(builder.build(), matrices, vertexConsumers.getBuffer(RenderLayer.getTranslucent()), light, overlay);
			}
			matrices.pop();
		}
	}


}
