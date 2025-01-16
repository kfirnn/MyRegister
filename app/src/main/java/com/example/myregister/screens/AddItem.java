package com.example.myregister.screens;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
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


    private DatabaseService databaseService;

    private ActivityResultLauncher<Intent> selectImageLauncher;
    private ActivityResultLauncher<Intent> captureImageLauncher;

    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;

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
                        iv.setImageURI(selectedImage);
                    }
                });

        captureImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                        iv.setImageBitmap(bitmap);
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
        etDescription=findViewById(R.id.etDescription);

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
            dedc = etDescription.getText().toString()+"";
            itemName = etItemName.getText().toString();
            stPrice = etPrice.getText().toString();



            if (iv.getDrawable() == null) {
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

            String imageBase64 = ImageUtil.convertTo64Base(iv);

            String itemid = databaseService.generateItemId();

            Item newItem = new Item(itemid, itemName, type, imageBase64, dedc, price);
            databaseService.createNewItem(newItem, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void object) {
                    // handle completion

                    Log.d("TAG", "Food added successfully");
                    Toast.makeText(AddItem.this, "Item added successfully", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailed(Exception e) {
                    // handle failure
                    Log.e("TAG", "Failed to add food", e);
                    Toast.makeText(AddItem.this, "Failed to add Item", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectImageLauncher.launch(intent);

        imageChooser();

    }

    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImageLauncher.launch(takePictureIntent);
    }

    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    iv.setImageURI(selectedImageUri);
                }
            }
        }
    }


}


