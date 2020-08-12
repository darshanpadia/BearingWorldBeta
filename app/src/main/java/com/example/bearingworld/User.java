package com.example.bearingworld;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    public String Uid;
    public String imgUri;
    public String address;
    public String username;
    public String phone;
    public String email;
    public HashMap<String,Order> orders = new HashMap<>();

    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String Uid, String imgUri, String address, String username, String phone, String email, HashMap<String,Order> orders){
        this.Uid = Uid;
        this.imgUri = imgUri;
        this.address = address;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.orders = orders;
    }


}
