package com.trading.models;

public class User {
    private final String userId;
    private final String name;
    private final String phone;
    private final String email;

    public User(String userId, String name, String phone, String email) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    // getters and setters
    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }


}
