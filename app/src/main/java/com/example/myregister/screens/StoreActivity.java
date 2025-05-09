package com.example.myregister.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myregister.R;
import com.example.myregister.adapters.ItemAdapter;
import com.example.myregister.model.Item;
import com.example.myregister.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends AppCompatActivity {

    // הגדרת משתנים עבור האלמנטים ב-XML
    private Spinner spSubCategory5, spinnertype;
    private EditText etSearchItem;
    private Button btnSearchItems, btnAllItems;
    private RecyclerView rcItems;

    private List<Item> itemsList=new ArrayList<>(); // רשימה שתכיל את כל הפריטים
    private ItemAdapter itemsAdapter; // אדפטר עבור רשימת הפריטים


    DatabaseService databaseService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        databaseService=DatabaseService.getInstance();

        initViews();



        // אתחול האדפטר לרשימה
        itemsAdapter = new ItemAdapter(new ArrayList<>(), StoreActivity.this, new ItemAdapter.ItemClickListener() {
            @Override
            public void onClick(Item item) {
                Intent intent = new Intent(StoreActivity.this, ShowSpecificItemsActivity.class);
                intent.putExtra("selectedItem", item);
                startActivity(intent);
            }
        });


        rcItems.setLayoutManager(new LinearLayoutManager(this));

        rcItems.setAdapter(itemsAdapter);

        databaseService.getItems(new DatabaseService.DatabaseCallback<List<Item>>() {
            @Override
            public void onCompleted(List<Item> object) {
                itemsList.clear();
                itemsList.addAll(object);
                itemsAdapter.setItems(object);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });


        // אתחול Spinner של קטגוריות משנה
        ArrayAdapter<CharSequence> subCategoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.sub_category_array, android.R.layout.simple_spinner_item);
        subCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubCategory5.setAdapter(subCategoryAdapter);

        // אתחול Spinner של סוג פריט
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.typeArr, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnertype.setAdapter(typeAdapter);

        // מאזין עבור כפתור חיפוש
        btnSearchItems.setOnClickListener(v -> {
            String searchQuery = etSearchItem.getText().toString().trim();

            if (searchQuery.isEmpty()) {
                Toast.makeText(StoreActivity.this, "הכנס מילת חיפוש", Toast.LENGTH_SHORT).show();
            } else {
                searchItems(searchQuery);
            }
        });

        // מאזין עבור כפתור הצגת כל הפריטים
        btnAllItems.setOnClickListener(v -> {
            showAllItems();
        });


    }

    private void initViews() {

        // אתחול האלמנטים
        spSubCategory5 = findViewById(R.id.spSubCategory5);
        spinnertype = findViewById(R.id.spinnertype);
        etSearchItem = findViewById(R.id.etSearchItem);
        btnSearchItems = findViewById(R.id.btnSearchItems);
        btnAllItems = findViewById(R.id.btnAllItems);
        rcItems = findViewById(R.id.rcItems);
    }

    // פונקציה לחיפוש פריטים ברשימה
    private void searchItems(String query) {
        List<Item> searchResults = new ArrayList<>();
        for (Item item : itemsList) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(item);
            }
        }
        itemsAdapter.setItems(searchResults);
    }

    // פונקציה להציג את כל הפריטים
    private void showAllItems() {
        itemsAdapter.setItems(itemsList);
    }
}
