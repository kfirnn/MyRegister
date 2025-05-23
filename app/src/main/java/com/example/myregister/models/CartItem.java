package com.example.myregister.models;

public class CartItem {
    private String itemId;
    private String name;
    private double price;
    private int quantity;
    private String userId;

    // Required empty constructor for Firebase
    public CartItem() {}

    public CartItem(String itemId, String name, double price, int quantity, String userId) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.userId = userId;
    }

    // Getters and Setters
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
} 