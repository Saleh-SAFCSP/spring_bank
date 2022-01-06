package com.example.spring101.model;


public class UserActions {

    private  String id;
    private  String password;
    private  int amount;

    public UserActions(String id, String password, int amount) {
        this.id = id;
        this.password = password;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
