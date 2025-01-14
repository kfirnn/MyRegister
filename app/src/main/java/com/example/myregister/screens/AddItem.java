package com.example.myregister.screens;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myregister.R;
import com.example.myregister.model.Item;
import com.example.myregister.services.DatabaseService;
import com.example.myregister.utils.ImageUtil;

public class AddItem extends AppCompatActivity implements View.OnClickListener {

    Spinner spinnertype, spinnertypeSize;  // Spinner for sizes

    EditText etItemName, etPrice, etDescription;
    String itemName, stPrice, type;

    double price;

    String imageRef;
    String dedc;

    Button btnGallery, btnTakePic, btnAddItem;
    ImageView iv;

    private ImageView foodImageView;
    private DatabaseService databaseService;

    private ActivityResultLauncher<Intent> selectImageLauncher;
    private ActivityResultLauncher<Intent> captureImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        initViews();

        ImageUtil.requestPermission(this);

        databaseService = DatabaseService.getInstance();

        btnTakePic.setOnClickListener(this);
        btnAddItem.setOnClickListener(this);
        btnGallery.setOnClickListener(this);

        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        foodImageView.setImageURI(selectedImage);
                    }
                });

        captureImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                        foodImageView.setImageBitmap(bitmap);
                    }
                });
    }

    private void initViews() {
        btnAddItem = findViewById(R.id.btnAddItem);
        btnGallery = findViewById(R.id.btnGalleryD);
        btnTakePic = findViewById(R.id.btnTakePicD);

        iv = findViewById(R.id.ivd);

        spinnertype = findViewById(R.id.spItemTYpe);
        spinnertypeSize = findViewById(R.id.spItemSize);  // Size spinner

        etItemName = findViewById(R.id.etItemName);
        etPrice = findViewById(R.id.etItemPrice);

        // Initially hide the size spinner
        spinnertypeSize.setVisibility(View.GONE);

        // Set listener to show or hide size spinner based on item type
        spinnertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedType = spinnertype.getSelectedItem().toString();

                if (selectedType.equals("חולצה") || selectedType.equals("מכנס")) {
                    spinnertypeSize.setVisibility(View.VISIBLE);  // Show size spinner
                } else {
                    spinnertypeSize.setVisibility(View.GONE);  // Hide size spinner
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No item selected
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == btnTakePic) {
            selectImageFromGallery();
            return;
        }
        if (view == btnGallery) {
            captureImageFromCamera();
            return;
        }
        if (view == btnAddItem) {
            type = spinnertype.getSelectedItem().toString();
            dedc = etDescription.getText().toString();
            itemName = etItemName.getText().toString();
            stPrice = etPrice.getText().toString();

            if (foodImageView.getDrawable() == null) {
                Toast.makeText(AddItem.this, "Please take pic!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(stPrice)) {
                Toast.makeText(AddItem.this, "Please enter a price", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                price = Double.parseDouble(stPrice);
            } catch (NumberFormatException e) {
                Toast.makeText(AddItem.this, "Invalid price input", Toast.LENGTH_SHORT).show();
                return;
            }

            String imageBase64 = ImageUtil.convertTo64Base(foodImageView);

            String itemid = databaseService.generateItemId();

            Item newItem = new Item(itemid, itemName, type, imageBase64, dedc, price);
            databaseService.createNewItem(newItem, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void object) {
                    // handle completion
                }

                @Override
                public void onFailed(Exception e) {
                    // handle failure
                }
            });
        }
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectImageLauncher.launch(intent);
    }

    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImageLauncher.launch(takePictureIntent);
    }
}


