package com.wip.mniBot.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

public class MemberCommand implements CommandExecutor{

	@Override
	public void onCommand(CommandContainer commandContainer) {
		String usersOnline = "";
		for(Member m :commandContainer.getChannel().getGuild().getMembers()) {
			usersOnline += m.getAsMention()+" ";
		}
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("Members Online :");
		builder.setDescription(usersOnline);
		commandContainer.getChannel().sendMessage(builder.build()).queue();
	}

	@Override
	public String getHelpText() {
		return "get a List of Current Members.";
	}

}
