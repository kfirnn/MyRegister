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
import com.example.myregister.services.AuthenticationService;

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

        if (AuthenticationService.getInstance().isUserSignedIn()) {
            SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            String email = sharedpreferences.getString("email", "");
            Intent intent = new Intent(this, HomePage.class);
            if (email.equals(Login.admin)) {
                intent = new Intent(this, AdminPage.class);
            }

        }
    }

    public void onRegisterClick(View view) {
        Intent go = new Intent(getApplicationContext(), Register.class);
        startActivity(go);
    }

    public void onLoginClick(View view) {

    }

    public void onLoginClick2(View view) {
        Intent go=new Intent(getApplicationContext(),Login.class);
        startActivity(go);
    }
}