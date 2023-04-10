package dev.sterner.book_of_the_dead.client.renderer.armor;

import dev.sterner.book_of_the_dead.common.item.WitherArmorItem;
import dev.sterner.book_of_the_dead.common.util.Constants;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class SkeletonArmorRenderer extends GeoArmorRenderer<WitherArmorItem> {
	public SkeletonArmorRenderer() {
		super(new DefaultedItemGeoModel<>(Constants.id("armor/skeleton_armor")));
	}
}
