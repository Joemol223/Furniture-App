package com.example.joemol_joy_project2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private ArrayList<Product> mProduct;
    private ProductInterface productInterface;
    private Context context;

    public ProductAdapter(ArrayList<Product> productList, ProductInterface productInterface, FirebaseDatabase database, FirebaseAuth auth, Context context) {
        this.mProduct = productList;
        this.productInterface = productInterface;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = mProduct.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return mProduct.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productDesc;
        TextView productPrice;
        ImageView productImage;
        Button cartBtn;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productDesc = itemView.findViewById(R.id.productDesc);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImg);
            cartBtn = itemView.findViewById(R.id.cartBtn);

            itemView.setOnClickListener(view -> {
                if (productInterface != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        productInterface.onItemClick(position);
                    }

                }
            });

            cartBtn.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Product product = mProduct.get(position);
                    addToCart(product);
                }
            });
        }

        void bind(Product product) {
            productName.setText(product.getName());
            productDesc.setText(product.getDesc());
            productPrice.setText(String.format("$%.2f", product.getPrice()));
            Picasso.get().load(product.getImage()).into(productImage);
        }

        private void addToCart(Product product) {
            DatabaseReference cartReference = FirebaseDatabase.getInstance().getReference().child("AddToCart").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("CurrentUser");

            String saveCurrentDate, saveCurrentTime;
            Calendar calForDate = Calendar.getInstance();

            SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
            saveCurrentDate = currentDate.format(calForDate.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:MM:SS a");
            saveCurrentTime = currentTime.format(calForDate.getTime());

            double totalPrice = product.getPrice();
            int totalQuantity = 1;

            HashMap<String, Object> cartMap = new HashMap<>();
            cartMap.put("productImage", product.getImage());
            cartMap.put("productName", product.getName());
            cartMap.put("productPrice", product.getPrice());
            cartMap.put("currentDate", saveCurrentDate);
            cartMap.put("currentTime", saveCurrentTime);
            cartMap.put("totalQuantity", totalQuantity);
            cartMap.put("totalPrice", totalPrice);

            cartReference.orderByChild("productName").equalTo(product.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            int existingQuantity = snapshot.child("totalQuantity").getValue(Integer.class);
                            int newQuantity = existingQuantity + totalQuantity;
                            double newPrice = product.getPrice() * newQuantity;
                            snapshot.getRef().child("totalQuantity").setValue(newQuantity);
                            snapshot.getRef().child("totalPrice").setValue(newPrice);
                            Toast.makeText(context, "Cart Updated", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        cartReference.push().setValue(cartMap);
                        Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                    Toast.makeText(context, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
