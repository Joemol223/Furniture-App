package com.example.joemol_joy_project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    TextView nameDetail, descDetail, priceDetail, quantity;
    ImageView imageDetail, backBtn, cartImage, addCount, minusCount;
    Button addToCartBtn;
    int totalQuantity = 1;
    double totalPrice = 0;
    String name, longDesc, url, detailUrl;
    Double price;
    DatabaseReference cartReference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        name = getIntent().getStringExtra("name");
        longDesc = getIntent().getStringExtra("longDesc");
        price = getIntent().getDoubleExtra("price",00.00);
        url = getIntent().getStringExtra("url");
        detailUrl = getIntent().getStringExtra("detailUrl");

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
        priceDetail.setText(String.format("$%.2f", price));
        Picasso.get().load(detailUrl).into(imageDetail);

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
            addedToCart();
        });
    }

    private void addedToCart() {
        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        // Calculate total price
        totalPrice = price * totalQuantity;

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("productImage", url);
        cartMap.put("productName", name);
        cartMap.put("productPrice",price);
        cartMap.put("currentDate", saveCurrentDate);
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("totalQuantity", totalQuantity);
        cartMap.put("totalPrice", totalPrice);

        DatabaseReference userCartReference = cartReference.child(auth.getCurrentUser().getUid()).child("CurrentUser");

        userCartReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean productExists = false;
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    String existingProductName = productSnapshot.child("productName").getValue(String.class);
                    if (existingProductName != null && existingProductName.equals(name)) {

                        // Product exists in the cart, update its quantity and price
                        int existingQuantity = Integer.parseInt(String.valueOf(productSnapshot.child("totalQuantity").getValue()));;
                        int newQuantity = existingQuantity + totalQuantity;
                        double newPrice = price * newQuantity;
                        productSnapshot.getRef().child("totalQuantity").setValue(newQuantity);
                        productSnapshot.getRef().child("totalPrice").setValue(newPrice);
                        productExists = true;
                        Toast.makeText(ProductDetailsActivity.this, "Cart Updated", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if (!productExists) {
                    // Product does not exist in the cart, add it
                    userCartReference.push().setValue(cartMap);
                }
                Toast.makeText(ProductDetailsActivity.this, "Added To Cart", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Toast.makeText(ProductDetailsActivity.this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
