package com.example.joemol_joy_project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ThankyouPageActivity extends AppCompatActivity {

    Button shoppingBtn;
    ImageView logoutBtn;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou_page);

        shoppingBtn = findViewById(R.id.shoppingBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        shoppingBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, ProductListActivity.class);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}