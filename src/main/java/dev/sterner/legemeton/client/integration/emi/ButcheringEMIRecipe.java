package dev.sterner.legemeton.client.integration.emi;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.sterner.legemeton.client.integration.rei.category.ButcheringDropCategory;
import dev.sterner.legemeton.common.recipe.ButcheringRecipe;
import dev.sterner.legemeton.common.registry.LegemetonObjects;
import dev.sterner.legemeton.common.util.Constants;
import dev.sterner.legemeton.common.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ButcheringEMIRecipe implements EmiRecipe {
	private final Identifier id;
	public final EntityType<?> entityType;
	private final List<EmiIngredient> input;
	private final List<EmiStack> output;
	public final DefaultedList<ItemStack> outputList;

	public ButcheringEMIRecipe(ButcheringRecipe recipe) {
		this.id = recipe.getId();
		entityType = recipe.entity_type;
		List<ItemStack> itemStackList = recipe.getOutputs().stream().map(Pair::getFirst).toList();
		DefaultedList<ItemStack> defaultedList = DefaultedList.copyOf(ItemStack.EMPTY, itemStackList.toArray(new ItemStack[0]));
		this.outputList = defaultedList;
		this.input = List.of(EmiStack.of(new ItemStack(Items.AIR)));


		List<EmiStack> list = new ArrayList<>();
		//recipe.getIngredients().forEach(ingredient -> list.add(EmiIngredient.of(ingredient)));
		defaultedList.forEach(itemStack -> list.add(EmiStack.of(itemStack)));
		this.output = list;
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return LegemetonEMIPlugin.BUTCHERING_CATEGORY;
	}

	@Override
	public @Nullable Identifier getId() {
		return id;
	}

	@Override
	public List<EmiIngredient> getInputs() {
		return input;
	}

	@Override
	public List<EmiStack> getOutputs() {
		return output;
	}

	@Override
	public int getDisplayWidth() {
		return 76 + 18;
	}

	@Override
	public int getDisplayHeight() {
		if(output.size() > 5){
			return 18 * 4;
		}
		if (output.size() > 3) {
			return 18 * 3;
		}
		return 18 + 18;
	}

	@Override
	public void addWidgets(WidgetHolder widgetHolder) {
		Entity target = entityType.create(MinecraftClient.getInstance().world);


		widgetHolder.addDrawable(36,18, 64, 59, (matrices, mouseX, mouseY, delta) -> {
			RenderSystem.setShaderTexture(0, Constants.id("textures/gui/icons/butchering.png"));
			DrawableHelper.drawTexture(matrices, 0, 0, 0, 0, 16, 16, 16, 16);
		});

		widgetHolder.addDrawable(18,18,20,20, (matrices, mouseX, mouseY, delta) -> {
			if (target instanceof LivingEntity livingEntity) {
				RenderUtils.drawEntity(18,18, 20, mouseX, mouseY, livingEntity, null);
			}
		});
		if (output.size() > 0) {
			widgetHolder.addSlot(output.get(0), 58, 0).recipeContext(this);
			if (output.size() > 1) {
				widgetHolder.addSlot(output.get(1), 58 + 18, 0).recipeContext(this);
				if (output.size() > 2) {
					widgetHolder.addSlot(output.get(2), 58, 18).recipeContext(this);
					if (output.size() > 3) {
						widgetHolder.addSlot(output.get(3), 58 + 18, 18).recipeContext(this);
						if (output.size() > 4) {
							widgetHolder.addSlot(output.get(4), 58, 18 + 18).recipeContext(this);
							if (output.size() > 5) {
								widgetHolder.addSlot(output.get(5), 58 + 18 , 18 + 18).recipeContext(this);
								if (output.size() > 6) {
									widgetHolder.addSlot(output.get(6), 58, 18 + 18 + 18).recipeContext(this);
									if (output.size() > 7) {
										widgetHolder.addSlot(output.get(7), 58 + 18, 18 + 18 + 18).recipeContext(this);
									}
								}
							}
						}
					}
				}
			}
		}


	}


}
