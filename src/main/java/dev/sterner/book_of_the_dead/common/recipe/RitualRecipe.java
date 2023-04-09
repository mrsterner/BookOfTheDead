package dev.sterner.book_of_the_dead.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.sterner.book_of_the_dead.api.CommandType;
import dev.sterner.book_of_the_dead.api.interfaces.IRecipe;
import dev.sterner.book_of_the_dead.common.registry.BotDRecipeTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDRegistries;
import dev.sterner.book_of_the_dead.common.rituals.BasicNecrotableRitual;
import dev.sterner.book_of_the_dead.common.util.Constants;
import dev.sterner.book_of_the_dead.common.util.RecipeUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.recipe.api.serializer.QuiltRecipeSerializer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record RitualRecipe(Identifier id,
						   BasicNecrotableRitual ritual,
						   Identifier texture,
						   boolean requireBotD,
						   boolean requireEmeraldTablet,
						   boolean isSpecial,
						   int duration,
						   @Nullable DefaultedList<Ingredient> inputs,
						   @Nullable List<ItemStack> outputs,
						   @Nullable List<EntityType<?>> sacrifices,
						   @Nullable List<EntityType<?>> summons,
						   @Nullable List<StatusEffectInstance> statusEffectInstance,
						   Set<CommandType> command) implements IRecipe {

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return BotDRecipeTypes.RITUAL_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return BotDRecipeTypes.RITUAL_RECIPE_TYPE;
	}

	@Override
	public boolean matches(Inventory inventory, World world) {
		return matches(inventory, inputs, sacrifices);
	}

	public static boolean matches(Inventory inv, DefaultedList<Ingredient> input, List<EntityType<?>> sacrifices) {
		List<ItemStack> checklist = new ArrayList<>();
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if (!stack.isEmpty()) {
				checklist.add(stack);
			}
		}
		if (input.size() != checklist.size()) {
			return false;
		}
		for (Ingredient ingredient : input) {
			boolean found = false;
			for (ItemStack stack : checklist) {
				if (ingredient.test(stack)) {
					found = true;
					checklist.remove(stack);
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
	}

	public static class Serializer implements QuiltRecipeSerializer<RitualRecipe> {

		@Override
		public RitualRecipe read(Identifier id, JsonObject json) {
			//Ritual
			BasicNecrotableRitual ritual = BotDRegistries.NECROTABLE_RITUALS.get(new Identifier(JsonHelper.getString(json, "ritual")));

			//Texture
			Identifier texture;
			if (JsonHelper.hasString(json, "texture")) {
				texture = new Identifier(JsonHelper.getString(json, "texture"));
			} else {
				texture = Constants.id("book_of_the_dead:misc/circle_necromancy.png");
			}

			boolean requireBotD = JsonHelper.getBoolean(json, "requireBotD", false);
			boolean requireEmeraldTablet = JsonHelper.getBoolean(json, "requireEmeraldTablet", false);
			boolean isSpecial = JsonHelper.getBoolean(json, "isSpecial", false);

			//Inputs
			DefaultedList<Ingredient> inputs = DefaultedList.of();
			if (JsonHelper.hasArray(json, "inputs")) {
				inputs = RecipeUtils.deserializeIngredients(JsonHelper.getArray(json, "inputs"));
			}

			//Outputs
			DefaultedList<ItemStack> outputs = DefaultedList.of();
			if (JsonHelper.hasArray(json, "outputs")) {
				outputs = RecipeUtils.deserializeStacks(JsonHelper.getArray(json, "outputs"));
			}

			//Sacrifices
			List<EntityType<?>> sacrifices = List.of();
			if (JsonHelper.hasArray(json, "summons")) {
				var sacrificeArray = JsonHelper.getArray(json, "sacrifices");
				sacrifices = RecipeUtils.deserializeEntityTypes(sacrificeArray);
			}

			//Summons
			List<EntityType<?>> summons = List.of();
			if (JsonHelper.hasArray(json, "summons")) {
				var summonArray = JsonHelper.getArray(json, "summons");
				summons = RecipeUtils.deserializeEntityTypes(summonArray);
			}

			//Duration
			int duration = JsonHelper.getInt(json, "duration", 20 * 8);

			//StatusEffect
			List<StatusEffectInstance> statusEffectInstanceList = List.of();
			if (JsonHelper.hasArray(json, "statusEffects")) {
				var statusList = JsonHelper.getArray(json, "statusEffects");
				statusEffectInstanceList = RecipeUtils.deserializeStatusEffects(statusList);
			}


			//Command
			Set<CommandType> commands = Set.of();
			if (JsonHelper.hasArray(json, "commands")) {
				JsonArray commandArray = JsonHelper.getArray(json, "commands");
				commands = RecipeUtils.deserializeCommands(commandArray);
			}

			return new RitualRecipe(id, ritual, texture, requireBotD, requireEmeraldTablet, isSpecial, duration, inputs, outputs, sacrifices, summons, statusEffectInstanceList, commands);
		}


		@Override
		public RitualRecipe read(Identifier id, PacketByteBuf buf) {
			//Ritual
			BasicNecrotableRitual rite = BotDRegistries.NECROTABLE_RITUALS.get(buf.readIdentifier());

			Identifier texture = buf.readIdentifier();

			boolean requireBotD = buf.readBoolean();
			boolean requireEmeraldTablet = buf.readBoolean();
			boolean isSpecial = buf.readBoolean();

			//Inputs
			DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readVarInt(), Ingredient.EMPTY);
			inputs.replaceAll(ignored -> Ingredient.fromPacket(buf));

			//Outputs
			DefaultedList<ItemStack> outputs = DefaultedList.ofSize(buf.readVarInt(), ItemStack.EMPTY);
			outputs.replaceAll(ignored -> buf.readItemStack());

			//Sacrifices
			int sacrificeSize = buf.readInt();
			List<EntityType<?>> sacrificeList = IntStream.range(0, sacrificeSize).mapToObj(i -> Registries.ENTITY_TYPE.get(new Identifier(buf.readString()))).collect(Collectors.toList());

			//Summons
			int summonsSize = buf.readInt();
			List<EntityType<?>> summons = IntStream.range(0, summonsSize).mapToObj(i -> Registries.ENTITY_TYPE.get(new Identifier(buf.readString()))).collect(Collectors.toList());

			//Duration
			int duration = buf.readInt();

			//StatusEffect
			int effectSize = buf.readInt();
			List<StatusEffectInstance> statusEffectInstanceList = IntStream.range(0, effectSize).mapToObj(i -> {
				StatusEffect effect = Registries.STATUS_EFFECT.get(new Identifier(buf.readString()));
				if (effect != null) {
					return new StatusEffectInstance(effect, buf.readInt(), buf.readInt());
				}
				return null;
			}).collect(Collectors.toList());

			//Commands
			Set<CommandType> commandTypeSet = new HashSet<>();
			for (int i = 0; i < buf.readInt(); i++) {
				commandTypeSet.add(new CommandType(buf.readString(), buf.readString()));
			}

			return new RitualRecipe(id, rite, texture, requireBotD, requireEmeraldTablet, isSpecial, duration, inputs, outputs, sacrificeList, summons, statusEffectInstanceList, commandTypeSet);
		}

		@Override
		public void write(PacketByteBuf buf, RitualRecipe recipe) {
			buf.writeIdentifier(recipe.ritual.getId());

			buf.writeIdentifier(recipe.texture);

			buf.writeBoolean(recipe.requireBotD);
			buf.writeBoolean(recipe.requireEmeraldTablet);
			buf.writeBoolean(recipe.isSpecial);

			//Inputs
			buf.writeVarInt(recipe.inputs.size());
			recipe.inputs.forEach(ingredient -> ingredient.write(buf));
			//Outputs
			buf.writeVarInt(recipe.outputs.size());
			recipe.outputs.forEach(buf::writeItemStack);

			//Sacrifices
			buf.writeInt(recipe.sacrifices.size());
			recipe.sacrifices.stream().map(entityType -> Registries.ENTITY_TYPE.getId(entityType).toString()).forEach(buf::writeString);

			//Summons
			buf.writeInt(recipe.summons.size());
			recipe.summons.stream().map(entityType -> Registries.ENTITY_TYPE.getId(entityType).toString()).forEach(buf::writeString);

			//Duration
			buf.writeInt(recipe.duration);

			//Effects
			buf.writeVarInt(recipe.statusEffectInstance.size());
			recipe.statusEffectInstance.forEach(effectInstance -> {
				buf.writeString(Registries.STATUS_EFFECT.getId(effectInstance.getEffectType()).toString());
				buf.writeInt(effectInstance.getDuration());
				buf.writeInt(effectInstance.getAmplifier());
			});

			//Commands
			buf.writeVarInt(recipe.command.size());
			recipe.command.forEach(commandType -> {
				buf.writeString(commandType.command());
				buf.writeString(commandType.type());
			});
		}

		@Override
		public JsonObject toJson(RitualRecipe recipe) {
			return null;
		}
	}
}
