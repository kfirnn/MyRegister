package com.example.myregister.utils;

import androidx.annotation.NonNull;
import android.util.Log;
import com.example.myregister.models.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static final String TAG = "CartManager";
    private static CartManager instance;
    private final DatabaseReference cartRef;
    private final FirebaseAuth auth;
    private List<CartItem> cartItems = new ArrayList<>();
    private CartUpdateListener listener;
    private ValueEventListener cartValueListener;

    public interface CartUpdateListener {
        void onCartUpdated(List<CartItem> items);
        default void onError(String message) {}
    }

    private CartManager() {
        auth = FirebaseAuth.getInstance();
        cartRef = FirebaseDatabase.getInstance().getReference("carts");
        setupAuthStateListener();
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

    private void setupAuthStateListener() {
        auth.addAuthStateListener(firebaseAuth -> {
            // Remove previous listener if exists
            if (cartValueListener != null) {
                cartRef.removeEventListener(cartValueListener);
            }
            
            // Clear current cart items
            cartItems.clear();
            
            // Setup new listener for current user
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                setupCartListener(user.getUid());
            } else {
                // Notify about empty cart when user is not logged in
                if (listener != null) {
                    listener.onCartUpdated(cartItems);
                    listener.onError("Please log in to view your cart");
                }
            }
        });
    }

    private void setupCartListener(String userId) {
        cartValueListener = new ValueEventListener() {
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
                Log.e(TAG, "Error loading cart items: " + error.getMessage());
                if (listener != null) {
                    listener.onError("Failed to load cart items: " + error.getMessage());
                }
            }
        };
        cartRef.child(userId).addValueEventListener(cartValueListener);
    }

    public void addToCart(String itemId, String name, double price) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            if (listener != null) {
                listener.onError("Please log in to add items to cart");
            }
            return;
        }

        String userId = user.getUid();
        // Check if item already exists in cart
        for (CartItem item : cartItems) {
            if (item.getItemId().equals(itemId)) {
                // Update quantity
                item.setQuantity(item.getQuantity() + 1);
                updateCartItem(userId, item);
                return;
            }
        }

        // Add new item
        CartItem newItem = new CartItem(itemId, name, price, 1, userId);
        updateCartItem(userId, newItem);
    }

    private void updateCartItem(String userId, CartItem item) {
        cartRef.child(userId).child(item.getItemId()).setValue(item)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "Cart item updated successfully"))
            .addOnFailureListener(e -> {
                Log.e(TAG, "Error updating cart item: " + e.getMessage());
                if (listener != null) {
                    listener.onError("Failed to update cart: " + e.getMessage());
                }
            });
    }

    public void removeFromCart(String itemId) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            if (listener != null) {
                listener.onError("Please log in to remove items from cart");
            }
            return;
        }

        cartRef.child(user.getUid()).child(itemId).removeValue()
            .addOnFailureListener(e -> {
                Log.e(TAG, "Error removing item from cart: " + e.getMessage());
                if (listener != null) {
                    listener.onError("Failed to remove item: " + e.getMessage());
                }
            });
    }

    public void updateQuantity(String itemId, int quantity) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            if (listener != null) {
                listener.onError("Please log in to update cart");
            }
            return;
        }

        if (quantity <= 0) {
            removeFromCart(itemId);
            return;
        }

        for (CartItem item : cartItems) {
            if (item.getItemId().equals(itemId)) {
                item.setQuantity(quantity);
                updateCartItem(user.getUid(), item);
                break;
            }
        }
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems); // Return a copy to prevent external modifications
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
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            if (listener != null) {
                listener.onError("Please log in to clear cart");
            }
            return;
        }

        cartRef.child(user.getUid()).removeValue()
            .addOnSuccessListener(aVoid -> {
                cartItems.clear();
                if (listener != null) {
                    listener.onCartUpdated(cartItems);
                }
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Error clearing cart: " + e.getMessage());
                if (listener != null) {
                    listener.onError("Failed to clear cart: " + e.getMessage());
                }
            });
    }

    public boolean isUserLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    public String getCurrentUserId() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null ? user.getUid() : null;
    }
} 