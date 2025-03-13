package com.example.myregister.screens;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myregister.R;
import com.example.myregister.adapters.CartAdapter;
import com.example.myregister.model.Cart;
import com.example.myregister.model.Item;
import com.example.myregister.model.User;
import com.example.myregister.services.DatabaseService;

import java.util.ArrayList;


public class CartActivity extends AppCompatActivity {

    private ListView cartListView;
    private TextView totalPriceText;
    private Cart cart;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListView = findViewById(R.id.cartListView);
        totalPriceText = findViewById(R.id.cartItemsText);

        // קבלת העגלה שנשלחה מ-RecyclerViewActivity
        cart = (Cart) getIntent().getSerializableExtra("cart");

        if (cart != null) {
            cartAdapter = new CartAdapter(this, cart.getItems());
            cartListView.setAdapter(cartAdapter);
            updateTotalPrice();
        }
    }

    // עדכון המחיר הכולל
    private void updateTotalPrice() {
        double totalPrice = 0;
        for (Item item : cart.getItems()) {
            totalPrice += item.getPrice();
        }
        totalPriceText.setText("סך הכל: ₪" + totalPrice);
    }
}
