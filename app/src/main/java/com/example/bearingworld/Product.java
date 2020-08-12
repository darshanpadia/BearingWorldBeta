package com.example.bearingworld;

import java.io.Serializable;

public class Product implements Serializable {
    public String id;
    public String imgUri;
    public String cost;
    public String name;
    public String description;
    public String stock;
    public String quantity;
    public int productTotalCost;

    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(Product.class)
    }

    public Product(String id, String imgUri, String cost, String name, String description, String stock, String quantity,int productTotalCost){
        this.id = id;
        this.imgUri = imgUri;
        this.cost = cost;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.quantity = quantity;
        this.productTotalCost = productTotalCost;
    }
}

