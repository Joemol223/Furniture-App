package com.example.joemol_joy_project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
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
                Toast.makeText(this,"Enter email",Toast.LENGTH_SHORT).show();
                textInputEmailBox.setError("Enter email");
                return;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textInputEmailBox.setError("Invalid email address");
            return;
        } else {
            textInputEmailBox.setError(null);
        }
            if(TextUtils.isEmpty(password)){
                Toast.makeText(this,"Enter password",Toast.LENGTH_SHORT).show();
                textInputPasswordBox.setError("Enter password");
                return;
            }else {
                textInputPasswordBox.setError(null);
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Account Created",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

        loginLink.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}