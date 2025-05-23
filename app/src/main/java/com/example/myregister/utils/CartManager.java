package com.example.myregister.utils;

import androidx.annotation.NonNull;
import com.example.myregister.models.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final DatabaseReference cartRef;
    private final FirebaseAuth auth;
    private List<CartItem> cartItems = new ArrayList<>();
    private CartUpdateListener listener;

    public interface CartUpdateListener {
        void onCartUpdated(List<CartItem> items);
    }

    private CartManager() {
        auth = FirebaseAuth.getInstance();
        cartRef = FirebaseDatabase.getInstance().getReference("carts");
        setupCartListener();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void setCartUpdateListener(CartUpdateListener listener) {
        this.listener = listener;
        if (listener != null && !cartItems.isEmpty()) {
            listener.onCartUpdated(cartItems);
        }
    }

    private void setupCartListener() {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "anonymous";
        cartRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItems.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    CartItem item = itemSnapshot.getValue(CartItem.class);
                    if (item != null) {
                        cartItems.add(item);
                    }
                }
                if (listener != null) {
                    listener.onCartUpdated(cartItems);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    public void addToCart(String itemId, String name, double price) {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "anonymous";
        
        // Check if item already exists in cart
        for (CartItem item : cartItems) {
            if (item.getItemId().equals(itemId)) {
                // Update quantity
                item.setQuantity(item.getQuantity() + 1);
                cartRef.child(userId).child(itemId).setValue(item);
                return;
            }
        }

        // Add new item
        CartItem newItem = new CartItem(itemId, name, price, 1, userId);
        cartRef.child(userId).child(itemId).setValue(newItem);
    }

    public void removeFromCart(String itemId) {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "anonymous";
        cartRef.child(userId).child(itemId).removeValue();
    }

    public void updateQuantity(String itemId, int quantity) {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "anonymous";
        if (quantity <= 0) {
            removeFromCart(itemId);
            return;
        }

        for (CartItem item : cartItems) {
            if (item.getItemId().equals(itemId)) {
                item.setQuantity(quantity);
                cartRef.child(userId).child(itemId).setValue(item);
                break;
            }
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public int getCartItemCount() {
        int count = 0;
        for (CartItem item : cartItems) {
            count += item.getQuantity();
        }
        return count;
    }

    public double getCartTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public void clearCart() {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "anonymous";
        cartRef.child(userId).removeValue();
        cartItems.clear();
        if (listener != null) {
            listener.onCartUpdated(cartItems);
        }
    }
} 