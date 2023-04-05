package dev.sterner.book_of_the_dead.data;

import dev.sterner.book_of_the_dead.common.registry.BotDEnchantments;
import dev.sterner.book_of_the_dead.common.registry.BotDEntityTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import dev.sterner.book_of_the_dead.common.registry.BotDStatusEffects;
import dev.sterner.book_of_the_dead.common.util.Constants;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class BotDLanguageProvider extends FabricLanguageProvider {

	protected BotDLanguageProvider(FabricDataOutput dataOutput) {
		super(dataOutput);
	}

	@Override
	public void generateTranslations(TranslationBuilder builder) {
		builder.add(Constants.BOTD_GROUP, "The Book of the Dead");
		builder.add("death.attack.book_of_the_dead.sacrifice", "%1$s was sacrificed");
		builder.add("death.attack.book_of_the_dead.sanguine", "%1$s was drained");
		builder.add(BotDObjects.PHILOSOPHERS_STONE, "Philosopher's Stone");
		builder.add(BotDStatusEffects.SOUL_SIPHON, "Soul Siphon");
		builder.add(BotDStatusEffects.SOUL_SICKNESS, "Soul Sickness");
		builder.add(BotDObjects.EYE, "Eye");
		builder.add(BotDObjects.SULFURIC_ACID, "Sulfuric Acid");
		builder.add(BotDObjects.SULFUR, "Sulfur");
		builder.add(BotDObjects.SULFUR_PILE, "Sulfur");
		builder.add(BotDObjects.HEART, "Heart");
		builder.add(BotDObjects.BRAIN, "Brain");
		builder.add(BotDObjects.BOOK_OF_THE_DEAD, "The Book of the Dead");
		builder.add(BotDObjects.MEAT_CLEAVER, "Meat Cleaver");
		builder.add(BotDObjects.CONTRACT, "Contract");
		builder.add(BotDObjects.PACKET, "Packet");
		builder.add(BotDObjects.FAT, "Fat");
		builder.add(BotDObjects.SKIN, "Skin");
		builder.add(BotDObjects.BOTTLE_OF_BLOOD, "Bottle of Blood");
		builder.add(BotDObjects.COOKED_FLESH, "Cooked Flesh");
		builder.add(BotDObjects.FLESH, "Flesh");
		builder.add(BotDObjects.CAGE, "Cage");
		builder.add(BotDObjects.HOOK, "Hook");
		builder.add(BotDObjects.METAL_HOOK, "Metal Hook");
		builder.add(BotDObjects.ALL_BLACK, "All-Black");
		builder.add(BotDObjects.CELLAR_KEY, "Cellar Key");
		builder.add(BotDObjects.REINFORCED_DOOR, "Reinforced Door");
		builder.add(BotDObjects.REINFORCED_BLOCK, "Reinforced Block");
		builder.add(BotDObjects.OLD_LETTER, "Old Letter");
		builder.add(BotDObjects.EMERALD_TABLET, "Emerald Tablet");
		builder.add(BotDObjects.RETORT_FLASK, "Retort Flask");
		builder.add(BotDObjects.POPPY_POD, "Poppy Pod");
		builder.add(BotDObjects.POPPY_SEEDS, "Poppy Seeds");
		builder.add(BotDObjects.PAPER_AND_QUILL, "Paper And Quill");
		builder.add(BotDObjects.CARPENTER_TOOLS, "Carpenter's Tools");
		builder.add(BotDObjects.PEDESTAL, "Pedestal");
		builder.add(BotDObjects.VILLAGER_HEAD, "Villager Head");
		builder.add(BotDObjects.VILLAGER_WALL_HEAD, "Villager Head");
		builder.add(BotDObjects.DEBUG_WAND, "Debug Wand");
		builder.add(BotDObjects.SOAP, "Soap");
		builder.add(BotDObjects.SYRINGE, "Syringe");

		builder.add(BotDEnchantments.BUTCHERING, "Butchering");

		builder.add(BotDEntityTypes.OLD_MAN_ENTITY, "Old Man");
		builder.add("entity.book_of_the_dead.old_man.nitwit", "Old Man");

		builder.add(BotDObjects.JAR, "Jar");
		builder.add(BotDObjects.ROPE, "Rope");
		builder.add(BotDObjects.POPPY_CROP, "Poppy Crop");
		builder.add("rei.book_of_the_dead.butchering_drops", "Butchering Drops");
		builder.add("emi.category.book_of_the_dead.butchering", "Butchering Drops");
		builder.add("tooltip.book_of_the_dead.old_friend", "Letter to an old friend");
		builder.add("tooltip.book_of_the_dead.from_archive", "From Library Archive");
		builder.add("info.book_of_the_dead.door_locked", "Door Locked");

		builder.add("book_of_the_dead.book_of_the_dead.subtitle", "Death of the book");
		builder.add("book_of_the_dead.book_of_the_dead", "Book of the Dead");
		builder.add("book_of_the_dead.book_of_the_dead:book_of_the_dead", "Book of the Dead");
		builder.add("book_of_the_dead.book_of_the_dead.landing", "This is a book of dead things and stuff");

		builder.add(BotDStatusEffects.MORPHINE, "Morphine");
		builder.add(BotDStatusEffects.ADRENALINE, "Adrenaline");
		builder.add(BotDStatusEffects.EUTHANASIA, "Euthanasia");
		builder.add(BotDStatusEffects.SANGUINE, "Sanguine Infection");

		builder.add("book_of_the_dead.gui.book.page.headline.butcher", "Butchering");

		builder.add("book_of_the_dead.gui.book.page.text.text", "This is Text text text txtx txtx txtx txtx txtx txtx txtx");
		builder.add("book_of_the_dead.gui.book.page.text.butcher.1", "Butchering is a fun fun fun fun fun fun thing");
		builder.add("book_of_the_dead.gui.book.page.text.test2", "This is a Text 2");

		builder.add("book_of_the_dead.gui.book.page.text.empty", "");
		builder.add("book_of_the_dead.gui.book.page.text.main.1", "Welcome");
		builder.add("book_of_the_dead.gui.book.page.headline.main", "Book of the Dead");
		builder.add("book_of_the_dead.gui.book.page.headline.glossary", "Glossary");
	}
}
