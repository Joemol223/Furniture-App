package com.example.joemol_joy_project2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private final ArrayList<Product> mProduct;
    private final ProductInterface productInterface;

    public ProductAdapter(ArrayList<Product> productList, ProductInterface productInterface) {
        this.mProduct = productList;
        this.productInterface = productInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);
        return new ViewHolder(view, productInterface);
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

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView productName;
        TextView productDesc;
        TextView productPrice;
        ImageView productImage;
        ViewHolder(@NonNull View itemView, ProductInterface productInterface){
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productDesc = itemView.findViewById(R.id.productDesc);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImg);

            itemView.setOnClickListener(view ->{
                if(productInterface != null){
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        productInterface.onItemClick(position);
                    }

                }
            });
        }

        void bind(Product product){
           productName.setText(product.getName());
           productDesc.setText(product.getDesc());
           productPrice.setText("$" + String.valueOf(product.getPrice()));
           Picasso.get().load(product.getImage()).into(productImage);
        }
    }
}
