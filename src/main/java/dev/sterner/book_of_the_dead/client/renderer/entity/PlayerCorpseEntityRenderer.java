package dev.sterner.book_of_the_dead.client.renderer.entity;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sterner.book_of_the_dead.common.entity.PlayerCorpseEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.Map;

public class PlayerCorpseEntityRenderer extends LivingEntityRenderer<PlayerCorpseEntity, BipedEntityModel<PlayerCorpseEntity>> {
	private final BipedEntityModel normalModel;
	private final BipedEntityModel slimModel;

	public PlayerCorpseEntityRenderer(EntityRendererFactory.Context ctx) {
		super(ctx, new BipedEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER)), 0.5F);
		this.normalModel = this.getModel();
		this.slimModel = new BipedEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER_SLIM));
	}


	@Override
	public void render(PlayerCorpseEntity livingEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		if(livingEntity.getSkinProfile() != null){
			PlayerEntity player = livingEntity.getWorld().getPlayerByUuid(livingEntity.getSkinProfile().getId());

			if (player instanceof AbstractClientPlayerEntity && DefaultSkinHelper.getModel(player.getUuid()).equals("slim")) {
				this.model = slimModel;
			} else {
				this.model = normalModel;
			}
		}
		matrixStack.push();
		this.model.child = livingEntity.isBaby();
		float bodyYaw = MathHelper.lerpAngleDegrees(tickDelta, livingEntity.prevBodyYaw, livingEntity.bodyYaw);
		float animationProgress = this.getAnimationProgress(livingEntity, tickDelta);
		this.setupTransforms(livingEntity, matrixStack, animationProgress, bodyYaw, tickDelta);
		matrixStack.scale(-1.0F, -1.0F, 1.0F);
		this.scale(livingEntity, matrixStack, tickDelta);
		matrixStack.translate(0.0, -1.501F, 0.0);

		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		boolean bl = this.isVisible(livingEntity);
		boolean bl2 = !bl && !livingEntity.isInvisibleTo(minecraftClient.player);
		boolean bl3 = minecraftClient.hasOutline(livingEntity);
		RenderLayer renderLayer = this.getRenderLayer(livingEntity, bl, bl2, bl3);
		if (renderLayer != null) {
			VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
			int p = getOverlay(livingEntity, this.getAnimationCounter(livingEntity, tickDelta));
			this.model.render(matrixStack, vertexConsumer, light, p, 1.0F, 1.0F, 1.0F, bl2 ? 0.15F : 1.0F);
		}

		matrixStack.pop();
	}



	@Override
	public Identifier getTexture(PlayerCorpseEntity entity) {
		if(entity.getSkinProfile() != null){
			MinecraftClient minecraftClient = MinecraftClient.getInstance();
			Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraftClient.getSkinProvider().getTextures(entity.getSkinProfile());
			if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
				return minecraftClient.getSkinProvider().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
			}else{
				PlayerEntity player = entity.world.getPlayerByUuid(entity.getSkinProfile().getId());
				if(player instanceof AbstractClientPlayerEntity abstractClientPlayerEntity){
					return abstractClientPlayerEntity.getSkinTexture();
				}
			}

		}
		return new Identifier("textures/entity/steve.png");
	}
}
