package com.wip.mniBot.commands;

public class PingCommand implements CommandExecutor{

	@Override
	public void onCommand(CommandContainer commandContainer) {
		commandContainer.getChannel().sendMessage("Pong!").queue();
		
		
	}

	@Override
	public String getHelpText() {
		return "!ping Pong!";
	}

}
