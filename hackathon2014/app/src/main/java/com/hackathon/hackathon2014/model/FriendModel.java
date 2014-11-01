package com.hackathon.hackathon2014.model;

public class FriendModel {
	private String name;
	private String id;
	
	public FriendModel() {
		
	}
	
	public FriendModel(String name, String id) {
		this.name = name;
		this.id = id;
	}
	
	public void setName( String name ) {
		this.name = name;
	}
	
	public void setId( String id ) {
		this.id = id;
	} 
	
	public String getName() {
		return this.name;
	}
	
	public String getId() {
		return this.id;
	}

	
}
