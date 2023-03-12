package dev.sterner.book_of_the_dead.client.renderer.shader;


import com.mojang.blaze3d.vertex.VertexFormats;
import com.mojang.datafixers.util.Pair;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.resource.ResourceManager;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class BotDShaders {
	private static ShaderProgram ritual;

	public static void init(ResourceManager resourceManager, List<Pair<ShaderProgram, Consumer<ShaderProgram>>> registrations) throws IOException {
		registrations.add(Pair.of(
				new ShaderProgram(resourceManager, Constants.MOD_ID + ":ritual", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL),
				inst -> ritual = inst)
		);
	}
	public static ShaderProgram ritual() {
		return ritual;
	}
}
