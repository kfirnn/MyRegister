package com.example.myregister.screens;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myregister.R;
import com.example.myregister.utils.CartManager;
import com.example.myregister.models.CartItem;

import java.util.List;

public abstract class BaseActivity extends AppCompatActivity implements CartManager.CartUpdateListener {
    protected Toolbar toolbar;
    protected TextView cartBadge;
    protected View cartBadgeView;

    @Override
    protected void onStart() {
        super.onStart();
        setupToolbar();
        CartManager.getInstance().setCartUpdateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        CartManager.getInstance().setCartUpdateListener(null);
    }

    protected void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        
        MenuItem cartItem = menu.findItem(R.id.action_cart);
        View actionView = cartItem.getActionView();
        
        if (actionView != null) {
            cartBadgeView = actionView.findViewById(R.id.cart_badge);
            cartBadge = actionView.findViewById(R.id.cart_badge);
            
            actionView.setOnClickListener(v -> {
                if (!CartManager.getInstance().isUserLoggedIn()) {
                    Toast.makeText(this, "Please log in to view your cart", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, Login.class);
                    startActivity(intent);
                    return;
                }
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
            });

            updateCartBadge();
        }
        
        return true;
    }

    @Override
    public void onCartUpdated(List<CartItem> items) {
        updateCartBadge();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void updateCartBadge() {
        if (cartBadge != null) {
            if (!CartManager.getInstance().isUserLoggedIn()) {
                cartBadge.setVisibility(View.GONE);
                return;
            }

            int itemCount = CartManager.getInstance().getCartItemCount();
            if (itemCount > 0) {
                cartBadge.setVisibility(View.VISIBLE);
                cartBadge.setText(String.valueOf(itemCount));
            } else {
                cartBadge.setVisibility(View.GONE);
            }
        }
    }
} 