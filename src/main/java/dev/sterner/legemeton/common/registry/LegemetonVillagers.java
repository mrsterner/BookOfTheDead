package dev.sterner.legemeton.common.registry;

import com.google.common.collect.ImmutableMap;
import dev.sterner.legemeton.common.registry.LegemetonObjects;
import dev.sterner.legemeton.common.util.Constants;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import org.quiltmc.qsl.points_of_interest.api.PointOfInterestHelper;

public class LegemetonVillagers {
	private static final Identifier OLD_MAN_POI_ID = Constants.id("old_man_poi");
	public static final RegistryKey<PointOfInterestType> OLD_MAN_POI = PointOfInterestHelper.register(
			OLD_MAN_POI_ID,
			1,
			1,
			LegemetonObjects.NECRO_TABLE
	);

	public static final VillagerProfession OLD_MAN = VillagerProfessionBuilder.create()
			.id(Constants.id("old_man"))
			.workstation(RegistryKey.of(Registry.POINT_OF_INTEREST_TYPE_KEY, OLD_MAN_POI_ID))
			.workSound(SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM)
			.build();

	public static void init() {
		Registry.register(Registry.VILLAGER_PROFESSION, Constants.id("old_man"), OLD_MAN);
		TradeOffers.PROFESSION_TO_LEVELED_TRADE.put(
				OLD_MAN, new Int2ObjectOpenHashMap<>(ImmutableMap.of(
						1,
						new TradeOffers.Factory[]{
								new TradeOffers.BuyForOneEmeraldFactory(LegemetonObjects.LEGEMETON, 20, 1, 2)
						}

        )));
	}
}
