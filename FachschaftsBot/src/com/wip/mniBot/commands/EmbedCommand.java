package com.wip.mniBot.commands;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

public class EmbedCommand implements CommandExecutor{

	@Override
	public void onCommand(CommandContainer commandContainer) {
		
		List<TextChannel> channels = commandContainer.getMessage().getMentionedChannels();
		TextChannel channel;
		if(channels.size() >= 0) {
			channel = commandContainer.getChannel();
		}else {
			channel = channels.get(0);
		}
		String message = "";
		if(commandContainer.getArgs().length <=1)
			return;
		for(int i = 1;i<commandContainer.getArgs().length;i++) {
			message += commandContainer.getArgs()[i] +" ";
		}
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("Message from :"+commandContainer.getMember().getAsMention());
		builder.setDescription(message);
		channel.sendMessage(builder.toString()).queue();
	}

	@Override
	public String getHelpText() {
		return "!embed #mentionedChannel Message     to send a Embeded Message at the specific Channel. \n"
				+ "!embed Message      to send a Embed Message in the current Channel.";
	}

	
	
}
