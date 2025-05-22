package com.example.myregister.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myregister.R;
import com.example.myregister.model.Cart;
import com.example.myregister.model.Item;
import com.example.myregister.utils.ImageUtil;

public class ShowSpecificItemsActivity extends AppCompatActivity {

    private static final String TAG = "ShowSpecificItems";
    private ImageView itemDetailImage;
    private TextView itemDetailName;
    private TextView itemDetailType;
    private TextView itemDetailPrice;
    private TextView itemDetailDescription;
    private Button btnAddToCartDetail;
    private Item selectedItem;
    private Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_specific_items);

        // Initialize views
        itemDetailImage = findViewById(R.id.itemDetailImage);
        itemDetailName = findViewById(R.id.itemDetailName);
        itemDetailType = findViewById(R.id.itemDetailType);
        itemDetailPrice = findViewById(R.id.itemDetailPrice);
        itemDetailDescription = findViewById(R.id.itemDetailDescription);
        btnAddToCartDetail = findViewById(R.id.btnAddToCartDetail);

        // Get the item and cart from intent
        selectedItem = (Item) getIntent().getSerializableExtra("selectedItem");
        cart = (Cart) getIntent().getSerializableExtra("cart");
        
        if (cart == null) {
            cart = new Cart();
        }

        if (selectedItem == null) {
            Log.e(TAG, "No item data received");
            Toast.makeText(this, "Error: No item data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d(TAG, "Received item: " + selectedItem.toString());

        // Display item details
        if (selectedItem.getPic() != null && !selectedItem.getPic().isEmpty()) {
            Log.d(TAG, "Loading image from base64 string");
            try {
                itemDetailImage.setImageBitmap(ImageUtil.convertFrom64base(selectedItem.getPic()));
            } catch (Exception e) {
                Log.e(TAG, "Error loading image", e);
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.w(TAG, "No image data available");
        }
        
        itemDetailName.setText(selectedItem.getName());
        itemDetailType.setText("Type: " + selectedItem.getType());
        itemDetailPrice.setText("₪" + selectedItem.getPrice());
        itemDetailDescription.setText(selectedItem.getAboutItem());

        // Set up Add to Cart button
        btnAddToCartDetail.setOnClickListener(v -> {
            cart.addItem(selectedItem);
            Toast.makeText(this, "Added " + selectedItem.getName() + " to cart", Toast.LENGTH_SHORT).show();
            
            // Return to store activity with updated cart
            Intent intent = new Intent();
            intent.putExtra("updatedCart", cart);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}