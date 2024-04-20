package com.example.joemol_joy_project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText textInputEditEmail, textInputEditPassword;
    TextInputLayout textInputEmailBox, textInputPasswordBox;
    Button btnSignUp;
    TextView loginLink;
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        textInputEditEmail = findViewById(R.id.textInputEditEmail);
        textInputEditPassword = findViewById(R.id.textInputEditPassword);
        textInputEmailBox = findViewById(R.id.textInputEmailBox);
        textInputPasswordBox = findViewById(R.id.textInputPasswordBox);
        btnSignUp = findViewById(R.id.btnSignUp);
        loginLink = findViewById(R.id.loginLink);

        btnSignUp.setOnClickListener(view -> {
            String email, password;
            email = String.valueOf(textInputEditEmail.getText());
            password = String.valueOf(textInputEditPassword.getText());

            if(TextUtils.isEmpty(email)){
                textInputEmailBox.setError("Enter email");
                textInputEditEmail.setError("Enter email");
                return;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                textInputEmailBox.setError("Invalid email address");
                textInputEditEmail.setError("Invalid email address");
                return;

        } else {
        textInputEmailBox.setError(null);
        textInputEditEmail.setError(null);
        }
            if(TextUtils.isEmpty(password)){
                textInputPasswordBox.setError("Enter password");
                textInputEditPassword.setError("Enter password");
                return;
            } else if (password.length() <= 6) {
                textInputPasswordBox.setError("Password must be at least 6 characters long");
                textInputEditPassword.setError("Password must be at least 6 characters long");
                return;
            }else {
                textInputPasswordBox.setError(null);
                textInputEditPassword.setError(null);
            }

        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(RegisterActivity.this, createUserTask -> {
                                        if (createUserTask.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                    } else {
                        Log.e("CheckEmail", "Error checking email existence", task.getException());
                        Toast.makeText(RegisterActivity.this, "Error checking email existence", Toast.LENGTH_SHORT).show();
                    }
                });
    });

        loginLink.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}