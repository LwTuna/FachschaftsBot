package com.wip.mniBot.database;

import java.util.Collection;

import org.json.JSONObject;
/**
 * This class represents a UserEntry in the Database
 * @author Jonas
 */
public class UserEntry {

	//Stored information about the User
	private long id;
	private String username;
	private int discriminatorValue;
	private boolean isMale;
	private String nickname;
	private int typ;
	
	/**
	 * Constructor for a UserEntry already in the Database
	 * Creates a Java Object from the JSONObject out of the Database
	 * @param entry The Entry in the Database as a JSON Object
	 */
	public UserEntry(JSONObject entry) {
		id=entry.getLong("Id");
		username = entry.getString("Username");
		discriminatorValue = entry.getInt("DiscriminatorValue");
		isMale = entry.getBoolean("IsMale");
		nickname = entry.getString("Nickname");
		typ = entry.getInt("Typ");
	}
	
	/**
	 * Constructor for UserEntry from JavaCode created when a User joined the Discord Server
	 * @param id The unique User ID from the Discord User
	 * @param username The Username from the User
	 * @param discriminatorValue the #number after the Username
	 * @param isMale if the User is male default true
	 * @param nickname the for the User on the Discord Server
	 * @param typ The UserTyp of the User
	 */
	public UserEntry(long id, String username, int discriminatorValue, boolean isMale, String nickname, int typ) {
		this.id = id;
		this.username = username;
		this.discriminatorValue = discriminatorValue;
		this.isMale = isMale;
		this.nickname = nickname;
		this.typ = typ;
	}

	/**
	 * Creates a JSON Object from the values in this UserEntry
	 * @return this Object as JSON Object
	 */
	public JSONObject toJSONObject() {
		JSONObject object = new JSONObject();
		object.put("Id", id);
		object.put("Username", username);
		object.put("DiscriminatorValue", discriminatorValue);
		object.put("IsMale", isMale);
		object.put("Nickname", nickname);
		object.put("Typ", typ);
		return object;
	}
	
	/***************GETTERS********************/
	public long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public int getDiscriminatorValue() {
		return discriminatorValue;
	}

	public boolean isMale() {
		return isMale;
	}

	public String getNickname() {
		return nickname;
	}

	public int getTyp() {
		return typ;
	}

	
	
	
	
	
	
}
