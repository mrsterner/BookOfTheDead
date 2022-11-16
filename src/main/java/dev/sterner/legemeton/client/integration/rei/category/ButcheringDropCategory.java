package dev.sterner.legemeton.client.integration.rei.category;

import dev.sterner.legemeton.client.integration.rei.LegemetonREIPlugin;
import dev.sterner.legemeton.client.integration.rei.display.ButcheringDropDisplay;
import dev.sterner.legemeton.common.registry.LegemetonObjects;
import dev.sterner.legemeton.common.util.Constants;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ButcheringDropCategory implements DisplayCategory<ButcheringDropDisplay> {
	public static final Text TITLE = Text.translatable("rei.legemeton.butchering_drops");
	public static final EntryStack<ItemStack> ICON = EntryStacks.of(LegemetonObjects.BLOODY_BUTCHER_KNIFE);

	@Override
	public Renderer getIcon() {
		return ICON;
	}

	@Override
	public Text getTitle() {
		return TITLE;
	}

	@Override
	public int getDisplayHeight() {
		return 36;
	}

	@Override
	public CategoryIdentifier<? extends ButcheringDropDisplay> getCategoryIdentifier() {
		return LegemetonREIPlugin.BUTCHERING_DROPS;
	}

	@Override
	public List<Widget> setupDisplay(ButcheringDropDisplay display, Rectangle bounds) {
		Entity target = display.entityType.create(MinecraftClient.getInstance().world);
		Rectangle targetBounds = new Rectangle(bounds.getMinX(), bounds.getCenterY() - 27, 54, 54);

		Point startPoint = new Point(bounds.getCenterX() - 64, bounds.getCenterY() - 8);

		List<Widget> widgets = new ArrayList<>();
		bounds.grow(5,5);
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createDrawableWidget((helper, matrices, mouseX, mouseY, delta) -> {
			if (target != null) {
				createEntityWidget(matrices, mouseX, mouseY, delta, target, targetBounds);
			}
		}));

		widgets.add(Widgets.createTexturedWidget(Constants.id("textures/gui/icons/butchering.png"), startPoint.x + 50, startPoint.y - 14, 0, 0, 32, 32, 16, 16, 16, 16));

		widgets.add(Widgets.createSlot(new Point(startPoint.x + 84, startPoint.y)).entries(EntryIngredients.ofIngredient((Ingredient.ofStacks(display.outputList.get(0))))).markInput());
		if (display.outputList.size() > 1) {
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 18 + 84, startPoint.y)).entries(EntryIngredients.ofIngredient((Ingredient.ofStacks(display.outputList.get(1))))).markInput());
			if (display.outputList.size() > 2) {
				widgets.add(Widgets.createSlot(new Point(startPoint.x + 36 + 84, startPoint.y)).entries(EntryIngredients.ofIngredient((Ingredient.ofStacks(display.outputList.get(2))))).markInput());
				if (display.outputList.size() > 3) {
					widgets.add(Widgets.createSlot(new Point(startPoint.x + 84, startPoint.y + 18)).entries(EntryIngredients.ofIngredient((Ingredient.ofStacks(display.outputList.get(3))))).markInput());
					if (display.outputList.size() > 4) {
						widgets.add(Widgets.createSlot(new Point(startPoint.x + 18 + 84, startPoint.y + 18)).entries(EntryIngredients.ofIngredient((Ingredient.ofStacks(display.outputList.get(4))))).markInput());
						if (display.outputList.size() > 5) {
							widgets.add(Widgets.createSlot(new Point(startPoint.x + 36 + 84, startPoint.y + 18)).entries(EntryIngredients.ofIngredient((Ingredient.ofStacks(display.outputList.get(5))))).markInput());
							if (display.outputList.size() > 6) {
								widgets.add(Widgets.createSlot(new Point(startPoint.x + 18 + 84, startPoint.y + 36)).entries(EntryIngredients.ofIngredient((Ingredient.ofStacks(display.outputList.get(6))))).markInput());
								if (display.outputList.size() > 7) {
									widgets.add(Widgets.createSlot(new Point(startPoint.x + 36 + 84, startPoint.y + 36)).entries(EntryIngredients.ofIngredient((Ingredient.ofStacks(display.outputList.get(7))))).markInput());
								}
							}
						}
					}
				}
			}
		}
		return widgets;
	}

	private void createEntityWidget(MatrixStack matrices, int mouseX, int mouseY, float delta, Entity entity, Rectangle bounds) {
		float f = (float) Math.atan((bounds.getCenterX() - mouseX) / 40.0F);
		float g = (float) Math.atan((bounds.getCenterY() - mouseY) / 40.0F);
		float size = 32;
		if (Math.max(entity.getWidth(), entity.getHeight()) > 1.0) {
			size /= Math.max(entity.getWidth(), entity.getHeight());
		}

		matrices.push();
		matrices.translate(bounds.getCenterX(), bounds.getCenterY() + 20, 1050.0);
		matrices.scale(1, 1, -1);
		matrices.translate(0.0D, 0.0D, 1000.0D);
		matrices.scale(size, size, size);
		Quaternion quaternion = Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F);
		Quaternion quaternion2 = Vec3f.POSITIVE_X.getDegreesQuaternion(g * 20.0F);
		quaternion.hamiltonProduct(quaternion2);
		matrices.multiply(quaternion);
		float i = entity.getYaw();
		float j = entity.getPitch();
		float h = 0, k = 0, l = 0;
		entity.setYaw(180.0F + f * 40.0F);
		entity.setPitch(-g * 20.0F);

		if (entity instanceof LivingEntity living) {
			h = living.bodyYaw;
			k = living.prevHeadYaw;
			l = living.headYaw;
			living.bodyYaw = 180.0F + f * 20.0F;
			living.headYaw = entity.getYaw();
			living.prevHeadYaw = entity.getYaw();
		}

		EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
		quaternion2.conjugate();
		entityRenderDispatcher.setRotation(quaternion2);
		entityRenderDispatcher.setRenderShadows(false);
		VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
		entityRenderDispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrices, immediate, 15728880);
		immediate.draw();
		entityRenderDispatcher.setRenderShadows(false);
		entity.setYaw(i);
		entity.setPitch(j);

		if (entity instanceof LivingEntity living) {
			living.bodyYaw = h;
			living.prevHeadYaw = k;
			living.headYaw = l;
		}

		matrices.pop();
	}
}
