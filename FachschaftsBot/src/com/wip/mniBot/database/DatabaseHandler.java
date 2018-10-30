package com.wip.mniBot.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

public class DatabaseHandler {

	public static final String defaultDatabasePath = "res/users.json";
	
	public static JSONObject loadDatabaseFromFile(String path) throws IOException{
		File file = new File(path);
		if(!file.exists()) {
			throw new FileNotFoundException("File : "+path+" not found...");
		}
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String obj = reader.readLine();
		reader.close();
		return new JSONObject(obj);
	}
	
	public static JSONObject addUser(JSONObject database,UserEntry newUser) {
		JSONArray userlist = database.getJSONArray("users");
		userlist.put(newUser.toJSONObject());
		database.put("users", userlist);
		return database;
	}
	
	public static void saveDatabase(JSONObject database,String path) throws IOException {
		File file = new File(path);
		if(!file.exists()) {
			file.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write(database.toString());
		bw.close();
	}
	
	public static int getIndexOfUserById(JSONObject database,long id) {
		JSONArray array = database.getJSONArray("users");
		for(int i=0;i<array.length();i++) {
			if(array.getJSONObject(i).getLong("Id") == id) {
				return i;
			}
		}
		return -1;
	}
	public static JSONObject getUserFromDatabaseByIndex(JSONObject database,int index) {
		return database.getJSONArray("users").getJSONObject(index);
	}
	public static UserEntry getUserEntry(JSONObject database,long id) {
		return new UserEntry(getUserFromDatabaseByIndex(database, getIndexOfUserById(database, id)));
	}
	
}
