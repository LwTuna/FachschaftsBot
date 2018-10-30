package com.wip.mniBot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;



/**
* This Class is the main Entry Point of the Bot.
* It creates the jda Connection and adds the EventHandler.
* @version 0.1
* @author Jonas Reitz
*/
public class Bot {

	//Bot Token from https://discordapp.com/developers/applications/ 
	private final String token = "NDk2MzgwMTQ0MTc3MzgxMzg2.Dp6IBw.iJKxNS9YkQOs1S38Tk_1htKcu1c";
	//Unique Server Id from the mni discord Server
	public static final long mniServerID = 299888714253991938L;
	//Instance of the JDA class used for multiple things
	private JDA jda;
	
	  /**
	   * Constructor of the Bot class.
	   * Creates a instance of the JDA and adds an instance of the 
	   * EventHandler class as an EventListener to the JDA
	   * @throws LoginException or InterruptedException if 
	   * unable to connect or is interrupted by something 
	   */
	public Bot() throws LoginException, InterruptedException {
		JDABuilder builder = new JDABuilder(AccountType.BOT);
		builder.setToken(token);
		builder.setGame(Game.playing("!help for a List of Commands"));
		builder.addEventListener(new EventHandler(this));
		jda =builder.buildBlocking();
	}
	
	
	
	
	  /**
	   * entry point of the programm.
	   * creates a instance of the bot or throws an exception if 
	   * an error occurs.
	   * @param args list of start arguments (Should be empty)
	   */
	public static void main(String[] args) {
		try {
			new Bot();
		} catch (Exception e) {
			System.out.println("Error Building Bot : ");
			e.printStackTrace();
		}
	}



	/********************Getters***********************/

	public String getToken() {
		return token;
	}

	public long getMniServerID() {
		return mniServerID;
	}


	public JDA getJda() {
		return jda;
	}
	
}
