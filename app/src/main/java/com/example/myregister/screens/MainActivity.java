package com.example.myregister.screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myregister.R;

public class MainActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onRegisterClick(View view) {
        Intent go = new Intent(getApplicationContext(), Register.class);
        startActivity(go);
    }

    public void onLoginClick(View view) {
        Intent go = new Intent(getApplicationContext(), Login.class);
        startActivity(go);
    }

    public void onLoginClick2(View view) {
        Intent go = new Intent(getApplicationContext(), Login.class);
        startActivity(go);
    }
}