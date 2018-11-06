package com.wip.mniBot.commands;

import java.util.Random;

public class RandomCommand implements CommandExecutor{

	@Override
	public void onCommand(CommandContainer commandContainer) {
		Random random = new Random();
		if(commandContainer.getArgs().length == 0) {
			commandContainer.getChannel().sendMessage("Your Random Number : "+random.nextInt(100)+1).queue();;
			return;
		}
		try {
			int i = Integer.parseInt(commandContainer.getArgs()[0]);
			
			if(commandContainer.getArgs().length == 1) {
				commandContainer.getChannel().sendMessage("Your Random Number : "+random.nextInt(i)+1).queue();
			}else
				commandContainer.getChannel().sendMessage("Your Random Number : "+random.nextInt(i)+Integer.parseInt(commandContainer.getArgs()[1])).queue();
		}catch(Exception e) {
			
		}
	}

	@Override
	public String getHelpText() {
		return "get a Random Number(1-100).\n"
				+ "!random 69(replaceable with every valid Integer) get a Random Number between 1 - 69.\n"
				+ "!random 69 -1337  get a random Number between 69 -1337.";
	}

}
