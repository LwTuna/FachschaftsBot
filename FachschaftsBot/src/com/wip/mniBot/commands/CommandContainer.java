package com.wip.mniBot.commands;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class CommandContainer {

	private String raw;
	private String[] args;
	private Member member;
	private JDA jda;
	private TextChannel channel;
	private Message message;
	
	public CommandContainer(String raw, String[] args, Member member, JDA jda, TextChannel channel,Message message) {
		this.raw = raw;
		this.args = args;
		this.member = member;
		this.jda = jda;
		this.channel = channel;
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}
	
	public String getRaw() {
		return raw;
	}

	public String[] getArgs() {
		return args;
	}

	public Member getMember() {
		return member;
	}

	public JDA getJda() {
		return jda;
	}

	public TextChannel getChannel() {
		return channel;
	}
	
	
	
	
	
}
