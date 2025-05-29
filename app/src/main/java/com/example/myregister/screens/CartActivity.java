package com.example.myregister.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myregister.R;
import com.example.myregister.adapters.CartAdapter;
import com.example.myregister.model.Item;
import com.example.myregister.model.User;
import com.example.myregister.services.DatabaseService;
import com.example.myregister.models.CartItem;
import com.example.myregister.utils.CartManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends BaseActivity implements CartAdapter.OnCartItemRemovedListener {
    private static final String TAG = "CartActivity";
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private TextView totalPriceText;
    private TextView emptyCartText;
    private Button checkoutButton;
    private View loginPrompt;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setupToolbar();

        // Initialize views
        recyclerView = findViewById(R.id.cart_recycler_view);
        totalPriceText = findViewById(R.id.total_price);
        emptyCartText = findViewById(R.id.empty_cart_text);
        checkoutButton = findViewById(R.id.checkout_button);
        loginPrompt = findViewById(R.id.login_prompt);
        loginButton = findViewById(R.id.login_button);

        // Setup RecyclerView
        setupRecyclerView();
        
        // Check if user is logged in
        if (!CartManager.getInstance().isUserLoggedIn()) {
            showLoginPrompt();
        } else {
            updateUI();
        }

        // Setup checkout button
        checkoutButton.setOnClickListener(v -> {
            if (!CartManager.getInstance().isUserLoggedIn()) {
                Toast.makeText(this, "Please log in to checkout", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (cartAdapter.getItemCount() == 0) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, PaymentActivity.class);
            double totalAmount = CartManager.getInstance().getCartTotal();
            intent.putExtra("total_amount", totalAmount);
            startActivityForResult(intent, PAYMENT_REQUEST_CODE);
        });

        // Setup login button
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        });
    }

    private static final int PAYMENT_REQUEST_CODE = 100;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYMENT_REQUEST_CODE && resultCode == RESULT_OK) {
            // Payment was successful, now clear the cart
            CartManager.getInstance().clearCart();
            Toast.makeText(this, "Payment successful! Cart cleared.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void setupRecyclerView() {
        cartAdapter = new CartAdapter(this);
        cartAdapter.setOnCartItemRemovedListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);
        cartAdapter.updateItems(CartManager.getInstance().getCartItems());
    }

    private void updateUI() {
        if (!CartManager.getInstance().isUserLoggedIn()) {
            showLoginPrompt();
            return;
        }

        loginPrompt.setVisibility(View.GONE);
        List<CartItem> items = CartManager.getInstance().getCartItems();
        
        if (items.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyCartText.setVisibility(View.VISIBLE);
            totalPriceText.setVisibility(View.GONE);
            checkoutButton.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyCartText.setVisibility(View.GONE);
            totalPriceText.setVisibility(View.VISIBLE);
            checkoutButton.setVisibility(View.VISIBLE);
            updateTotalPrice();
        }
    }

    private void updateTotalPrice() {
        double total = CartManager.getInstance().getCartTotal();
        totalPriceText.setText(String.format("Total: ₪%.2f", total));
    }

    @Override
    public void onCartItemRemoved() {
        updateTotalPrice();
        if (cartAdapter.getItemCount() == 0) {
            updateUI();
        }
    }

    @Override
    public void onCartUpdated(List<CartItem> items) {
        super.onCartUpdated(items);
        cartAdapter.updateItems(items);
        updateUI();
    }

    private void showLoginPrompt() {
        recyclerView.setVisibility(View.GONE);
        emptyCartText.setVisibility(View.GONE);
        totalPriceText.setVisibility(View.GONE);
        checkoutButton.setVisibility(View.GONE);
        loginPrompt.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
