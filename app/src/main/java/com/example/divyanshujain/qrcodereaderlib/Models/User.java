package com.example.divyanshujain.qrcodereaderlib.Models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Ravi Tamada on 07/10/16.
 * www.androidhive.info
 */

@IgnoreExtraProperties
public class User {

    public String password;
    public String email;
    private String userType;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String userType, String password, String email) {
        this.userType = userType;
        this.password = password;
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public boolean equals(Object obj) {
        return this.email.equals(((User) obj).email);
    }
}
