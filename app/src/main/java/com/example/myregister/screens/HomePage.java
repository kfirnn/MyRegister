package com.example.myregister.screens;

import static com.example.myregister.R.*;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myregister.R;
import com.example.myregister.services.AuthenticationService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePage extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";

    public Button btnShop, btnGames, btnProfile;
    private TextView tvWelcome;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_home_page);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        
        // Initialize views
        btnShop = findViewById(R.id.btnShop);
        btnGames = findViewById(R.id.btnGames);
        btnProfile = findViewById(R.id.btnProfile);
        tvWelcome = findViewById(R.id.tvWelcome);

        // Get current user and display welcome message
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String firstName = snapshot.child("fName").getValue(String.class);
                        if (firstName != null) {
                            tvWelcome.setText("Hello, " + firstName + "!");
                        } else {
                            tvWelcome.setText("Welcome to MaccabiZone!");
                            Toast.makeText(HomePage.this, "Could not load user name", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        tvWelcome.setText("Welcome to MaccabiZone!");
                        Toast.makeText(HomePage.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    tvWelcome.setText("Welcome to MaccabiZone!");
                    Toast.makeText(HomePage.this, "Failed to load user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            tvWelcome.setText("Welcome to MaccabiZone!");
        }

        // מאזין לכפתור "חנות"
        btnShop.setOnClickListener(v -> {
            // עבור לדף החנות
            Intent intent = new Intent(HomePage.this, StoreActivity.class);
            startActivity(intent);
        });

        // מאזין לכפתור "פרופיל"
        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Set up the toolbar
        setSupportActionBar(findViewById(R.id.toolbar));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu resource
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        
        if (itemId == R.id.menu_home) {
            // We're already on home page
            return true;
        } else if (itemId == R.id.menu_store) {
            startActivity(new Intent(this, StoreActivity.class));
            return true;
        } else if (itemId == R.id.menu_cart) {
            startActivity(new Intent(this, CartActivity.class));
            return true;
        } else if (itemId == R.id.menu_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        } else if (itemId == R.id.menu_settings) {
            // TODO: Add Settings activity
            Toast.makeText(this, "Settings - Coming soon", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.menu_logout) {
            AuthenticationService.getInstance().signOut();
            SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
}
