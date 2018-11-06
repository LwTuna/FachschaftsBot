package com.wip.mniBot;

public enum UserTyp {

	NICHTS(0),EXTERNER_STUDENT(1),PROFESSOR(2),BACHELOR(3),MASTER(4);
	
	int id;
	private UserTyp(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
	
}
