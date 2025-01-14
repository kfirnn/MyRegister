package com.example.myregister.screens;

import static com.example.myregister.R.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myregister.R;

public class HomePage extends AppCompatActivity {



    public Button btnShop, btnGames, btnProfile;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(layout.activity_home_page);

            // אתחול כפתורים
            btnShop = findViewById(R.id.btnShop);
            btnGames = findViewById(R.id.btnGames);
            btnProfile = findViewById(R.id.btnProfile);

            // מאזין לכפתור "חנות"
            btnShop.setOnClickListener(v -> {
                // עבור לדף החנות (כמובן שתצטרך ליצור את העמוד הזה)
                Intent intent = new Intent(HomePage.this, SearchItem.class);
                startActivity(intent);
            });


    }

    }
