package com.example.myregister.screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "loginToFireBase";

    EditText etEmailLogin, etPasswordLogin;
    Button btnLogin;

    String email2, pass2;
    private FirebaseAuth mAuth;
    public static final String admin = "levyarin14@gmail.com";
    String adminPass = "010407";
    public static final String MyPREFERENCES = "MyPrefs";

    SharedPreferences sharedpreferences;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    public static User theUser;
    public static Boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        initViews();

        email2 = sharedpreferences.getString("email", "");
        pass2 = sharedpreferences.getString("password", "");
        etEmailLogin.setText(email2);
        etPasswordLogin.setText(pass2);
    }

    private void initViews() {
        etEmailLogin = findViewById(R.id.etEmailLog);
        etPasswordLogin = findViewById(R.id.etPassLog);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        email2 = etEmailLogin.getText().toString().trim();
        pass2 = etPasswordLogin.getText().toString().trim();

        if (email2.isEmpty() || pass2.isEmpty()) {
            Toast.makeText(Login.this, "יש למלא אימייל וסיסמה", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email2, pass2)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("email", email2);
                        editor.putString("password", pass2);
                        editor.apply();

                        FirebaseUser user = mAuth.getCurrentUser();
                        myRef = database.getReference("Users").child(user.getUid());

                        if (email2.equals(admin) && pass2.equals(adminPass)) {
                            isAdmin = true;
                            startActivity(new Intent(Login.this, AdminPage.class));
                        } else {
                            startActivity(new Intent(Login.this, HomePage.class));
                        }
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Exception e = task.getException();
                        if (e != null) {
                            Toast.makeText(Login.this, "שגיאה: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Login.this, "ההתחברות נכשלה", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
    }
}
