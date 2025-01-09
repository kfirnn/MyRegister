package com.example.myregister.model;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;

public class Cart {

    /// unique id of the cart
    private String id;

    private final ArrayList<Item> items;

    public Cart() {
        items = new ArrayList<>();
    }

    public Cart(String id) {
        this.id = id;
        items = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addFood(Item item) {
        items.add(item);
    }

    public void addFoods(List<Item> foods) {
        this.items.addAll(foods);
    }

    public boolean removeFood(Item food) {
        return items.remove(food);
    }

    public Item removeItem(int index) {
        if (index < 0 || index >= items.size()) {
            return null;
        }
        return items.remove(index);
    }

    public Item getFood(int index) {
        if (index < 0 || index >= items.size()) {
            return null;
        }
        return items.get(index);
    }

    public ArrayList<Item> getFoods() {
        return items;
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (Item item : items) {
            totalPrice += item.getPrice();  // תיקון כאן
        }
        return totalPrice;
    }

    public void clear() {
        items.clear();
    }

    @NonNull
    @Override
    public String toString() {
        return "Cart{" +
                "id='" + id + '\'' +
                ", items=" + items +
                '}';
    }
}
