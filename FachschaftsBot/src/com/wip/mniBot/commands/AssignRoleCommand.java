package com.wip.mniBot.commands;

import com.wip.mniBot.EventHandler;

import net.dv8tion.jda.core.entities.User;

public class AssignRoleCommand implements CommandExecutor{

	private EventHandler handler;
	
	
	public AssignRoleCommand(EventHandler handler) {
		this.handler = handler;
	}

	@Override
	public void onCommand(CommandContainer commandContainer) {
		if(commandContainer.getArgs().length == 0) {
			handler.sendWelcomeMessage(commandContainer.getMember().getUser());
		}else {
			for(User u : commandContainer.getMessage().getMentionedUsers()) {
				handler.sendWelcomeMessage(u);
			}
		}
		
		
	}

	@Override
	public String getHelpText() {
		return "!assign Um eine Nachricht vom Bot zu erhalten, um eine Rolle zuzuweisen."
				+ "\n !assign @MentionedUser um ihn die Willkommensnachricht zu senden (Mehrfachnennungen möglich)";
	}

}
