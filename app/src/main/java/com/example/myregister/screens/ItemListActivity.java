package com.example.myregister.screens;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myregister.R;
import com.example.myregister.adapters.ItemAdapter;
import com.example.myregister.model.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ItemListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list); // זהו ה-XML שלך לעמוד הרשימה

        recyclerView = findViewById(R.id.recyclerView);
        itemAdapter = new ItemAdapter();

        // הגדרת ה-RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);

        // חיבור ל-Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("items");

        // קריאה לנתונים מ-Firebase
        fetchItemsFromFirebase();
    }

    private void fetchItemsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Item> itemList = new ArrayList<>();

                // מעבר על כל הנתונים ב-Firebase והמרה לאובייקטים של Item
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    if (item != null) {
                        itemList.add(item);
                    }
                }

                // הוספת הפריטים ל-Adapter
                itemAdapter.addItems(itemList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ItemListActivity.this, "Failed to load items.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
