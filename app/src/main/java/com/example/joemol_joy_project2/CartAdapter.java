package com.example.joemol_joy_project2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private ArrayList<CartItem> mCartItems;
    private Context mContext;
    private DatabaseReference mCartReference;
    private FirebaseAuth mAuth;

    public CartAdapter(ArrayList<CartItem> cartItems, Context context, DatabaseReference cartReference) {
        this.mCartItems = cartItems;
        this.mContext = context;
        this.mCartReference = cartReference;
        this.mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_card, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        CartItem cart = mCartItems.get(position);
        holder.bind(cart);
    }

    @Override
    public int getItemCount() {
        return mCartItems.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        TextView cartProductName, cartProductPrice, count, quantityPrice;
        ImageView cartImg, delete, addCount, minusCount;
        CartItem currentItem;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cartProductName = itemView.findViewById(R.id.cartProductName);
            cartProductPrice = itemView.findViewById(R.id.cartProductPrice);
            quantityPrice = itemView.findViewById(R.id.quantityPrice);
            cartImg = itemView.findViewById(R.id.cartImg);
            delete = itemView.findViewById(R.id.delete);
            count = itemView.findViewById(R.id.count);
            addCount = itemView.findViewById(R.id.addCount);
            minusCount = itemView.findViewById(R.id.minusCount);

            addCount.setOnClickListener(v -> {
                if(currentItem.getQuantity() < 10) {
                    currentItem.setQuantity(currentItem.getQuantity() + 1);
                    count.setText(String.valueOf(currentItem.getQuantity()));
                    updateQuantityPrice();
                }
            });

            minusCount.setOnClickListener(v -> {
                if (currentItem.getQuantity() > 1) {
                    currentItem.setQuantity(currentItem.getQuantity() - 1);
                    count.setText(String.valueOf(currentItem.getQuantity()));
                    updateQuantityPrice();
                }
            });

            delete.setOnClickListener(v -> deleteProductFromCart(currentItem));
        }

        void bind(CartItem cartItem) {
            currentItem = cartItem;
            cartProductName.setText(cartItem.getName());
            cartProductPrice.setText(String.format("$%.2f", cartItem.getPrice()) + "/item");
            count.setText(String.valueOf(cartItem.getQuantity()));
            updateQuantityPrice();
            Picasso.get().load(cartItem.getImage()).into(cartImg);
        }

        private void updateQuantityPrice() {
            double totalPrice = currentItem.getPrice() * currentItem.getQuantity();
            quantityPrice.setText(String.format("$%.2f", totalPrice));

            DatabaseReference userCartReference = mCartReference.child(mAuth.getCurrentUser().getUid()).child("CurrentUser");

            userCartReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        String existingProductName = productSnapshot.child("productName").getValue(String.class);
                        if (existingProductName != null && existingProductName.equals(currentItem.getName())) {
                            productSnapshot.getRef().child("totalQuantity").setValue(currentItem.getQuantity());
                            productSnapshot.getRef().child("totalPrice").setValue(totalPrice);
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("CartAdapter", "Failed to update cart", databaseError.toException());
                }
            });
        }

        private void deleteProductFromCart(CartItem cartItem) {
            DatabaseReference userCartReference = mCartReference.child(mAuth.getCurrentUser().getUid()).child("CurrentUser");
            userCartReference.orderByChild("productName").equalTo(cartItem.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        productSnapshot.getRef().removeValue();
                        Toast.makeText(mContext, "Product Removed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(mContext, "Failed to delete product from cart", Toast.LENGTH_SHORT).show();
                    Log.e("CartAdapter", "Failed to delete product from cart", databaseError.toException());
                }
            });
        }
    }
}
