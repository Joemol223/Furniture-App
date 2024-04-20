package com.example.joemol_joy_project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class ProductListActivity extends AppCompatActivity implements ProductInterface{


    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<Product> productList ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.action_product);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

                if(id == R.id.action_product){
                    return true;}
                else if(id == R.id.action_cart) {
                    startActivity(new Intent(getApplicationContext(), CartActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
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

        productList = new ArrayList<>();
        mAdapter = new ProductAdapter(productList, this, database, FirebaseAuth.getInstance(), this);
        recyclerView.setAdapter(mAdapter);

        database =  FirebaseDatabase.getInstance();
        reference = database.getReference().child("Product");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    String name = productSnapshot.child("name").getValue(String.class);
                    String desc = productSnapshot.child("desc").getValue(String.class);
                    String longDesc = productSnapshot.child("longDesc").getValue(String.class);
                    String url = productSnapshot.child("url").getValue(String.class);
                    String detailUrl = productSnapshot.child("detailUrl").getValue(String.class);
                    String color = productSnapshot.child("color").getValue(String.class);
                    Double price = productSnapshot.child("price").getValue(Double.class);

                    if (name != null && detailUrl != null && desc != null && longDesc != null &&  url != null &&  color != null  && price != null) {
                        Product product = new Product(url, detailUrl, name, desc,longDesc, color, price);
                        productList.add(product);
                    } else {
                        Log.e("Error", "One or more fields are null for product with ID: " + productSnapshot.getKey());
                    }
                }
               mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database error", error.getMessage());
                Toast.makeText(ProductListActivity.this, "Error retrieving data from Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ProductListActivity.this, ProductDetailsActivity.class);

        intent.putExtra("name", productList.get(position).getName());
        intent.putExtra("longDesc", productList.get(position).getLongDesc());
        intent.putExtra("price", productList.get(position).getPrice());
        intent.putExtra("url", productList.get(position).getImage());
        intent.putExtra("detailUrl", productList.get(position).getDetailUrl());
        intent.putExtra("color", productList.get(position).getColor());

        startActivity(intent);
    }
}