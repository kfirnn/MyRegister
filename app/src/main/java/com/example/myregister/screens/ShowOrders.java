package com.example.myregister.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myregister.R;
import com.example.myregister.adapters.OrderAdapter;
import com.example.myregister.model.Order;
import com.example.myregister.services.DatabaseService;
import java.util.ArrayList;
import java.util.List;

public class ShowOrders extends AppCompatActivity {
    private static final String TAG = "ShowOrders";
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private Button backButton;
    private List<Order> orderList = new ArrayList<>();
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_orders);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize DatabaseService
        databaseService = DatabaseService.getInstance();

        // Initialize views
        recyclerView = findViewById(R.id.OrderRecyclerView);
        backButton = findViewById(R.id.backButton);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(orderAdapter);

        // Fetch orders
        fetchOrders();

        // Back button click listener
        backButton.setOnClickListener(v -> finish());
    }

    private void fetchOrders() {
        databaseService.getOrders(new DatabaseService.DatabaseCallback<List<Order>>() {
            @Override
            public void onCompleted(List<Order> orders) {
                orderList.clear();
                orderList.addAll(orders);
                orderAdapter.notifyDataSetChanged();
                
                if (orderList.isEmpty()) {
                    Toast.makeText(ShowOrders.this, "No orders found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Error fetching orders: " + e.getMessage());
                Toast.makeText(ShowOrders.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
            }
        });
    }
} 