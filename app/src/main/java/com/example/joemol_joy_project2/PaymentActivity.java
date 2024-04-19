package com.example.joemol_joy_project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class PaymentActivity extends AppCompatActivity {
    ImageView backBtn;
    Button orderBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        backBtn = findViewById(R.id.backBtn);
        orderBtn = findViewById(R.id.orderBtn);

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, CheckoutActivity.class);
            startActivity(intent);
        });

        orderBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, ThankyouPageActivity.class);
            startActivity(intent);
        });
    }
}