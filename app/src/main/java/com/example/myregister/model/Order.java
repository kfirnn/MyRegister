package com.example.myregister.model;

import java.util.List;
import com.example.myregister.models.CartItem;

public class Order {
    private String orderId;
    private String userId;
    private String userEmail;
    private String userName;
    private List<CartItem> items;
    private double totalAmount;
    private long orderDate;
    private String status; // "pending", "completed", "cancelled"

    // Required empty constructor for Firebase
    public Order() {}

    public Order(String orderId, String userId, String userEmail, String userName, List<CartItem> items, double totalAmount, long orderDate, String status) {
        this.orderId = orderId;
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.items = items;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.status = status;
    }

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public long getOrderDate() { return orderDate; }
    public void setOrderDate(long orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
} 