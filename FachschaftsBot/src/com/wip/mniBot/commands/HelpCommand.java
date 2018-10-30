package com.wip.mniBot.commands;

import java.util.Map.Entry;

import com.wip.mniBot.EventHandler;

import net.dv8tion.jda.core.EmbedBuilder;

public class HelpCommand implements CommandExecutor{

	private EventHandler eventHandler;
	
	
	
	public HelpCommand(EventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}

	@Override
	public void onCommand(CommandContainer commandContainer) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("Help");
		for(Entry<String,CommandExecutor> entry:eventHandler.getCommands().entrySet()) {
			String desc = entry.getValue().getHelpText();
			if(desc == null)
				continue;
			builder.appendDescription(desc+"\n");
		}
		commandContainer.getChannel().sendMessage(builder.build()).queue();
	}

	@Override
	public String getHelpText() {
		// TODO Auto-generated method stub
		return null;
	}

}
