package dev.sterner.book_of_the_dead.api;

import com.mojang.brigadier.ParseResults;
import dev.sterner.book_of_the_dead.common.block.entity.RitualBlockEntity;
import dev.sterner.book_of_the_dead.common.recipe.RitualRecipe;
import dev.sterner.book_of_the_dead.common.registry.BotDObjects;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class NecrotableRitual {
	private final Identifier id;
	public BlockPos ritualCenter = null;
	public RitualRecipe recipe;
	public List<Entity> summons = new ArrayList<>();
	public World world = null;
	public int ticker = 0;
	public UUID user = null;
	public int height = 0;

	public NecrotableRitual(Identifier id) {
		this.id = id;
	}

	public void tick(World world, BlockPos blockPos, RitualBlockEntity blockEntity) {
		ticker++;
		MinecraftServer minecraftServer = world.getServer();
		for (CommandType commandType : blockEntity.ritualRecipe.command) {
			if (commandType.type.equals("tick")) {
				runCommand(minecraftServer, blockPos, commandType.command);
			}
		}
	}

	public void onStopped(World world, BlockPos blockPos, RitualBlockEntity blockEntity){
		ticker = 0;
		MinecraftServer minecraftServer = world.getServer();
		for (CommandType commandType : blockEntity.ritualRecipe.command) {
			if (commandType.type.equals("end")) {
				runCommand(minecraftServer, blockPos, commandType.command);
			}
		}
	}

	public void onStart(World world, BlockPos blockPos, RitualBlockEntity blockEntity){
		if(world.getBlockState(blockPos).isOf(BotDObjects.NECRO_TABLE)){
			if(this.world == null){
				this.world = world;
			}
			ritualCenter = blockPos.add(0.5,0.5,0.5);
			MinecraftServer minecraftServer = world.getServer();
			for (CommandType commandType : blockEntity.ritualRecipe.command) {
				if (commandType.type.equals("start")) {
					runCommand(minecraftServer, blockPos, commandType.command);
				}
			}
		}
	}

	public Identifier getId() {
		return id;
	}

	//TODO test this
	public void runCommand(MinecraftServer minecraftServer, BlockPos blockPos, String command) {
		if (minecraftServer != null && !command.isEmpty()) {
			String posString = blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ();
			String parsedCommand = command.replaceAll("\\{pos}", posString);
			ServerCommandSource commandSource = minecraftServer.getCommandSource();
			CommandManager commandManager = minecraftServer.getCommandManager();
			ParseResults<ServerCommandSource> parseResults = commandManager.getDispatcher().parse(parsedCommand, commandSource);
			commandManager.execute(parseResults, parsedCommand);
		}
	}
}
