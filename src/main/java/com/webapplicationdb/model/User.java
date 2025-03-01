package com.webapplicationdb.model;

public class User {
    private String userName;
    private String password;
    private String userRole;

    public User() {}

    public User(String userName, String password, String userRole) {
        this.userName = userName;
        this.password = password;
        this.userRole = userRole;
    }

    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
