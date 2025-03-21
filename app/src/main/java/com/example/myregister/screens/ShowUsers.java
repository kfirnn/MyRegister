package com.example.myregister.screens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myregister.R;
import com.example.myregister.adapters.UserAdapter;
import com.example.myregister.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShowUsers extends AppCompatActivity {

        private RecyclerView recyclerView;
        private UserAdapter usersAdapter;
        private FirebaseDatabase database;
        private DatabaseReference myRef;
        private Button backButton;
        private SearchView userSearchView;
        private List<User> userList;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_show_users);

            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }

            // קישור רכיבים מה-XML
            userSearchView = findViewById(R.id.userSearchView);
            recyclerView = findViewById(R.id.UserRecyclerView);
            backButton = findViewById(R.id.backButton);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Users");

            userList = new ArrayList<>();
            usersAdapter = new UserAdapter(userList, this);
            recyclerView.setAdapter(usersAdapter);

            // טעינת המשתמשים מהפיירבייס
            fetchUsers();

            // כפתור חזור
            backButton.setOnClickListener(v -> finish());

            // מאזין לחיפוש משתמשים
            userSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    usersAdapter.filter(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    usersAdapter.filter(newText);
                    return false;
                }
            });
        }

        private void fetchUsers() {
            myRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userList.clear();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        User user = data.getValue(User.class);
                        if (user != null) {
                            userList.add(user);
                        }
                    }
                    usersAdapter.filter(""); // טוען את כל המשתמשים ומציג אותם
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // טיפול בשגיאה
                }
            });
        }
    }
