package dev.sterner.book_of_the_dead.api.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

public abstract class BotDParticle extends SpriteBillboardParticle {

	protected BotDParticle(ClientWorld clientWorld, double d, double e, double f) {
		super(clientWorld, d, e, f);
	}

	protected BotDParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
		super(clientWorld, d, e, f, g, h, i);
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		Vec3d vec3d = camera.getPos();
		float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
		Quaternionf quaternionf;
		if (this.angle == 0.0F) {
			quaternionf = camera.getRotation();
		} else {
			quaternionf = new Quaternionf(camera.getRotation());
			quaternionf.rotateZ(MathHelper.lerp(tickDelta, this.prevAngle, this.angle));
		}

		Vector3f[] vector3fs = new Vector3f[]{
			new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)
		};
		float i = this.getSize(tickDelta);

		for (int j = 0; j < 4; ++j) {
			Vector3f vector3f = vector3fs[j];
			vector3f.rotate(quaternionf);
			vector3f.mul(i);
			vector3f.add(f, g, h);
		}

		float k = this.getMinU();
		float l = this.getMaxU();
		float m = this.getMinV();
		float n = this.getMaxV();
		int o = 15728880;

		vertexConsumer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).uv(l, n).color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha).light(o).next();
		vertexConsumer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).uv(l, m).color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha).light(o).next();
		vertexConsumer.vertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).uv(k, m).color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha).light(o).next();
		vertexConsumer.vertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).uv(k, n).color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha).light(o).next();
	}
}
