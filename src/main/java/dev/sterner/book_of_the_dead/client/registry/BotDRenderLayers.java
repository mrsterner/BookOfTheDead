package dev.sterner.book_of_the_dead.client.registry;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.util.Constants;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.function.Function;

public class BotDRenderLayers extends RenderLayer {
	private static final ThreadLocal<ItemStack> targetStack = new ThreadLocal<>();

	public static void setTargetStack(ItemStack stack) {
		targetStack.set(stack);
	}

	public static boolean checkAllBlack() {
		ItemStack target = targetStack.get();
		return target != null && !target.isEmpty() && (target.isOf(BotDObjects.ALL_BLACK));
	}

	public static RenderLayer glintColor = BotDRenderLayers.buildGlintRenderLayer();
	public static RenderLayer glintDirectColor = BotDRenderLayers.buildGlintDirectRenderLayer();

	public BotDRenderLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
	}

	public static void addGlintTypes(Object2ObjectLinkedOpenHashMap<RenderLayer, BufferBuilder> map) {
		addGlintTypes(map, glintColor);
		addGlintTypes(map, glintDirectColor);
	}

	public static void addGlintTypes(Object2ObjectLinkedOpenHashMap<RenderLayer, BufferBuilder> map, RenderLayer renderLayer) {
		if (!map.containsKey(renderLayer)) {
			map.put(renderLayer, new BufferBuilder(renderLayer.getExpectedBufferSize()));
		}
	}

	public static final RenderLayer FX = RenderLayer.MultiPhase.of(
		"fx",
		VertexFormats.POSITION_COLOR,
		VertexFormat.DrawMode.QUADS,
		256,
		true,
		true,
		RenderLayer.MultiPhaseParameters.builder()
			.shader(RenderPhase.LIGHTNING_SHADER)
			.transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
			.target(RenderPhase.OUTLINE_TARGET)
			.build(false)
	);

	public static final Function<Identifier, RenderLayer> GLOWING_LAYER = Util.memoize(texture -> {
		MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder()
			.texture(new RenderPhase.Texture(texture, false, false))
			.transparency(TRANSLUCENT_TRANSPARENCY)
			.cull(DISABLE_CULLING)
			.lightmap(ENABLE_LIGHTMAP)
			.overlay(ENABLE_OVERLAY_COLOR)
			.shader(ENERGY_SWIRL_SHADER)
			.build(true);
		return RenderLayer.of("glowing_layer", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, true, true, multiPhaseParameters);
	});

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


	@ClientOnly
	public static RenderLayer getGlint() {
		return checkAllBlack() ? BotDRenderLayers.glintColor : RenderLayer.getGlint();
	}

	@ClientOnly
	public static RenderLayer getGlintDirect() {
		return checkAllBlack() ? BotDRenderLayers.glintDirectColor : RenderLayer.getDirectGlint();
	}
}
