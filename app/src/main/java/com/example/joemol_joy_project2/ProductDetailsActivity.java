package com.example.joemol_joy_project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import kotlin.collections.DoubleIterator;

public class ProductDetailsActivity extends AppCompatActivity {

    TextView nameDetail, descDetail, priceDetail, quantity;
    ImageView imageDetail, backBtn, cartImage, addCount, minusCount;
    Button addToCartBtn;
    int totalQuantity = 1;
    double totalPrice = 0;
    String name, longDesc, url;
    Double price;

    DatabaseReference cartReference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        name = getIntent().getStringExtra("name");
        longDesc = getIntent().getStringExtra("longDesc");
        price = getIntent().getDoubleExtra("price",15.99);
        url = getIntent().getStringExtra("url");

        nameDetail = findViewById(R.id.nameDetail);
        descDetail = findViewById(R.id.descDetail);
        priceDetail = findViewById(R.id.priceDetail);
        imageDetail = findViewById(R.id.imageDetail);
        backBtn = findViewById(R.id.backBtn);
        cartImage = findViewById(R.id.cartImage);
        addToCartBtn = findViewById(R.id.addToCartBtn);
        quantity = findViewById(R.id.count);
        addCount = findViewById(R.id.addCount);
        minusCount = findViewById(R.id.minusCount);

        nameDetail.setText(name);
        descDetail.setText(longDesc);
        priceDetail.setText("$" + String.valueOf(price));
        Picasso.get().load(url).into(imageDetail);

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ProductDetailsActivity.this, ProductListActivity.class);
            startActivity(intent);
        });

        cartImage.setOnClickListener(view -> {
            Intent intent = new Intent(ProductDetailsActivity.this, CartActivity.class);
            startActivity(intent);
        });

        addCount.setOnClickListener(view -> {
            if(totalQuantity < 10){
                totalQuantity++;
                quantity.setText(String.valueOf(totalQuantity));
            }
        });

        minusCount.setOnClickListener(view -> {
            if(totalQuantity > 1){
                totalQuantity--;
                quantity.setText(String.valueOf(totalQuantity));
            }
        });

        totalPrice = price * totalQuantity;
        cartReference = FirebaseDatabase.getInstance().getReference().child("AddToCart");
        auth = FirebaseAuth.getInstance();



        addToCartBtn.setOnClickListener(view -> {
            adddedToCart();
        });

    }

    private void adddedToCart() {
        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        // Calculate total price
        totalPrice = price * totalQuantity;

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:MM:SS a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();

        cartMap.put("productName", name);
        cartMap.put("productPrice", priceDetail.getText().toString());
        cartMap.put("currentDate", saveCurrentDate);
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("totalQuantity", quantity.getText().toString());
        cartMap.put("totalPrice", totalPrice);

        cartReference.child(auth.getCurrentUser().getUid())
                .child("CurrentUser").push().setValue(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(ProductDetailsActivity.this, "Added To Cart", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ProductDetailsActivity.this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}