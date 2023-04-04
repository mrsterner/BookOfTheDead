package dev.sterner.book_of_the_dead.common.item;

import dev.sterner.book_of_the_dead.client.renderer.WitherArmorRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class WitherArmorItem extends ArmorItem implements GeoItem {
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

	public WitherArmorItem(ArmorSlot slot, Settings settings) {
		super(WitherArmor.WITHER, slot, settings);
	}

	@Override
	public void createRenderer(Consumer<Object> consumer) {
		consumer.accept(new RenderProvider() {
			private GeoArmorRenderer<?> renderer;

			@Override
			public @NotNull BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<LivingEntity> original) {
				if (this.renderer == null) {
					this.renderer = new WitherArmorRenderer();
				}

				this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

				return this.renderer;
			}
		});
	}

	@Override
	public Supplier<Object> getRenderProvider() {
		return this.renderProvider;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, 20, state -> {
			state.getController().setAnimation(DefaultAnimations.IDLE);

			return PlayState.CONTINUE;
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}

	public enum WitherArmor implements ArmorMaterial {
		WITHER("book_of_the_dead:wither", 16, new int[]{1, 3, 4, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, Items.WITHER_SKELETON_SKULL, 0),
		SKELETON("book_of_the_dead:skeleton", 12, new int[]{1, 2, 3, 2}, 12, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, Items.WITHER_SKELETON_SKULL, 0);

		private final String name;
		private final int durabilityMultiplier;
		private final int[] damageReduction;
		private final int enchantability;
		private final SoundEvent equipSound;
		private final Item repairItem;
		private final float toughness;
		private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};

		WitherArmor(String name, int durabilityMultiplier, int[] damageReduction, int enchantability, SoundEvent equipSound, Item repairItem, float toughness) {
			this.name = name;
			this.durabilityMultiplier = durabilityMultiplier;
			this.damageReduction = damageReduction;
			this.enchantability = enchantability;
			this.equipSound = equipSound;
			this.repairItem = repairItem;
			this.toughness = toughness;
		}

		@Override
		public int getDurability(ArmorSlot slot) {
			return durabilityMultiplier * MAX_DAMAGE_ARRAY[slot.getEquipmentSlot().getEntitySlotId()];
		}

		@Override
		public int getProtection(ArmorSlot slot) {
			return damageReduction[slot.getEquipmentSlot().getEntitySlotId()];
		}

		@Override
		public int getEnchantability() {
			return enchantability;
		}

		@Override
		public SoundEvent getEquipSound() {
			return equipSound;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.ofItems(repairItem);
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public float getToughness() {
			return toughness;
		}

		@Override
		public float getKnockbackResistance() {
			return 0;
		}
	}
}
