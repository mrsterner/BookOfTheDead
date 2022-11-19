package dev.sterner.book_of_the_dead.client.renderer.renderlayer;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.util.Constants;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import ladysnake.satin.mixin.client.render.RenderLayerAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class BotDRenderLayer extends RenderLayer {
	private static final ThreadLocal<ItemStack> targetStack = new ThreadLocal<>();

	public static void setTargetStack(ItemStack stack) {
		targetStack.set(stack);
	}

	public static boolean checkAllBlack() {
		ItemStack target = targetStack.get();
		return target != null && !target.isEmpty() && (target.isOf(BotDObjects.ALL_BLACK));
	}

	public static RenderLayer glintColor = BotDRenderLayer.buildGlintRenderLayer();
	public static RenderLayer glintDirectColor = BotDRenderLayer.buildGlintDirectRenderLayer();

	public BotDRenderLayer(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
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

	public static RenderLayer get(Identifier texture) {
		RenderLayer.MultiPhaseParameters multiPhaseParameters =
				RenderLayer.MultiPhaseParameters.builder()
						.texture(new RenderPhase.Texture(texture, false, false))
						.transparency(Transparency.TRANSLUCENT_TRANSPARENCY)
						.cull(DISABLE_CULLING)
						.lightmap(ENABLE_LIGHTMAP)
						.overlay(DISABLE_OVERLAY_COLOR)
						.layering(VIEW_OFFSET_Z_LAYERING)
						.shader(ENERGY_SWIRL_SHADER).build(true);
		return RenderLayerAccessor.satin$of("glowing", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, false, multiPhaseParameters);
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
		return checkAllBlack() ? BotDRenderLayer.glintColor : RenderLayer.getGlint();
	}

	@Environment(EnvType.CLIENT)
	public static RenderLayer getGlintDirect() {
		return checkAllBlack() ? BotDRenderLayer.glintDirectColor : RenderLayer.getDirectGlint();
	}
}
