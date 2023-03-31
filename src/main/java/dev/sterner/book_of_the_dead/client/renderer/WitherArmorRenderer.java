package dev.sterner.book_of_the_dead.client.renderer;

import dev.sterner.book_of_the_dead.common.item.WitherArmorItem;
import dev.sterner.book_of_the_dead.common.util.Constants;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class WitherArmorRenderer extends GeoArmorRenderer<WitherArmorItem> {
	public WitherArmorRenderer() {
		super(new DefaultedItemGeoModel<>(Constants.id("armor/wither_armor")));
	}
}
