package com.wip.mniBot.commands;

public interface CommandExecutor {

	
	
	public void onCommand(CommandContainer commandContainer);
	
	public String getHelpText();
}
