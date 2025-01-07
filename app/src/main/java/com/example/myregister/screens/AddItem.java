package com.example.myregister.screens;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myregister.R;
import com.example.myregister.model.Item;

public class AddItem  extends AppCompatActivity{

    EditText etItemName, etItemPrice;
    Spinner spSize, spTYpe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // קישור Views
         editTextItemId = findViewById(R.id.editTextItemId);
         editTextItemName = findViewById(R.id.editTextItemName);
         editTextItemType = findViewById(R.id.editTextItemType);
        EditText editTextItemImg = findViewById(R.id.editTextItemImg);
        EditText editTextItemSize = findViewById(R.id.editTextItemSize);
        EditText editTextItemPrice = findViewById(R.id.editTextItemPrice);
        Button buttonSaveItem = findViewById(R.id.buttonSaveItem);

        // מאזין ללחיצה על כפתור
        buttonSaveItem.setOnClickListener(v -> {
            // קבלת ערכים מהשדות
            String id = editTextItemId.getText().toString();
            String name = editTextItemName.getText().toString();
            String type = editTextItemType.getText().toString();
            String img = editTextItemImg.getText().toString();
            String size = editTextItemSize.getText().toString();
            String priceStr = editTextItemPrice.getText().toString();

            // בדיקת תקינות
            if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name) || TextUtils.isEmpty(type) ||
                    TextUtils.isEmpty(img) || TextUtils.isEmpty(size) || TextUtils.isEmpty(priceStr)) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid price format.", Toast.LENGTH_SHORT).show();
                return;
            }

            // יצירת אובייקט Item חדש
            Item newItem = new Item(id, name, type, img, size, price);

            // ניתן להוסיף את המוצר לרשימה או לשמור במסד נתונים
            Toast.makeText(this, "Item added: " + newItem.toString(), Toast.LENGTH_SHORT).show();

            // איפוס שדות
            editTextItemId.setText("");
            editTextItemName.setText("");
            editTextItemType.setText("");
            editTextItemImg.setText("");
            editTextItemSize.setText("");
            editTextItemPrice.setText("");
        });
    }
}
