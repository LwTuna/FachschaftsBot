package com.wip.mniBot.database;

import java.util.Collection;

import org.json.JSONObject;

public class UserEntry {

	private long id;
	private String username;
	private int discriminatorValue;
	private boolean isMale;
	private String nickname;
	private int typ;
	
	public UserEntry(JSONObject entry) {
		id=entry.getLong("Id");
		username = entry.getString("Username");
		discriminatorValue = entry.getInt("DiscriminatorValue");
		isMale = entry.getBoolean("IsMale");
		nickname = entry.getString("Nickname");
		typ = entry.getInt("Typ");
	}

	public UserEntry(long id, String username, int discriminatorValue, boolean isMale, String nickname, int typ) {
		this.id = id;
		this.username = username;
		this.discriminatorValue = discriminatorValue;
		this.isMale = isMale;
		this.nickname = nickname;
		this.typ = typ;
	}

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
