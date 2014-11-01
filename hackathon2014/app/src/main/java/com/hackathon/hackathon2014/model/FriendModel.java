package com.hackathon.hackathon2014.model;

public class FriendModel {
    private String name;
    private String id;
    private String email;
    private int weight;

    public FriendModel() {

    }

    public FriendModel(String name, String id, String email, int weight) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.weight = weight;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public void setEmail( String email) { this.email = email; }

    public void setWeight( int weight ) { this.weight = weight; }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public String getEmail() {
        return  this.email;
    }

    public int getWeight() {
        return this.weight;
    }

}
