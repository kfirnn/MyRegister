package com.example.myregister.screens;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myregister.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    private TextInputEditText editFirstName, editLastName, editEmail, editPhone;
    private TextInputEditText editCurrentPassword, editNewPassword, editConfirmPassword;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            finish();
            return;
        }
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Profile Settings");
        }

        // Initialize views
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        editCurrentPassword = findViewById(R.id.editCurrentPassword);
        editNewPassword = findViewById(R.id.editNewPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        Button btnSaveProfile = findViewById(R.id.btnSaveProfile);

        // Load user data
        loadUserData();

        // Save button click listener
        btnSaveProfile.setOnClickListener(v -> saveChanges());
    }

    private void loadUserData() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String fName = snapshot.child("fName").getValue(String.class);
                    String lName = snapshot.child("lName").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);

                    if (fName != null) editFirstName.setText(fName);
                    if (lName != null) editLastName.setText(lName);
                    if (email != null) editEmail.setText(email);
                    if (phone != null) editPhone.setText(phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Error loading profile: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveChanges() {
        String newFirstName = Objects.requireNonNull(editFirstName.getText()).toString().trim();
        String newLastName = Objects.requireNonNull(editLastName.getText()).toString().trim();
        String newEmail = Objects.requireNonNull(editEmail.getText()).toString().trim();
        String newPhone = Objects.requireNonNull(editPhone.getText()).toString().trim();
        String currentPassword = Objects.requireNonNull(editCurrentPassword.getText()).toString();
        String newPassword = Objects.requireNonNull(editNewPassword.getText()).toString();
        String confirmPassword = Objects.requireNonNull(editConfirmPassword.getText()).toString();

        // Validate inputs
        if (TextUtils.isEmpty(newFirstName) || TextUtils.isEmpty(newLastName) || 
            TextUtils.isEmpty(newEmail) || TextUtils.isEmpty(newPhone)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update profile information
        Map<String, Object> updates = new HashMap<>();
        updates.put("fName", newFirstName);
        updates.put("lName", newLastName);
        updates.put("phone", newPhone);

        // Update email if changed
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null && !user.getEmail().equals(newEmail)) {
            user.updateEmail(newEmail)
                    .addOnSuccessListener(aVoid -> updates.put("email", newEmail))
                    .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this,
                            "Failed to update email: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }

        // Update password if provided
        if (!TextUtils.isEmpty(currentPassword) && !TextUtils.isEmpty(newPassword)) {
            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "New passwords don't match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (user != null) {
                user.reauthenticate(EmailAuthProvider.getCredential(user.getEmail(), currentPassword))
                        .addOnSuccessListener(aVoid -> user.updatePassword(newPassword)
                                .addOnSuccessListener(aVoid1 -> {
                                    Toast.makeText(ProfileActivity.this,
                                            "Password updated successfully", Toast.LENGTH_SHORT).show();
                                    updates.put("password", newPassword);
                                })
                                .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this,
                                        "Failed to update password: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show()))
                        .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this,
                                "Current password is incorrect", Toast.LENGTH_SHORT).show());
            }
        }

        // Update profile data in database
        userRef.updateChildren(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ProfileActivity.this, "Profile updated successfully",
                            Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this,
                        "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 