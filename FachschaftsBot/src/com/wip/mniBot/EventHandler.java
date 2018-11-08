package com.wip.mniBot;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.json.JSONObject;

import com.wip.mniBot.commands.AssignRoleCommand;
import com.wip.mniBot.commands.CommandContainer;
import com.wip.mniBot.commands.CommandExecutor;
import com.wip.mniBot.commands.EmbedCommand;
import com.wip.mniBot.commands.HelpCommand;
import com.wip.mniBot.commands.MemberCommand;
import com.wip.mniBot.commands.PingCommand;
import com.wip.mniBot.database.DatabaseHandler;
import com.wip.mniBot.database.UserEntry;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildAvailableEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.priv.react.PrivateMessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
* This is the Event Handler for all of the used ListenerAdapter event methods.
* @version 0.1
* @author Jonas Reitz
*/
public class EventHandler extends ListenerAdapter{

	
	//Instance of the Bot class used for getting constants
	private Bot bot;
	
	//The User database
	private JSONObject database;
	
	//Name of the Bachelor and Master Role on the Discord Server
	private final String bachelorRoleName = "Bachelorstudent" , masterRoleName = "Masterstudent";
	
	//Assigned Roles , only initialized when the Discord Server is connected with the Bot
	private Role bachelorRole;
	private Role masterRole;
	
	//Unicode for the B and M Emoji used as a reaction of the welcome message
	private final String bUnicode = "\uD83C\uDDE7",mUnicode = "\uD83C\uDDF2";
	
	//Welcome Message Send to new Users to get their prefered Server Roles
	private final String WELCOME_MESSAGE = "Um dir auf diesem Discord Server eine Rolle zuzuweisen \n"
			+ "wähle bitte ob du eine Bachelor Student :regional_indicator_b: oder ein Master Student :regional_indicator_m: "
			+ "bist und wähle dies hier Unten oder antworte mit einem B oder M auf diese Nachricht.n"
			+ "Falls du dich verklickt hast kannst du ganz einfach die Reaktion entfernen und eine andere wählen. :wink:";
	
	//Mapping the command keyword to an instance of a CommandExecutor
	private Map<String,CommandExecutor> commands = new HashMap<String,CommandExecutor>();
	//Default CommandExecutor when no command is found
	private CommandExecutor commandNotFoundExecutor;
	
	/**
	 * Constructor of the EventHandler class
	 * Loads the JSON Database
	 * Creates Instances of the CommandExecutors and puts it in the commands Map
	 * @param bot used for getting constants and the jda object
	 */
	public EventHandler(Bot bot) {
		this.bot = bot;
		try {
			database = DatabaseHandler.loadDatabaseFromFile("res/users.json");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		commandNotFoundExecutor = new CommandExecutor() {
			
			@Override
			public void onCommand(CommandContainer commandContainer) {
				commandContainer.getChannel().sendMessage("Command +"+commandContainer.getRaw()+" not found...").queue();;
			}
			
			@Override
			public String getHelpText() {
				return null;
			}
		};
		commands.put("help", new HelpCommand(this));
		commands.put("ping", new PingCommand());
		commands.put("assign", new AssignRoleCommand(this));
		commands.put("embed", new EmbedCommand());
		commands.put("members", new MemberCommand());
	}

	/**
	 * Logs if the Bot is Ready
	 * @param event information about the ready event
	 */
	@Override
	public void onReady(ReadyEvent event) {
		System.out.println("Bot Started!");
		onGuildAvailable(new GuildAvailableEvent(event.getJDA(),0 , event.getJDA().getGuilds().get(0)));
	}
	
	/**
	 * Called when a new Guild become available to the Bot. Try to set the Bachelor & Master Role, logs if it failed to initialized the Roles
	 * @param event contains information about the event, guild that become available
	 */
	@Override
	public void onGuildAvailable(GuildAvailableEvent event) {
		System.out.println("Guild : "+event.getGuild().getName()+" is available");
		bachelorRole = event.getGuild().getRolesByName(bachelorRoleName, true).get(0);
		masterRole = event.getGuild().getRolesByName(masterRoleName, true).get(0);
		
		if(bachelorRole == null || masterRole == null) {
			System.out.println("Failed to initialize Roles on the Server :"+event.getGuild().getName());
		}
	}
	/**
	 * Called whenever a new User joined a Guild the Bot is connected with
	 * Sends call the sendWelcomeMessage method with the new User as a parameter
	 * @param event contains the User information needed to send him/her a message
	 */
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		sendWelcomeMessage(event.getMember());
	}
	
	/**
	 * Called whenever a message is written on a connecte Server
	 * If the message starts with a ! executes Command stuff
	 * Parse the message and creates a CommandContainer with the information
	 * Search for a MapEntry in the commands map and call the onCommand method of the mapped CommandExecutor instance
	 * if no entry is found execute the d
	 * 
	 */
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

		if(event.getMessage().getContentRaw().startsWith("!")) {
			String raw = event.getMessage().getContentRaw();
			String[] splited = raw.split("\\s+");
			if(raw.length()<=1)
				return;
			String key = splited[0].substring(1);
			String[] args;
			if(splited.length <= 1) {
				args = new String[0];
			}else {
				args=new String[splited.length-1];
				for(int i=1;i<splited.length;i++) {
					args[i-1] = splited[i];
				}
			}
			commands.getOrDefault(key, commandNotFoundExecutor).onCommand(new CommandContainer(raw, args, event.getMember(), event.getJDA(), event.getChannel(),event.getMessage()));
		}
	}	
	/**
	 * This Method is called when a reaction is added to a message in a private channel
	 * returns when the reaction is added by the bot itself
	 * checks if the reaction is a B or M emote and calls the assignRole method with bachelor or master role
	 * @param event Keeps information about the reaction event
	 */
	@Override
	public void onPrivateMessageReactionAdd(PrivateMessageReactionAddEvent event) {
		if(event.getUser().isBot()){
			return;
		}
		
		String emoteUnicode = event.getReactionEmote().getName();
		if(emoteUnicode.equalsIgnoreCase(bUnicode)) {
			assignRole(event.getJDA(), event.getUser(), true, false,event.getChannel());
		}else if(emoteUnicode.equalsIgnoreCase(mUnicode)) {
			assignRole(event.getJDA(), event.getUser(), false, true,event.getChannel());
		}
	}
	/**
	 * This Method is called when a reaction is remove to a message in a private channel
	 * returns when the reaction is added by the bot itself
	 * checks if the reaction is a B or M emote and calls the removeRole method with bachelor or master role
	 * @param event Keeps information about the reaction event
	 */
	@Override
	public void onPrivateMessageReactionRemove(PrivateMessageReactionRemoveEvent event) {
		String emoteUnicode = event.getReactionEmote().getName();
		Member m = event.getJDA().getGuildById(bot.getMniServerID()).getMember(event.getUser());
		
		if(emoteUnicode.equalsIgnoreCase(bUnicode)) {
			removeRole(event.getJDA(), event.getUser(), true, false,event.getChannel());
		}else if(emoteUnicode.equalsIgnoreCase(mUnicode)) {
			removeRole(event.getJDA(), event.getUser(), false, true,event.getChannel());
		}
	}
	/**
	 * This Method is called when a message is send in private channel
	 * returns when the reaction is added by the bot itself
	 * checks if the message is a B/Bacherlor or M/Master emote and calls the assignRole method with bachelor or master role
	 * @param event Keeps information about the reaction event
	 */
	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		if(event.getAuthor().isBot()) {
			return;
		}
		
		if(event.getMessage().getContentRaw().equalsIgnoreCase("b") || event.getMessage().getContentRaw().equalsIgnoreCase("bachelor")) {
			assignRole(event.getJDA(), event.getAuthor(), true, false,event.getChannel());	
		}
		if(event.getMessage().getContentRaw().equalsIgnoreCase("m") || event.getMessage().getContentRaw().equalsIgnoreCase("master")) {
			assignRole(event.getJDA(), event.getAuthor(), false, true,event.getChannel());	
		}
		
	}
	
	/**
	 * This Method return if the given user has the given role on the server.
	 * 
	 * @param m the member to check if he/her has the role
	 * @param roleName the role name to check
	 * @return whether the member has the given role or not
	 */
	private boolean hasRole(Member m,String roleName) {
		for(Role r : m.getRoles()) {
			if(r.getName().equalsIgnoreCase(roleName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This Method removes a role from the given user.
	 * If the user does not have the given role it does nothing
	 * Logs if the Guild is not initialized yet	
	 * 
	 * @param jda The JDA instance of the Bot
	 * @param user the User the role should be removed from
	 * @param bachelor if bachelor Role should be removed
	 * @param master if master Role should be removed
	 * @param channel the private message channel to send a response
	 */
	private void removeRole(JDA jda,User user,boolean bachelor,boolean master,PrivateChannel channel) {
		Guild mni = jda.getGuildById(bot.getMniServerID());
		if(mni == null) {
			System.out.println("MNI Guild not initialized yet.");
			return;
		}
		Member member = mni.getMember(user);
		if(member == null) {
			System.out.println("User not part of the guild tried to assign a Role");
			return;
		}
		
		//if bachelor selected and if user is already owner of the role
		if(bachelor && hasRole(member, bachelorRoleName)) {
			
			mni.getController().removeRolesFromMember(member, bachelorRole).queue(new Consumer<Void>() {
				
				@Override
				public void accept(Void t) {
					channel.sendMessage("Die Rolle BachelorStudent wurde dir entfernt.").queue();
					
				}
			});
		}
		//if master selected and if user is already owner of the role
		if(master && hasRole(member, masterRoleName)) {
			mni.getController().removeRolesFromMember(member, masterRole).queue(new Consumer<Void>() {
				
				@Override
				public void accept(Void t) {
					channel.sendMessage("Die Rolle MasterStudent wurde dir entfernt.").queue();
					
				}
			});
		}
	}
	/**
	 * 
	 * This Method assigns a Role to the given User.
	 * If the selected Role is alread assigned it does nothing
	 * If the other possible role is already assigned it removes it and assigns the selected Role
	 * Logs if the Guild is not initialized yet
	 * 
	 * @param jda The Jda instance of the Bot
	 * @param user The User the role should be assigned to
	 * @param bachelor if bacherlor role should be assigned
	 * @param master if master role should be assigned
	 * @param channel the private channel to send response on this action
	 */
	private void assignRole(JDA jda,User user,boolean bachelor,boolean master,PrivateChannel channel) {
		
		
		Guild mni = jda.getGuildById(bot.getMniServerID());
		if(mni == null) {
			System.out.println("MNI Guild not initialized yet.");
			return;
		}
		Member member = mni.getMember(user);
		if(member == null) {
			System.out.println("User not part of the guild tried to assign a Role");
			return;
		}
		
		if(bachelor && !hasRole(member, bachelorRoleName)) {
			mni.getController().addRolesToMember(member, bachelorRole).queue(new Consumer<Void>() {
				
				@Override
				public void accept(Void t) {
					channel.sendMessage("Die Rolle BachelorStudent wurde dir erfolgreich zugewiesen.").queue();
					removeRole(jda, user,false, true, channel);
				}
			});
			
		}
		if(master && !hasRole(member, masterRoleName)) {
			mni.getController().addRolesToMember(member, masterRole).queue(new Consumer<Void>() {
				
				@Override
				public void accept(Void t) {
					channel.sendMessage("Die Rolle MasterStudent wurde dir erfolgreich zugewiesen.").queue();
					removeRole(jda, user,true, false, channel);
				}
			});
			
		}
	}
	/**
	 * This Method opens an async private Channel and send the User the welcome message and adds the M & B Reaction
	 * @param user the User the message shoul be send to
	 */
	public void sendWelcomeMessage(Member mem) {
		User user = mem.getUser();
		UserEntry entry = DatabaseHandler.getUserEntry(database, user.getIdLong());
		if(entry == null) {
			database = DatabaseHandler.addUser(database, new UserEntry(user.getIdLong(), user.getName(), Integer.parseInt(user.getDiscriminator()), true, mem.getNickname(), UserTyp.BACHELOR.getId()));
			try {
				DatabaseHandler.saveDatabase(database, DatabaseHandler.defaultDatabasePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		user.openPrivateChannel().queue((channel) ->
		{
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("Willkommen auf dem MNI Discord Server");
			builder.setDescription(WELCOME_MESSAGE);
			channel.sendMessage(builder.build()).queue((message) ->
			{
				message.addReaction(bUnicode).queue();
				message.addReaction(mUnicode).queue();
			});
		});
	}
	
	/**
	 * Opens a PrivateMessage channel and send the Welcome message as an embeded Text
	 * When Send adds a B and M emote to the Message
	 * @param user the User the welcome message should be send to
	 */
	public void sendWelcomeMessage(User user) {
		user.openPrivateChannel().queue((channel) ->
		{
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("Willkommen auf dem MNI Discord Server");
			builder.setDescription(WELCOME_MESSAGE);
			channel.sendMessage(builder.build()).queue((message) ->
			{
				message.addReaction(bUnicode).queue();
				message.addReaction(mUnicode).queue();
			});
		});
	}

	/******************GETTERS*******************/
	public Map<String, CommandExecutor> getCommands() {
		return commands;
	}
	
	
}
