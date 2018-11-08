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
/**
 * Manage(Load,Save and Edit) the Database
 * @author Jonas
 *
 */
public class DatabaseHandler {

	//The Default Path to the Database
	public static final String defaultDatabasePath = "res/users.json";
	/**
	 * Loads The Database from the given path
	 * @param path The Path to the Database File
	 * @return the JSON Object which contains a UserEntry[] of JSON Objects
	 * @throws IOException throws a Exception if the File doesn't exists or if it's not found
	 */
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
	
	/**
	 * Adds a UserEntry to the Database
	 * @param database The Database the User shoul be added to
	 * @param newUser The new User
	 * @return The updated Database with the new UserEntry
	 */
	public static JSONObject addUser(JSONObject database,UserEntry newUser) {
		JSONArray userlist = database.getJSONArray("users");
		userlist.put(newUser.toJSONObject());
		database.put("users", userlist);
		return database;
	}
	/**
	 * Saves the Database as a JSON Object to the given Path
	 * Creates a new File if it's not Found
	 * @param database The Database Object which should be saved
	 * @param path the Path to the File where it should be stored
	 * @throws IOException a Exception thrown when it failed to created a FileWriter
	 */
	public static void saveDatabase(JSONObject database,String path) throws IOException {
		File file = new File(path);
		if(!file.exists()) {
			file.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write(database.toString());
		bw.close();
	}
	/**
	 * Gets the Database index of the given Userid
	 * @param database The DatabaseObject where the User should be found
	 * @param id the User ID as a Key for the Search
	 * @return the Index of the User in the Database
	 */
	public static int getIndexOfUserById(JSONObject database,long id) {
		JSONArray array = database.getJSONArray("users");
		for(int i=0;i<array.length();i++) {
			if(array.getJSONObject(i).getLong("Id") == id) {
				return i;
			}
		}
		return -1;
	}
	/**
	 * Gets a Json Object from the Database by its index
	 * @param database The Database the user should be searched in
	 * @param index The index of the User in the databse
	 * @return A JSON object the UserEntry in the Database
	 */
	public static JSONObject getUserFromDatabaseByIndex(JSONObject database,int index) {
		return database.getJSONArray("users").getJSONObject(index);
	}
	/**
	 * Gets a UserEntry Object of the Database by its id
	 * @param database The Datbase the User should be in
	 * @param id the id of the User
	 * @return A UserEntry Object of the User
	 */
	public static UserEntry getUserEntry(JSONObject database,long id) {
		JSONObject obj = getUserFromDatabaseByIndex(database, getIndexOfUserById(database, id));
		if(obj == null)
			return null;
		return new UserEntry(obj);
	}
	
}
