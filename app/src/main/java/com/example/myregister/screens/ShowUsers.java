package com.example.myregister.screens;

import android.os.Bundle;
import android.util.Log;
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
import com.example.myregister.services.DatabaseService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

public class ShowUsers extends AppCompatActivity {

        private RecyclerView recyclerView;
        private UserAdapter usersAdapter;

        private Button backButton;
        private SearchView userSearchView;
        private List<User> userList= new ArrayList<>();
    private DatabaseService databaseService;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_show_users);


            databaseService= DatabaseService.getInstance();

            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }

            // קישור רכיבים מה-XML
          //  userSearchView = findViewById(R.id.userSearchView);
            recyclerView = findViewById(R.id.UserRecyclerView);
            backButton = findViewById(R.id.backButton);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));


            userList = new ArrayList<>();
            usersAdapter = new UserAdapter(userList, this);
            recyclerView.setAdapter(usersAdapter);

            // טעינת המשתמשים מהפיירבייס
            fetchUsers();

            // כפתור חזור
            backButton.setOnClickListener(v -> finish());

            // מאזין לחיפוש משתמשים
//            userSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    usersAdapter.filter(query);
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    usersAdapter.filter(newText);
//                    return false;
//                }
//            });
        }




        private void fetchUsers() {




            databaseService.getUsers(new DatabaseService.DatabaseCallback<List<User>>() {




        @Override
        public void onCompleted(List<User> object) {
            Log.d("TAG", "onCompleted: " + object);
            userList.clear();
            userList.addAll(object);
                // notify the adapter that the data has changed
               // this specifies that the data has changed
                // and the adapter should update the view

                 usersAdapter.notifyDataSetChanged();



        }




        @Override
        public void onFailed(Exception e) {
            Log.e("TAG", "onFailed: ", e);
        }
    });
//
            }
    }
