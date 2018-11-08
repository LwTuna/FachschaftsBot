package com.wip.mniBot.commands;
/**
 * The CommandExecutor interface for every Command used
 * Implement a New Command and put it in the EventHandler commands HashMap in the Constructor with the command(String) as Key and an instance as its value 
 * @author Jonas
 */
public interface CommandExecutor {

	
	
	public void onCommand(CommandContainer commandContainer);
	
	public String getHelpText();
}
