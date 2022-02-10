package com.example.bybloslogin.ui.login;

public class User {
    protected String username;
    protected String password;
    protected String accountType;

    public User() {

    }

    public User(String username, String password, String accountType) {
        this.username = username;
        this.password = password;
        this.accountType = accountType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAccountType() {
        return accountType;
    }
}
