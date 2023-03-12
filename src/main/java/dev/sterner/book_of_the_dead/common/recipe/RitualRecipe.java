package dev.sterner.book_of_the_dead.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.sterner.book_of_the_dead.api.CommandType;
import dev.sterner.book_of_the_dead.api.NecrotableRitual;
import dev.sterner.book_of_the_dead.api.interfaces.IRecipe;
import dev.sterner.book_of_the_dead.common.registry.BotDRecipeTypes;
import dev.sterner.book_of_the_dead.common.registry.BotDRegistries;
import dev.sterner.book_of_the_dead.common.util.RecipeUtils;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.server.command.EnchantCommand;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.recipe.api.serializer.QuiltRecipeSerializer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RitualRecipe implements Recipe<Inventory> {
	public final Identifier id;
	public final NecrotableRitual ritual;

	public final DefaultedList<Ingredient> inputs;
	public final List<ItemStack> outputs;
	public final List<EntityType<?>> summons;
	public final List<EntityType<?>> sacrifices;
	public final int duration;
	public final Set<CommandType> command;

	public final boolean requireBotD;
	public final boolean requireEmeraldTablet;
	public final List<StatusEffectInstance> statusEffectInstance;

	public RitualRecipe(Identifier id, NecrotableRitual ritual, boolean requireBotD, boolean requireEmeraldTablet, int duration, @Nullable DefaultedList<Ingredient> inputs, @Nullable List<ItemStack> outputs, @Nullable List<EntityType<?>> sacrifices, @Nullable List<EntityType<?>> summons, @Nullable List<StatusEffectInstance> statusEffectInstance, Set<CommandType> command) {
		this.id = id;
		this.outputs = outputs;
		this.inputs = inputs;
		this.sacrifices = sacrifices;
		this.duration = duration;
		this.command = command;
		this.ritual = ritual;
		this.requireBotD = requireBotD;
		this.requireEmeraldTablet = requireEmeraldTablet;
		this.statusEffectInstance = statusEffectInstance;
		this.summons = summons;
	}

	public int getDuration(){
		return this.duration;
	}

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
	public ItemStack craft(Inventory inventory) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getOutput() {
		return ItemStack.EMPTY;
	}

	public static boolean matches(Inventory inv, DefaultedList<Ingredient> input) {
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

	public static class Serializer implements QuiltRecipeSerializer<RitualRecipe> {

		@Override
		public RitualRecipe read(Identifier id, JsonObject json) {
			//Ritual
			NecrotableRitual ritual = BotDRegistries.NECROTABLE_RITUALS.get(new Identifier(JsonHelper.getString(json, "ritual")));
			boolean requireBotD = JsonHelper.getBoolean(json, "requireBotD", false);
			boolean requireEmeraldTablet = JsonHelper.getBoolean(json, "requireEmeraldTablet", false);

			//Inputs
			DefaultedList<Ingredient> inputs = RecipeUtils.deserializeIngredients(JsonHelper.getArray(json, "inputs"));

			//Outputs
			DefaultedList<ItemStack> outputs = RecipeUtils.deserializeStacks(JsonHelper.getArray(json, "outputs"));

			//Sacrifices
			var sacrificeArray = JsonHelper.getArray(json, "sacrifices");
			List<EntityType<?>> sacrifices = RecipeUtils.deserializeEntityTypes(sacrificeArray);

			//Summons
			var summonArray = JsonHelper.getArray(json, "summons");
			List<EntityType<?>> summons = RecipeUtils.deserializeEntityTypes(summonArray);

			//Duration
			int duration = JsonHelper.getInt(json, "duration", 20 * 8);

			//StatusEffect
			var statusList = JsonHelper.getArray(json, "statusEffects");
			List<StatusEffectInstance> statusEffectInstanceList = RecipeUtils.deserializeStatusEffects(statusList);

			//Command
			JsonArray commandArray = JsonHelper.getArray(json, "commands");
			Set<CommandType> commands = RecipeUtils.deserializeCommands(commandArray);

			return new RitualRecipe(id, ritual, requireBotD, requireEmeraldTablet, duration, inputs, outputs, sacrifices, summons, statusEffectInstanceList, commands);
		}



		@Override
		public RitualRecipe read(Identifier id, PacketByteBuf buf) {
			//Ritual
			NecrotableRitual rite = BotDRegistries.NECROTABLE_RITUALS.get(buf.readIdentifier());
			boolean requireBotD = buf.readBoolean();
			boolean requireEmeraldTablet = buf.readBoolean();

			//Inputs
			DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readVarInt(), Ingredient.EMPTY);
			inputs.replaceAll(ignored -> Ingredient.fromPacket(buf));

			//Outputs
			DefaultedList<ItemStack> outputs = DefaultedList.ofSize(buf.readVarInt(), ItemStack.EMPTY);
			outputs.replaceAll(ignored -> buf.readItemStack());

			//Sacrifices
			int sacrificeSize = buf.readInt();
			List<EntityType<?>> sacrificeList = IntStream.range(0, sacrificeSize).mapToObj(i -> Registry.ENTITY_TYPE.get(new Identifier(buf.readString()))).collect(Collectors.toList());

			//Summons
			int summonsSize = buf.readInt();
			List<EntityType<?>> summons = IntStream.range(0, summonsSize).mapToObj(i -> Registry.ENTITY_TYPE.get(new Identifier(buf.readString()))).collect(Collectors.toList());

			//Duration
			int duration = buf.readInt();

			//StatusEffect
			int effectSize = buf.readInt();
			List<StatusEffectInstance> statusEffectInstanceList = IntStream.range(0, effectSize).mapToObj(i -> {
				StatusEffect effect = Registry.STATUS_EFFECT.get(new Identifier(buf.readString()));
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

			return new RitualRecipe(id, rite, requireBotD, requireEmeraldTablet, duration, inputs, outputs, sacrificeList, summons, statusEffectInstanceList, commandTypeSet);
		}

		@Override
		public void write(PacketByteBuf buf, RitualRecipe recipe) {
			buf.writeIdentifier(recipe.ritual.getId());
			buf.writeBoolean(recipe.requireBotD);
			buf.writeBoolean(recipe.requireEmeraldTablet);

			//Inputs
			buf.writeVarInt(recipe.inputs.size());
			for (Ingredient ingredient : recipe.inputs) {
				ingredient.write(buf);
			}
			//Outputs
			buf.writeVarInt(recipe.outputs.size());
			for (ItemStack stack : recipe.outputs) {
				buf.writeItemStack(stack);
			}

			//Sacrifices
			buf.writeInt(recipe.sacrifices.size());
			for (EntityType<?> entityType : recipe.sacrifices) {
				buf.writeString(Registry.ENTITY_TYPE.getId(entityType).toString());
			}

			//Summons
			buf.writeInt(recipe.summons.size());
			for (EntityType<?> entityType : recipe.summons) {
				buf.writeString(Registry.ENTITY_TYPE.getId(entityType).toString());
			}

			//Duration
			buf.writeInt(recipe.duration);

			//Effects
			buf.writeVarInt(recipe.statusEffectInstance.size());
			for (StatusEffectInstance effectInstance : recipe.statusEffectInstance) {
				buf.writeString(Registry.STATUS_EFFECT.getId(effectInstance.getEffectType()).toString());
				buf.writeInt(effectInstance.getDuration());
				buf.writeInt(effectInstance.getAmplifier());
			}

			//Commands
			buf.writeVarInt(recipe.command.size());
			for (CommandType commandType : recipe.command) {
				buf.writeString(commandType.getCommand());
				buf.writeString(commandType.getType());
			}
		}

		@Override
		public JsonObject toJson(RitualRecipe recipe) {
			return null;
		}
	}
}
