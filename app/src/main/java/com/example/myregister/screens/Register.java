package com.example.myregister.screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myregister.R;
import com.example.myregister.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "Register";
    
    EditText etFname, etLname, etPhone, etEmail, etPassword;
    String fName, lName, Phone, Email, Password;
    String city;
    Spinner spCity;
    Button btnReg;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        
        // Initialize SharedPreferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        
        init_views();
    }

    private void init_views() {
        btnReg = findViewById(R.id.btnReg);
        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        spCity = findViewById(R.id.spiCity);

        btnReg.setOnClickListener(this);
        spCity.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnReg) {
            registerUser();
        }
    }

    private void registerUser() {
        // Get input values
        fName = etFname.getText().toString().trim();
        lName = etLname.getText().toString().trim();
        Phone = etPhone.getText().toString().trim();
        Email = etEmail.getText().toString().trim();
        Password = etPassword.getText().toString().trim();

        // Validate input
        if (!validateInput()) {
            return;
        }

        // Show progress to user
        Toast.makeText(Register.this, "מבצע רישום...", Toast.LENGTH_SHORT).show();

        // Create user in Firebase Auth
        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser fireuser = mAuth.getCurrentUser();
                            if (fireuser != null) {
                                // Create user object
                                User newUser = new User(fireuser.getUid(), fName, lName, Phone, Email, Password, city);
                                
                                // Save user to database
                                myRef.child(fireuser.getUid()).setValue(newUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Save credentials
                                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                                editor.putString("email", Email);
                                                editor.putString("password", Password);
                                                editor.apply();

                                                // Show success message
                                                Toast.makeText(Register.this, "הרישום הושלם בהצלחה!", Toast.LENGTH_SHORT).show();

                                                // Go to login page
                                                Intent goLog = new Intent(Register.this, Login.class);
                                                goLog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(goLog);
                                                finish();
                                            } else {
                                                // Failed to save user data
                                                Toast.makeText(Register.this, "שגיאה בשמירת נתוני המשתמש", Toast.LENGTH_LONG).show();
                                                // Delete the auth user since we couldn't save their data
                                                fireuser.delete();
                                            }
                                        }
                                    });
                            }
                        } else {
                            // Registration failed
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Authentication failed";
                            Toast.makeText(Register.this, "שגיאה: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean validateInput() {
        boolean isValid = true;

        if (fName.length() < 2) {
            etFname.setError("שם פרטי קצר מדי");
            isValid = false;
        }
        if (lName.length() < 2) {
            etLname.setError("שם משפחה קצר מדי");
            isValid = false;
        }
        if (Phone.length() < 9 || Phone.length() > 10) {
            etPhone.setError("מספר הטלפון לא תקין");
            isValid = false;
        }
        if (!Email.contains("@")) {
            etEmail.setError("כתובת האימייל לא תקינה");
            isValid = false;
        }
        if (Password.length() < 6) {
            etPassword.setError("הסיסמה חייבת להכיל לפחות 6 תווים");
            isValid = false;
        }
        if (Password.length() > 20) {
            etPassword.setError("הסיסמה ארוכה מדי");
            isValid = false;
        }
        if (city == null || city.isEmpty()) {
            Toast.makeText(this, "אנא בחר עיר", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        city = (String) adapterView.getItemAtPosition(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        city = (String) adapterView.getItemAtPosition(0);
    }

    public void onGobackClick(View view) {
        Intent go = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(go);
        finish();
    }
}

