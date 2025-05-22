package com.example.myregister.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myregister.R;
import com.example.myregister.adapters.ItemAdapter;
import com.example.myregister.model.Cart;
import com.example.myregister.model.Item;
import com.example.myregister.services.DatabaseService;
import com.example.myregister.models.CartItem;
import com.example.myregister.utils.CartManager;

import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends BaseActivity {

    private static final String TAG = "StoreActivity";
    private Spinner spSubCategory5, spinnertype;
    private EditText etSearchItem;
    private Button btnSearchItems, btnAllItems;
    private RecyclerView rcItems;
    private Cart cart;

    private List<Item> itemsList = new ArrayList<>();
    private ItemAdapter itemsAdapter;

    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        databaseService = DatabaseService.getInstance();
        cart = new Cart();
        CartManager.getInstance().setCartUpdateListener(this);

        initViews();

        itemsAdapter = new ItemAdapter(new ArrayList<>(), StoreActivity.this, new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                Intent intent = new Intent(StoreActivity.this, ShowSpecificItemsActivity.class);
                intent.putExtra("selectedItem", item);
                intent.putExtra("cart", cart);
                startActivity(intent);
            }

            @Override
            public void onAddToCartClick(Item item) {
                addItemToCart(item);
            }
        });

        rcItems.setLayoutManager(new LinearLayoutManager(this));
        rcItems.setAdapter(itemsAdapter);

        loadItems();
        initializeSpinners();
        setupButtonListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_store) {
            // Already in store
            return true;
        } else if (itemId == R.id.action_home) {
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_games) {
            Toast.makeText(this, "Games feature coming soon!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addItemToCart(Item item) {
        CartManager.getInstance().addToCart(item.getId(), item.getName(), item.getPrice());
        Toast.makeText(this, "Added " + item.getName() + " to cart", Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        spSubCategory5 = findViewById(R.id.spSubCategory5);
        spinnertype = findViewById(R.id.spinnertype);
        etSearchItem = findViewById(R.id.etSearchItem);
        btnSearchItems = findViewById(R.id.btnSearchItems);
        btnAllItems = findViewById(R.id.btnAllItems);
        rcItems = findViewById(R.id.rcItems);
    }

    private void loadItems() {
        databaseService.getItems(new DatabaseService.DatabaseCallback<List<Item>>() {
            @Override
            public void onCompleted(List<Item> items) {
                Log.d(TAG, "Loaded " + items.size() + " items from database");
                itemsList.clear();
                itemsList.addAll(items);
                itemsAdapter.setItems(items);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Failed to load items", e);
                Toast.makeText(StoreActivity.this, "Failed to load items", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeSpinners() {
        ArrayAdapter<CharSequence> subCategoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.sub_category_array, android.R.layout.simple_spinner_item);
        subCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubCategory5.setAdapter(subCategoryAdapter);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.typeArr, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnertype.setAdapter(typeAdapter);
    }

    private void setupButtonListeners() {
        btnSearchItems.setOnClickListener(v -> {
            String searchQuery = etSearchItem.getText().toString().trim();

            if (searchQuery.isEmpty()) {
                Toast.makeText(StoreActivity.this, "Please enter a search term", Toast.LENGTH_SHORT).show();
            } else {
                searchItems(searchQuery);
            }
        });

        btnAllItems.setOnClickListener(v -> showAllItems());
    }

    private void searchItems(String query) {
        List<Item> searchResults = new ArrayList<>();
        for (Item item : itemsList) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(item);
            }
        }
        Log.d(TAG, "Search found " + searchResults.size() + " items for query: " + query);
        itemsAdapter.setItems(searchResults);
    }

    private void showAllItems() {
        Log.d(TAG, "Showing all " + itemsList.size() + " items");
        itemsAdapter.setItems(itemsList);
    }

    @Override
    public void onCartUpdated(List<CartItem> items) {
        super.onCartUpdated(items);
    }
}
