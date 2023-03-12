package dev.sterner.book_of_the_dead.api;

public class CommandType {
	public String command;
	public String type;

	public CommandType(String command, String type) {
		this.command = command;
		this.type = type;
	}

	public String getCommand() {
		return command;
	}

	public String getType() {
		return type;
	}
}
