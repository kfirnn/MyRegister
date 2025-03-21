package com.example.myregister.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myregister.R;

public class AdminPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_page);

    }
    public void onAddItemClick(View view) {
        Intent go=new Intent(getApplicationContext(),AddItem.class);
        startActivity(go);
    }
    public void btnUserList(View view) {
        Intent go=new Intent(getApplicationContext(),ShowUsers.class);
        startActivity(go);
    }
}
