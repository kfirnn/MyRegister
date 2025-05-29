package com.example.myregister.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import com.example.myregister.R;
import com.example.myregister.models.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;

public class AdminPage extends BaseActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Check if user is admin, if not, redirect to home page
        if (!Login.isAdmin) {
            Toast.makeText(this, "Access denied: Admin only", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomePage.class));
            finish();
            return;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        mAuth = FirebaseAuth.getInstance();

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Set toolbar title
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("MaccabiZone");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Only inflate the admin menu, not the base menu with cart
        getMenuInflater().inflate(R.menu.admin_toolbar_menu, menu);
        return true;
    }

    @Override
    protected void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        // Don't set up cart badge for admin
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        
        if (itemId == R.id.menu_homee) {
            startActivity(new Intent(this, HomePage.class));
            finish();
            return true;
        }
        else if (itemId == R.id.menu_storee) {
            startActivity(new Intent(this, StoreActivity.class));
            return true;
        }
        else if (itemId == R.id.menu_cartt) {
            startActivity(new Intent(this, CartActivity.class));
            return true;
        }
        else if (itemId == R.id.menu_profilee) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        }
        else if (itemId == R.id.menu_logoutt) {
            mAuth.signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItemClick(View view) {
        Intent go = new Intent(getApplicationContext(), AddItem.class);
        startActivity(go);
    }

    public void btnUserList(View view) {
        Intent go = new Intent(getApplicationContext(), ShowUsers.class);
        startActivity(go);
    }
    
    public void btnOrderList(View view) {
        Intent go = new Intent(getApplicationContext(), ShowOrders.class);
        startActivity(go);
    }

    @Override
    public void onCartUpdated(List<CartItem> items) {
        // Do nothing - admin doesn't need cart updates
    }
}
