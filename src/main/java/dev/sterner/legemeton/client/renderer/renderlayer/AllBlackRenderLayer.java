package dev.sterner.legemeton.client.renderer.renderlayer;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import dev.sterner.legemeton.common.item.AllBlackSwordItem;
import dev.sterner.legemeton.common.registry.LegemetonObjects;
import dev.sterner.legemeton.common.util.Constants;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.item.ItemStack;

public class AllBlackRenderLayer extends RenderLayer {
	private static final ThreadLocal<ItemStack> targetStack = new ThreadLocal<>();

	public static void setTargetStack(ItemStack stack) {
		targetStack.set(stack);
	}

	public static boolean checkAllBlack() {
		ItemStack target = targetStack.get();
		return target != null && !target.isEmpty() && (target.isOf(LegemetonObjects.ALL_BLACK) || target.isOf(LegemetonObjects.NECROSWORD));
	}

	public static RenderLayer glintColor = AllBlackRenderLayer.buildGlintRenderLayer();
	public static RenderLayer glintDirectColor = AllBlackRenderLayer.buildGlintDirectRenderLayer();

	public AllBlackRenderLayer(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
	}

	public static void addGlintTypes(Object2ObjectLinkedOpenHashMap<RenderLayer, BufferBuilder> map) {
		addGlintTypes(map, glintColor);
		addGlintTypes(map, glintDirectColor);
	}

	public static void addGlintTypes(Object2ObjectLinkedOpenHashMap<RenderLayer, BufferBuilder> map, RenderLayer renderLayer) {
			if (!map.containsKey(renderLayer)){
				map.put(renderLayer, new BufferBuilder(renderLayer.getExpectedBufferSize()));
			}
	}

	private static RenderLayer buildGlintRenderLayer() {
		return RenderLayer.of("glint_black", VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, MultiPhaseParameters.builder()
				.shader(RenderPhase.GLINT_SHADER)
				.texture(new Texture(Constants.id("textures/misc/all_black_glint.png"), true, false))
				.writeMaskState(COLOR_MASK)
				.cull(DISABLE_CULLING)
				.depthTest(EQUAL_DEPTH_TEST)
				.transparency(GLINT_TRANSPARENCY)
				.texturing(GLINT_TEXTURING)
				.build(false));
	}

	private static RenderLayer buildGlintDirectRenderLayer() {
		return RenderLayer.of("glint_direct_black", VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, MultiPhaseParameters.builder()
				.shader(RenderPhase.DIRECT_GLINT_SHADER)
				.texture(new Texture(Constants.id("textures/misc/all_black_glint.png"), true, false))
				.writeMaskState(COLOR_MASK)
				.cull(DISABLE_CULLING)
				.depthTest(EQUAL_DEPTH_TEST)
				.transparency(GLINT_TRANSPARENCY)
				.texturing(GLINT_TEXTURING)
				.build(false));
	}

	@Environment(EnvType.CLIENT)
	public static RenderLayer getGlint() {
		return checkAllBlack() ? AllBlackRenderLayer.glintColor : RenderLayer.getGlint();
	}

	@Environment(EnvType.CLIENT)
	public static RenderLayer getGlintDirect() {
		return checkAllBlack() ? AllBlackRenderLayer.glintDirectColor : RenderLayer.getDirectGlint();
	}
}
