package com.example.joemol_joy_project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<CartItem> cartList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.action_cart);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if(id == R.id.action_product){
                startActivity(new Intent(getApplicationContext(), ProductListActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;}
            else if(id == R.id.action_cart) {
                return true;
            }
            else if(id == R.id.action_account) {
                startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        recyclerView = findViewById(R.id.rView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        reference = FirebaseDatabase.getInstance().getReference().child("AddToCart");

        cartList = new ArrayList<>();
        mAdapter = new CartAdapter(cartList, this, reference);
        recyclerView.setAdapter(mAdapter);

        database =  FirebaseDatabase.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            reference = database.getReference().child("AddToCart").child(userId).child("CurrentUser");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    cartList.clear(); // Clear the existing list before adding new items
                    for (DataSnapshot cartSnapshot : snapshot.getChildren()) {
                        String url = cartSnapshot.child("productImage").getValue(String.class);
                        String name = cartSnapshot.child("productName").getValue(String.class);
                        Integer quantity = cartSnapshot.child("totalQuantity").getValue(Integer.class);
                        Double price = cartSnapshot.child("productPrice").getValue(Double.class);
                        String firebaseKeyId = cartSnapshot.getKey();

                        if (name != null &&  url != null && price != null && quantity != null) {
                            CartItem cartItem = new CartItem(firebaseKeyId, url, name, price, quantity);
                            cartList.add(cartItem);
                        } else {
                            Log.e("Error", "One or more fields are null for product with ID: " + cartSnapshot.getKey());
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Database error", error.getMessage());
                    Toast.makeText(CartActivity.this, "Error retrieving data from Firebase", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
