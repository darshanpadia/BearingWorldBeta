package com.example.bearingworld;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class Order implements Serializable {
    public String id;
    public int totalAmount;
    public String orderState;
    public HashMap<String,Product> productList = new HashMap<>();
    public boolean isActive;
    public String userAddress;
    public String userPhone;
    public String userName;
    public String userEmail;
    public String  dateOrderPlaced;
    public String deliveryDate;
    public String userID;

    public Order(){
        // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    }

    public Order(String id, int totalAmount,String userID, HashMap<String,Product> productList, String userName, String userPhone, String userEmail, String userAddress, String dateOrderPlaced, String deliveryDate){
        this.totalAmount = totalAmount;
        this.id = id;
        this.orderState = "Placed";
        this.productList = productList;
        this.isActive = true;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userAddress = userAddress;
        this.dateOrderPlaced = dateOrderPlaced;
        this.deliveryDate = deliveryDate;
        this.userID = userID;
        //Setting Dates


    }

}
