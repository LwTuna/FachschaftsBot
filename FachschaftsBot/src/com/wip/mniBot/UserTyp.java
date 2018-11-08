package com.wip.mniBot;
/**
 * Enumeration for the Different UserTypes in the Database
 * @author Jonas
 */
public enum UserTyp {

	NICHTS(0),EXTERNER_STUDENT(1),PROFESSOR(2),BACHELOR(3),MASTER(4);
	//The id of the User
	int id;
	/**
	 * Constructor for the enum UserTyp
	 * @param id the id of the User type
	 */
	private UserTyp(int id) {
		this.id = id;
	}
	/**
	 * 
	 * @return the id of the user type
	 */
	public int getId() {
		return id;
	}
	
	
}
