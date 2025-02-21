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

import com.example.myregister.R;

import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends AppCompatActivity {

    // הגדרת משתנים עבור האלמנטים ב-XML
    private Spinner spSubCategory5, spinnertype;
    private EditText etSearchItem;
    private Button btnSearchItems, btnAllItems;
    private ListView lvItem;

    private List<String> itemsList; // רשימה שתכיל את כל הפריטים
    private ArrayAdapter<String> itemsAdapter; // אדפטר עבור רשימת הפריטים

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        // אתחול האלמנטים
        spSubCategory5 = findViewById(R.id.spSubCategory5);
        spinnertype = findViewById(R.id.spinnertype);
        etSearchItem = findViewById(R.id.etSearchItem);
        btnSearchItems = findViewById(R.id.btnSearchItems);
        btnAllItems = findViewById(R.id.btnAllItems);
        lvItem = findViewById(R.id.lvItem);

        // אתחול הרשימה
        itemsList = new ArrayList<>();
        itemsList.add("חולצה");
        itemsList.add("מכנסיים");
        itemsList.add("צעיף");
        itemsList.add("כובע");
        itemsList.add("תיק");

        // אתחול האדפטר לרשימה
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemsList);
        lvItem.setAdapter(itemsAdapter);

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

        // מאזין לבחירת פריט ב-ListView
        lvItem.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = itemsList.get(position);
            Toast.makeText(StoreActivity.this, "נבחר: " + selectedItem, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(StoreActivity.this, ShowSpecificItemsActivity.class);
            intent.putExtra("selectedItem", selectedItem);
            startActivity(intent);
        });

    }

    // פונקציה לחיפוש פריטים ברשימה
    private void searchItems(String query) {
        List<String> searchResults = new ArrayList<>();
        for (String item : itemsList) {
            if (item.toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(item);
            }
        }
        itemsAdapter.clear();
        itemsAdapter.addAll(searchResults);
        itemsAdapter.notifyDataSetChanged();
    }

    // פונקציה להציג את כל הפריטים
    private void showAllItems() {
        itemsAdapter.clear();
        itemsAdapter.addAll(itemsList);
        itemsAdapter.notifyDataSetChanged();
    }
}
